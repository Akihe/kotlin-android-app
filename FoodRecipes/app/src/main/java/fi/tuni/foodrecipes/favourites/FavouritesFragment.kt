package fi.tuni.foodrecipes.favourites

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import fi.tuni.foodrecipes.R

class FavouritesFragment:Fragment(R.layout.fragment_favourites) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println(arguments?.getIntArray("favourites"))
    }
}