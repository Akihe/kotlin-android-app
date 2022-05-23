package fi.tuni.foodrecipes.listeners

import fi.tuni.foodrecipes.home.Recipe

interface FavouriteListListener {
    fun deleteFavourite(recipe: Recipe)
}