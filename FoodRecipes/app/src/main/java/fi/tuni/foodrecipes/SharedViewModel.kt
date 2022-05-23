package fi.tuni.foodrecipes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import fi.tuni.foodrecipes.home.Recipe
import java.io.*

/**
 * Used for sharing data between fragments
 */
class SharedViewModel(private val state: SavedStateHandle) : ViewModel() {

    // LiveData variable that can be observer = tells when the data is changed
    val favouriteRecipesLiveData = MutableLiveData<List<Recipe>>()
    // Holds the objects because you cant add or delete a single object from LiveData
    // Only the whole data can be replaced
    var favouriteRecipes = ArrayList<Recipe>()
    var previousFetchedData = mutableListOf<Recipe>()

    fun saveFavourites(filesDir: File) {
        val file = FileOutputStream("$filesDir/favourites.txt")
        val outStream = ObjectOutputStream(file)
        outStream.writeObject(favouriteRecipes)
        outStream.close()
        file.close()
    }

    fun openFavourites(filesDir: File) {
        val file = FileInputStream("$filesDir/favourites.txt")
        val inStream = ObjectInputStream(file)
        val items = inStream.readObject() as ArrayList<Recipe>
        favouriteRecipes = items
    }

    fun savePreviousFetched(filesDir: File) {
        val file = FileOutputStream("$filesDir/previousfetched.txt")
        val outStream = ObjectOutputStream(file)
        outStream.writeObject(previousFetchedData)
        outStream.close()
        file.close()
    }

    fun openPreviousFetched(filesDir: File) {
        val file = FileInputStream("$filesDir/previousfetched.txt")
        val inStream = ObjectInputStream(file)
        val items = inStream.readObject() as MutableList<Recipe>
        previousFetchedData = items
    }


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