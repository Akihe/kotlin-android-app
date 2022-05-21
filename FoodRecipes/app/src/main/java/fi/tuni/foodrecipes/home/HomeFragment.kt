package fi.tuni.foodrecipes.home


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import fi.tuni.foodrecipes.CustomAdapter
import fi.tuni.foodrecipes.R
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
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

class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var fetchButton : Button
    var myAdapter = CustomAdapter()
    lateinit var recyclerView : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false
        )

        super.onCreate(savedInstanceState)
        fetchButton = view.findViewById(R.id.fetchbutton)
        recyclerView = view.findViewById(R.id.recipeList)

        recyclerView
            .apply {
                layoutManager = LinearLayoutManager(activity)
            }

            thread {
                var data = createObjects()
                myAdapter.setData(data)
                activity?.runOnUiThread {
                    recyclerView.adapter = myAdapter
                }
            }

        fetchButton.setOnClickListener {
            thread {
                createObjects()
            }
        }
        return view
    }

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
        return result
    }

    fun createObjects () : MutableList<Recipe> {

        //var data = fetch("https://api.spoonacular.com/recipes/complexSearch?query=pasta&maxFat=25&apiKey=a84165b11bbe41f0ae6ff525b82eed8e")

        var data = data.toString()
        val mp = ObjectMapper()
        val myObject: RecipeJsonObject = mp.readValue(data, RecipeJsonObject::class.java)
        val recipes: MutableList<Recipe>? = myObject.results
        return recipes as MutableList<Recipe>
    }

}

var data = JSONObject("""{
    "offset": 0,
    "number": 2,
    "results": [
    {
        "id": 716429,
        "title": "Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs",
        "calories": 584,
        "carbs": "84g",
        "fat": "20g",
        "image": "https://spoonacular.com/recipeImages/716429-312x231.jpg",
        "imageType": "jpg",
        "protein": "19g"
    },
    {
        "id": 715538,
        "title": "What to make for dinner tonight?? Bruschetta Style Pork & Pasta",
        "calories": 521,
        "carbs": "69g",
        "fat": "10g",
        "image": "https://spoonacular.com/recipeImages/715538-312x231.jpg",
        "imageType": "jpg",
        "protein": "35g"
    }, 
    {
        "id": 716429,
        "title": "Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs",
        "calories": 584,
        "carbs": "84g",
        "fat": "20g",
        "image": "https://spoonacular.com/recipeImages/716429-312x231.jpg",
        "imageType": "jpg",
        "protein": "19g"
    },
    {
        "id": 715538,
        "title": "What to make for dinner tonight?? Bruschetta Style Pork & Pasta",
        "calories": 521,
        "carbs": "69g",
        "fat": "10g",
        "image": "https://spoonacular.com/recipeImages/715538-312x231.jpg",
        "imageType": "jpg",
        "protein": "35g"
    },
    {
        "id": 716429,
        "title": "Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs",
        "calories": 584,
        "carbs": "84g",
        "fat": "20g",
        "image": "https://spoonacular.com/recipeImages/716429-312x231.jpg",
        "imageType": "jpg",
        "protein": "19g"
    },
    {
        "id": 715538,
        "title": "What to make for dinner tonight?? Bruschetta Style Pork & Pasta",
        "calories": 521,
        "carbs": "69g",
        "fat": "10g",
        "image": "https://spoonacular.com/recipeImages/715538-312x231.jpg",
        "imageType": "jpg",
        "protein": "35g"
    }
    ],
    "totalResults": 86
}""")