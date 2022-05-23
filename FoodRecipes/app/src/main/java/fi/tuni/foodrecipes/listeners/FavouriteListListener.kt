package fi.tuni.foodrecipes.listeners

import fi.tuni.foodrecipes.home.Recipe

/**
 * Used for handling the Favourites list in FavouritesFragment
 */
interface FavouriteListListener {
    // Delete a favourite
    fun deleteFavourite(recipe: Recipe)
    // Open details window of favourite recipe
    fun onRecipeClick(recipe: Recipe)
}