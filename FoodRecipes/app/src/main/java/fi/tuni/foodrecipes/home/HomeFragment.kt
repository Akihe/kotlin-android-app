package fi.tuni.foodrecipes.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
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
import fi.tuni.foodrecipes.adapters.CustomAdapter
import fi.tuni.foodrecipes.R
import fi.tuni.foodrecipes.SharedViewModel
import fi.tuni.foodrecipes.hideKeyboard
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

val r1 = Recipe(716429,"Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs","https://spoonacular.com/recipeImages/716429-312x231.jpg")
val r2 = Recipe(715538,"What to make for dinner tonight?? Bruschetta Style Pork & Pasta","https://spoonacular.com/recipeImages/715538-312x231.jpg")
val r3 = Recipe(638125,"Chicken In A Pot","https://spoonacular.com/recipeImages/638125-312x231.jpg")
val r4 = Recipe(631882,"5-Minute Rocky Road Fudge","https://spoonacular.com/recipeImages/631882-556x370.jpg")
val r5 = Recipe(650855,"Mangoes with Rum and Ice Cream","https://spoonacular.com/recipeImages/650855-556x370.jpg")
val r6 = Recipe(654835,"Pasta e Fagioli (Pasta and Beans)","https://spoonacular.com/recipeImages/654835-312x231.jpg")
var dummyList = mutableListOf(r1, r2, r3, r4, r5, r6)

/**
 * Main view, displays the search bar and recipe list.
 */
class HomeFragment : Fragment(R.layout.fragment_home), OnRecipeClickListener {

    private lateinit var editText: EditText
    private lateinit var fetchButton : Button

    /**
     * Handles putting data into recyclerView, gets listener as parameter to allow calling functions implemented here in HomeFragment
     */
    private var myAdapter = CustomAdapter(this)
    lateinit var recyclerView : RecyclerView

    /**
     * ViewModel used to share data between fragments (favourites list)
     */
    private val model: SharedViewModel by activityViewModels()


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Get the current view
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        editText = view.findViewById(R.id.inputField)
        fetchButton = view.findViewById(R.id.fetchbutton)
        recyclerView = view.findViewById(R.id.recipeList)

        recyclerView.apply {
                layoutManager = GridLayoutManager(activity, 2)
        }
        myAdapter.setData(dummyList) //add dummydata to the list on open just so the view isnt empty.
        recyclerView.adapter = myAdapter

        //Detects when the user presses enter on keyboard, calls the Search buttons onclick action
        editText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                fetchButton.callOnClick()
                return@OnKeyListener true
            }
            false
        })

        fetchButton.setOnClickListener {
            thread {
                val input = editText.text.toString()
                //val data = createObjects("https://api.spoonacular.com/recipes/complexSearch?query=${input}&number=1&apiKey=a84165b11bbe41f0ae6ff525b82eed8e")
                //activity?.runOnUiThread {
                //    myAdapter.setData(data)
                //    myAdapter.notifyDataSetChanged()
                //}
            }
            hideKeyboard()
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

        val data = fetch(url)

        val mp = ObjectMapper()
        val myObject: RecipeJsonObject = mp.readValue(data, RecipeJsonObject::class.java)
        val recipes: MutableList<Recipe>? = myObject.results
        return recipes as MutableList<Recipe>
    }

    override fun onRecipeButtonClick(recipe: Recipe) {
        model.addFavouriteRecipe(recipe)
    }

    override fun onRecipeClick(recipe: Recipe) {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            addToBackStack(HomeFragment().javaClass.canonicalName) //makes it possible to return to previous view by pressing back button
            replace(R.id.flFragment, RecipeDetailsFragment(recipe.id))
            commit()
        }
    }
}
