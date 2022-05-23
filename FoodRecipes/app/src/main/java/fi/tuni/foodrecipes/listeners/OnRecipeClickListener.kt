package fi.tuni.foodrecipes.listeners

import fi.tuni.foodrecipes.home.Recipe

interface OnRecipeClickListener {
    fun onRecipeButtonClick(recipe : Recipe)
    fun onRecipeClick(recipe : Recipe)
}
