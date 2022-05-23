package fi.tuni.foodrecipes

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.material.bottomnavigation.BottomNavigationView
import fi.tuni.foodrecipes.favourites.FavouritesFragment
import fi.tuni.foodrecipes.home.HomeFragment
import fi.tuni.foodrecipes.recipe.RecipeDetailsFragment
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.thread
import kotlin.math.sqrt

@JsonIgnoreProperties(ignoreUnknown = true)
data class randomJoke(val text : String) {

    constructor() : this(text = "")

    override fun toString(): String {
        return text
    }
}


class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView
    private var bundle = Bundle()

    //For detecting phones movement
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    private val MIN_TIME_BETWEEN_SHAKES_MILLISECS = 2000
    private var mLastShakeTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNav = findViewById(R.id.bottom_navigation)

        val homeFragment = HomeFragment()
        val favouritesFragment = FavouritesFragment()

        //SensorManager to detect phones movement
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(sensorManager)!!.registerListener(sensorListener, sensorManager!!
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH

        bundle.putIntArray("favourites", intArrayOf(1, 2))

        homeFragment.arguments = bundle
        favouritesFragment.arguments = bundle

        setCurrentFragment(homeFragment)
        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.page_1 -> {
                    setCurrentFragment(homeFragment)
                }
                R.id.page_2 -> {
                    setCurrentFragment(favouritesFragment)

                }
            }
            return@setOnItemSelectedListener true
        }
    }

    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                val curTime = System.currentTimeMillis()
                if ((curTime - mLastShakeTime) > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {

                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    lastAcceleration = currentAcceleration
                    currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                    val delta: Float = currentAcceleration - lastAcceleration
                    acceleration = acceleration * 0.9f + delta
                    if (acceleration > 12) {
                        mLastShakeTime = curTime
                        Log.d("shake", "shake")
                        thread {
                            val word =
                                fetchRandomJoke("https://api.spoonacular.com/food/jokes/random?apiKey=a84165b11bbe41f0ae6ff525b82eed8e")
                            if (word != null) {
                                Log.d("shake", word)
                            }
                            runOnUiThread {
                                Toast.makeText(applicationContext, word, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    }
                }
            }
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            //not needed
        }
    }

    private fun fetchRandomJoke(url: String) : String? {
        var result: String? = null
        val url = URL(url)
        val sb = StringBuffer()
        val connection = url.openConnection() as HttpURLConnection
        val reader = BufferedReader(InputStreamReader(connection.inputStream))

        reader.use {
            var line: String? = null
            do {
                line = it.readLine()
                sb.append(line)
            } while (line != null)
            result = sb.toString()
        }

        val mp = ObjectMapper()
        val text = mp.readValue(result, randomJoke::class.java)

        return text.text
    }

    // "https://api.spoonacular.com/recipes/findByIngredients?ingredients=apples,+flour,+sugar&number=2&apiKey=a84165b11bbe41f0ae6ff525b82eed8e"
    // "https://api.spoonacular.com/recipes/1234/information/?apiKey=a84165b11bbe41f0ae6ff525b82eed8e"
    // "https://api.spoonacular.com/recipes/complexSearch?query=pasta&maxFat=25&number=2&apiKey=a84165b11bbe41f0ae6ff525b82eed8e"


    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("favouritesString", bundle.getString("favouritesString"))
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        bundle.putString("favouritesString", savedInstanceState.getString("favouritesString"))
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onResume() {
        sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }
    override fun onPause() {
        sensorManager!!.unregisterListener(sensorListener)
        super.onPause()
    }
}
//used for closing the keyboard
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}
//used for closing the keyboard
fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}
//used for closing the keyboard
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}