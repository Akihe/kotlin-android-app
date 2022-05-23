package fi.tuni.foodrecipes

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.material.bottomnavigation.BottomNavigationView
import fi.tuni.foodrecipes.favourites.FavouritesFragment
import fi.tuni.foodrecipes.home.HomeFragment
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.concurrent.thread
import kotlin.math.sqrt

/**
 * An object for a random joke fetched from the api
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class randomJoke(val text : String) {
    constructor() : this(text = "")
    override fun toString(): String {
        return text
    }
}

/**
 * MainActivity holds stuff behinds the scenes, but views are displayed as Fragments.
 * The only activity thats used in this app
 */
class MainActivity : AppCompatActivity() {


    lateinit var bottomNav : BottomNavigationView

    // For detecting phones movement
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    // Prevents the shaking effect to be called too often
    private val MIN_TIME_BETWEEN_SHAKES_MILLISECS = 2000
    private var mLastShakeTime = 0L

    // Home-view
    val homeFragment = HomeFragment()
    // Favourites-view
    val favouritesFragment = FavouritesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNav = findViewById(R.id.bottom_navigation)


        //SensorManager to detect phones movement
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(sensorManager)!!.registerListener(sensorListener, sensorManager!!
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH

        // Set the first view to be homeFragment when opening the app
        setCurrentFragment(homeFragment)

        // Handles changing the view to a different fragment when pressing an item in the navbar
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

    /**
     * Used for listening the phones movement, calculates the speed that the phone is moving on
     * If the speed is fast enough, its treated as shaking the phone
     */
    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                // Current time from the system, to detect time between shakes
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
                        // If the speed to shake the phone was enough,
                        // We call the api and fetch a random joke to be displayed at the bottom of the screen
                        // If i had time this would be shown on a new fragment, there is not enough time to read the joke now
                        try {
                            thread {
                                val word =
                                    fetchRandomJoke("https://api.spoonacular.com/food/jokes/random?apiKey=a84165b11bbe41f0ae6ff525b82eed8e")
                                runOnUiThread {
                                    Toast.makeText(applicationContext, word, Toast.LENGTH_LONG)
                                        .show()
                                }
                            }
                        } catch (e: Exception) {
                            runOnUiThread {
                                Toast.makeText(applicationContext, "Api call failed", Toast.LENGTH_LONG)
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

    /**
     * Fetch a random joke from the api
     */
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

    /**
     * Handles changing the fragment. An UI-element called flFragment is replaced with the given fragment (home or favourites).
     */
    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    override fun onSaveInstanceState(outState: Bundle) {
        homeFragment.saveData(filesDir)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        homeFragment.openData(filesDir)
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
