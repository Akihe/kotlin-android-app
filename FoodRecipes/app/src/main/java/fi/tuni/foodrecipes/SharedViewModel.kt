package fi.tuni.foodrecipes

import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import fi.tuni.foodrecipes.home.Recipe
import java.io.File

class SharedViewModel() : ViewModel() {

    val favouriteRecipesLiveData = MutableLiveData<List<Recipe>>()
    private val favouriteRecipes = ArrayList<Recipe>()

    fun addFavouriteRecipe(recipe: Recipe) {
        favouriteRecipes.add(recipe)
        favouriteRecipesLiveData.value = favouriteRecipes
    }

    fun removeFavouriteRecipe(recipe: Recipe) {
        Log.d("index", favouriteRecipes.indexOf(recipe).toString())
        favouriteRecipes.remove(recipe)
        favouriteRecipesLiveData.value = favouriteRecipes
    }

}