package fi.tuni.foodrecipes.home


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import fi.tuni.foodrecipes.CustomAdapter
import fi.tuni.foodrecipes.R
import fi.tuni.foodrecipes.SharedViewModel
import fi.tuni.foodrecipes.listeners.OnRecipeClickListener
import fi.tuni.foodrecipes.recipe.RecipeDetailsFragment
import org.json.JSONObject
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

class HomeFragment : Fragment(R.layout.fragment_home), OnRecipeClickListener {

    private lateinit var editText: EditText
    private lateinit var fetchButton : Button
    private var myAdapter = CustomAdapter(this)
    lateinit var recyclerView : RecyclerView
    private var favourites = arrayListOf<Recipe>()
    private val model: SharedViewModel by activityViewModels()


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        editText = view.findViewById(R.id.inputField)
        fetchButton = view.findViewById(R.id.fetchbutton)
        recyclerView = view.findViewById(R.id.recipeList)

        recyclerView
            .apply {
                layoutManager = GridLayoutManager(activity, 2)
            }

            thread {
                activity?.runOnUiThread {
                    recyclerView.adapter = myAdapter
                }
            }

        fetchButton.setOnClickListener {
            thread {
                val input = editText.text.toString()
                val data = createObjects("https://api.spoonacular.com/recipes/complexSearch?query=${input}&number=1&apiKey=a84165b11bbe41f0ae6ff525b82eed8e")
                Log.d("data", data.toString())
                activity?.runOnUiThread {
                    myAdapter.setData(data)
                    myAdapter.notifyDataSetChanged()
                }
            }
        }
        Log.d("asd", arguments?.getString("message").toString())
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
//"https://api.spoonacular.com/recipes/complexSearch?query=pasta&maxFat=25&apiKey=a84165b11bbe41f0ae6ff525b82eed8e"
    fun createObjects (url : String) : MutableList<Recipe> {

        //val data = fetch(url)
        val data = myData.toString()

        val mp = ObjectMapper()
        val myObject: RecipeJsonObject = mp.readValue(data, RecipeJsonObject::class.java)
        val recipes: MutableList<Recipe>? = myObject.results
        return recipes as MutableList<Recipe>
    }

    override fun onRecipeButtonClick(recipe: Recipe) {
        favourites.add(recipe)
        model.addFavouriteRecipe(recipe)
    }

    override fun onRecipeClick(recipe: Recipe) {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            addToBackStack(HomeFragment().javaClass.canonicalName)//optional
            replace(R.id.flFragment, RecipeDetailsFragment(recipe.id))
            commit()
        }
    }
}

var myData = JSONObject("""{
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
    }
    ],
    "totalResults": 86
}""")
