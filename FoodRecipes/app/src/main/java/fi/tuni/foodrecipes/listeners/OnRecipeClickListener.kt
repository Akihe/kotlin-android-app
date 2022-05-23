package fi.tuni.foodrecipes.listeners

import fi.tuni.foodrecipes.home.Recipe

/**
 * Used for handling the elements clicked in HomeFragment view
 */
interface OnRecipeClickListener {
    // Adds to favourites
    fun onRecipeButtonClick(recipe : Recipe)
    // Opens the details fragment
    fun onRecipeClick(recipe : Recipe)
}
