package fi.tuni.foodrecipes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


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



class MainActivity : AppCompatActivity() {

    lateinit var fetchButton : Button
    lateinit var bottomNav : BottomNavigationView
    var myAdapter = CustomAdapter()
    lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchButton = findViewById(R.id.fetchbutton)
        bottomNav = findViewById(R.id.bottom_navigation)

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


        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.page_1 -> {
                }
                R.id.page_2 -> {
                }
            }
            return@setOnItemSelectedListener true
        }
        fetchButton.setOnClickListener {
            thread {
                createObjects()
            }
        }
    }
    // "https://api.spoonacular.com/recipes/findByIngredients?ingredients=apples,+flour,+sugar&number=2&apiKey=a84165b11bbe41f0ae6ff525b82eed8e"
    // "https://api.spoonacular.com/recipes/1234/information/?apiKey=a84165b11bbe41f0ae6ff525b82eed8e"
    // "https://api.spoonacular.com/recipes/complexSearch?query=pasta&maxFat=25&number=2&apiKey=a84165b11bbe41f0ae6ff525b82eed8e"

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
}