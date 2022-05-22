package fi.tuni.foodrecipes.recipe

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import fi.tuni.foodrecipes.R

class RecipeDetailsFragment : Fragment(R.layout.fragment_recipe_details) {

    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}