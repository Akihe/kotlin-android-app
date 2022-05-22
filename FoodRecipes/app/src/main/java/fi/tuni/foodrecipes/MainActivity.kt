package fi.tuni.foodrecipes

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import fi.tuni.foodrecipes.favourites.FavouritesFragment
import fi.tuni.foodrecipes.home.HomeFragment
import java.util.*
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView
    private var bundle = Bundle()

    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNav = findViewById(R.id.bottom_navigation)

        val homeFragment = HomeFragment()
        val favouritesFragment = FavouritesFragment()

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
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta
            if (acceleration > 12) {
                Toast.makeText(applicationContext, "shake event detected", Toast.LENGTH_SHORT).show()
            }

        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

        }
    }
    // "https://api.spoonacular.com/recipes/findByIngredients?ingredients=apples,+flour,+sugar&number=2&apiKey=a84165b11bbe41f0ae6ff525b82eed8e"
    // "https://api.spoonacular.com/recipes/1234/information/?apiKey=a84165b11bbe41f0ae6ff525b82eed8e"
    // "https://api.spoonacular.com/recipes/complexSearch?query=pasta&maxFat=25&number=2&apiKey=a84165b11bbe41f0ae6ff525b82eed8e"


    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        this.bundle = savedInstanceState
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
/*
var apikey = "a84165b11bbe41f0ae6ff525b82eed8e"
var baseurl = "https://api.spoonacular.com/"
retrofit = Retrofit.Builder().baseUrl("https://api.spoonacular.com/").build()
//fetch("https://api.spoonacular.com/recipes/findByIngredients?ingredients=apples,+flour,+sugar&number=2&apiKey=a84165b11bbe41f0ae6ff525b82eed8e")

fun fetcher(url:String) {
    //var data = @GET("/food/ingredients/search?query=banana&number=2&sort=calories&sortDirection=desc&apiKey=a84165b11bbe41f0ae6ff525b82eed8e")
}
 */