package fi.tuni.foodrecipes.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fi.tuni.foodrecipes.R
import fi.tuni.foodrecipes.home.Recipe
import fi.tuni.foodrecipes.home.RecipeJsonObject
import fi.tuni.foodrecipes.home.data
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

@JsonIgnoreProperties(ignoreUnknown = true)
data class RecipeDetails(val name: String, val amount: AmountDetails) {

    constructor() : this(name = "", AmountDetails()) {
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class AmountDetails(val metric: MetricDetails) {

    constructor() : this(metric = MetricDetails()) {

    }
}
@JsonIgnoreProperties(ignoreUnknown = true)
data class MetricDetails(val value: Int, val unit: String) {

    constructor() : this(value = 0, unit = "") {

    }
}



@JsonIgnoreProperties(ignoreUnknown = true)
data class RecipeDetailsJSON(var ingredients: MutableList<RecipeDetails>? = null) {

}

class RecipeDetailsFragment : Fragment(R.layout.fragment_recipe_details) {

    lateinit var textView: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thread {
            // val data = fetch("https://api.spoonacular.com/recipes/716429/ingredientWidget.json?apiKey=a84165b11bbe41f0ae6ff525b82eed8e")
            // println(data)
        }
        var view = inflater.inflate(R.layout.fragment_recipe_details, container, false)

        textView = view.findViewById(R.id.ingredients)
        textView.text = "tekstiä"
        var ingreds =  createObjects()
        println(ingreds)
        var resultString = ingreds.joinToString(separator = "\n") { "${it.name}: ${it.amount.metric.value} ${it.amount.metric.unit}" }
        println(resultString)
        textView.text = resultString

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

    fun createObjects (): MutableList<RecipeDetails>{

        //var data = fetch("https://api.spoonacular.com/recipes/complexSearch?query=pasta&maxFat=25&apiKey=a84165b11bbe41f0ae6ff525b82eed8e")

        var data = jsondata.toString()
        val mp = ObjectMapper()
        val myObject: RecipeDetailsJSON = mp.readValue(data, RecipeDetailsJSON::class.java)
        val recipes: MutableList<RecipeDetails>? = myObject.ingredients
        return recipes as MutableList<RecipeDetails>
    }

}

val jsondata = JSONObject("""{
   "ingredients":[
      {
         "name":"butter",
         "image":"butter-sliced.jpg",
         "amount":{
            "metric":{
               "value":1.0,
               "unit":"Tbsp"
            },
            "us":{
               "value":1.0,
               "unit":"Tbsp"
            }
         }
      },
      {
         "name":"frozen cauliflower florets",
         "image":"cauliflower.jpg",
         "amount":{
            "metric":{
               "value":200.0,
               "unit":"g"
            },
            "us":{
               "value":2.0,
               "unit":"cups"
            }
         }
      },
      {
         "name":"cheese",
         "image":"cheddar-cheese.png",
         "amount":{
            "metric":{
               "value":2.0,
               "unit":"Tbsps"
            },
            "us":{
               "value":2.0,
               "unit":"Tbsps"
            }
         }
      },
      {
         "name":"extra virgin olive oil",
         "image":"olive-oil.jpg",
         "amount":{
            "metric":{
               "value":1.0,
               "unit":"Tbsp"
            },
            "us":{
               "value":1.0,
               "unit":"Tbsp"
            }
         }
      },
      {
         "name":"garlic",
         "image":"garlic.png",
         "amount":{
            "metric":{
               "value":5.0,
               "unit":"cloves"
            },
            "us":{
               "value":5.0,
               "unit":"cloves"
            }
         }
      },
      {
         "name":"pasta",
         "image":"fusilli.jpg",
         "amount":{
            "metric":{
               "value":170.097,
               "unit":"g"
            },
            "us":{
               "value":6.0,
               "unit":"ounces"
            }
         }
      },
      {
         "name":"red pepper flakes",
         "image":"red-pepper-flakes.jpg",
         "amount":{
            "metric":{
               "value":2.0,
               "unit":"pinches"
            },
            "us":{
               "value":2.0,
               "unit":"pinches"
            }
         }
      },
      {
         "name":"salt and pepper",
         "image":"salt-and-pepper.jpg",
         "amount":{
            "metric":{
               "value":2.0,
               "unit":"servings"
            },
            "us":{
               "value":2.0,
               "unit":"servings"
            }
         }
      },
      {
         "name":"green white scallions",
         "image":"spring-onions.jpg",
         "amount":{
            "metric":{
               "value":3.0,
               "unit":""
            },
            "us":{
               "value":3.0,
               "unit":""
            }
         }
      },
      {
         "name":"white wine",
         "image":"white-wine.jpg",
         "amount":{
            "metric":{
               "value":2.0,
               "unit":"Tbsps"
            },
            "us":{
               "value":2.0,
               "unit":"Tbsps"
            }
         }
      },
      {
         "name":"whole wheat bread crumbs",
         "image":"breadcrumbs.jpg",
         "amount":{
            "metric":{
               "value":27.0,
               "unit":"g"
            },
            "us":{
               "value":0.25,
               "unit":"cup"
            }
         }
      }
   ]
}""")