package fi.tuni.foodrecipes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import fi.tuni.foodrecipes.favourites.FavouritesFragment
import fi.tuni.foodrecipes.home.HomeFragment

class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView
    val bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNav = findViewById(R.id.bottom_navigation)

        val homeFragment = HomeFragment()
        val favouritesFragment = FavouritesFragment()


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
    // "https://api.spoonacular.com/recipes/findByIngredients?ingredients=apples,+flour,+sugar&number=2&apiKey=a84165b11bbe41f0ae6ff525b82eed8e"
    // "https://api.spoonacular.com/recipes/1234/information/?apiKey=a84165b11bbe41f0ae6ff525b82eed8e"
    // "https://api.spoonacular.com/recipes/complexSearch?query=pasta&maxFat=25&number=2&apiKey=a84165b11bbe41f0ae6ff525b82eed8e"


    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
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