package fi.tuni.foodrecipes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fi.tuni.foodrecipes.home.Recipe

/**
 * Used for sharing data between fragments
 */
class SharedViewModel : ViewModel() {

    // LiveData variable that can be observer = tells when the data is changed
    val favouriteRecipesLiveData = MutableLiveData<List<Recipe>>()
    // Holds the objects because you cant add or delete a single object from LiveData
    // Only the whole data can be replaced
    private val favouriteRecipes = ArrayList<Recipe>()

    // Add a new recipe to favourites, replace the LiveData
    fun addFavouriteRecipe(recipe: Recipe) {
        favouriteRecipes.add(recipe)
        favouriteRecipesLiveData.value = favouriteRecipes
    }

    // Delete a recipe from favourites, replace the livedata
    fun removeFavouriteRecipe(recipe: Recipe) {
        favouriteRecipes.remove(recipe)
        favouriteRecipesLiveData.value = favouriteRecipes
    }

}