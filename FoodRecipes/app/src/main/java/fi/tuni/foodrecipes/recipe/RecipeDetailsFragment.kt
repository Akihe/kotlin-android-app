package fi.tuni.foodrecipes.recipe

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import fi.tuni.foodrecipes.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

/**
 * Holds the details of the recipe, fetched from the api.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class RecipeDetails(val name: String, val amount: AmountDetails) {
    constructor() : this(name = "", AmountDetails())
}

/**
 * More details, fetched from the api
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class AmountDetails(val metric: MetricDetails) {
    constructor() : this(metric = MetricDetails())
}

/**
 * More details, fetched from the api
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class MetricDetails(val value: Int, val unit: String) {
    constructor() : this(value = 0, unit = "")
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class RecipeDetailsJSON(var ingredients: MutableList<RecipeDetails>? = null) {

}

/**
 * Used for displaying the ingredients of a given recipe.
 */
class RecipeDetailsFragment(id: Int) : Fragment(R.layout.fragment_recipe_details) {

    lateinit var textView: TextView
    // Id of the recipe, given as parameter when the recipe is clicked
    var myRecipeId = id

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_recipe_details, container, false)
        textView = view.findViewById(R.id.ingredients)
        // Enables a possibility to scroll the details textview, in case there is alot of ingredients.
        textView.movementMethod = ScrollingMovementMethod()

        // Fetch the ingredients data using the id
        thread {
            val ingredients = createObjects("https://api.spoonacular.com/recipes/${myRecipeId}/ingredientWidget.json?apiKey=a84165b11bbe41f0ae6ff525b82eed8e")
            // Put all the data in to a String, separated by line breaks.
            val resultString = ingredients.joinToString(separator = "\n") { "${it.name}: ${it.amount.metric.value} ${it.amount.metric.unit}" }
            // Set the fetched data to be displayed in the view
            activity?.runOnUiThread {
                textView.text = resultString
            }
        }
        return view
    }

    /**
     * Fetches JSON from the given url, converts it to a one line string
     */
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

    /**
     * Creates RecipeDetails objects based on the fetched data, puts them in to a list
     */
    fun createObjects (url : String): MutableList<RecipeDetails>{
        val data = fetch(url)
        val mp = ObjectMapper()
        val myObject: RecipeDetailsJSON = mp.readValue(data, RecipeDetailsJSON::class.java)
        val recipes: MutableList<RecipeDetails>? = myObject.ingredients
        return recipes as MutableList<RecipeDetails>
    }

}
