package fi.tuni.foodrecipes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.material.bottomnavigation.BottomNavigationView
import fi.tuni.foodrecipes.favourites.FavouritesFragment
import fi.tuni.foodrecipes.home.HomeFragment
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


/*
@JsonIgnoreProperties(ignoreUnknown = true)
data class Recipe(var id : Int, var title: String? = null, var image: String) {

    constructor() : this(id = 0, title = null, image = "")

    override fun toString(): String {
        return "title = $title"
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class RecipeJsonObject(var results: MutableList<Recipe>? = null) {
}
*/



class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

/*
    lateinit var fetchButton : Button
    var myAdapter = CustomAdapter()
    lateinit var recyclerView : RecyclerView
*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNav = findViewById(R.id.bottom_navigation)

/*
        fetchButton = findViewById(R.id.fetchbutton)
        recyclerView = findViewById(R.id.recipeList)

        recyclerView
            .apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
            }

        thread {
            var data = createObjects()
                myAdapter.setData(data)
            runOnUiThread {
                recyclerView.adapter = myAdapter
            }
        }
*/

        val homeFragment = HomeFragment()
        val favouritesFragment = FavouritesFragment()

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
/*        fetchButton.setOnClickListener {
            thread {
                createObjects()
            }
        }*/
    }
    // "https://api.spoonacular.com/recipes/findByIngredients?ingredients=apples,+flour,+sugar&number=2&apiKey=a84165b11bbe41f0ae6ff525b82eed8e"
    // "https://api.spoonacular.com/recipes/1234/information/?apiKey=a84165b11bbe41f0ae6ff525b82eed8e"
    // "https://api.spoonacular.com/recipes/complexSearch?query=pasta&maxFat=25&number=2&apiKey=a84165b11bbe41f0ae6ff525b82eed8e"


    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

/*
    fun fetch (url : String) : String? {
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
            Log.d("api", result!!)
        return result
    }

    fun createObjects () : MutableList<Recipe> {
        var data = fetch("https://api.spoonacular.com/recipes/complexSearch?query=pasta&maxFat=25&apiKey=a84165b11bbe41f0ae6ff525b82eed8e")

        val mp = ObjectMapper()
        val myObject: RecipeJsonObject = mp.readValue(data, RecipeJsonObject::class.java)
        val recipes: MutableList<Recipe>? = myObject.results
        return recipes as MutableList<Recipe>
    }
*/
}
/*
var apikey = "a84165b11bbe41f0ae6ff525b82eed8e"
var baseurl = "https://api.spoonacular.com/"
retrofit = Retrofit.Builder().baseUrl("https://api.spoonacular.com/").build()

var intent = Intent(this, FavouritesActivity::class.java)
startActivity(intent)

//fetch("https://api.spoonacular.com/recipes/findByIngredients?ingredients=apples,+flour,+sugar&number=2&apiKey=a84165b11bbe41f0ae6ff525b82eed8e")

fun fetcher(url:String) {
    //var data = @GET("/food/ingredients/search?query=banana&number=2&sort=calories&sortDirection=desc&apiKey=a84165b11bbe41f0ae6ff525b82eed8e")
}

viewHolder.itemView.setOnClickListener {Log.d("view", adapterData[position].id.toString())}


 */