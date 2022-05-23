package fi.tuni.foodrecipes.favourites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fi.tuni.foodrecipes.R
import fi.tuni.foodrecipes.SharedViewModel
import fi.tuni.foodrecipes.adapters.FavouritesAdapter
import fi.tuni.foodrecipes.home.Recipe
import fi.tuni.foodrecipes.listeners.FavouriteListListener
import fi.tuni.foodrecipes.recipe.RecipeDetailsFragment

/**
 * Works as the Favourites tab ui-view.
 */
class FavouritesFragment : Fragment(R.layout.fragment_favourites), FavouriteListListener {

    lateinit var recyclerView : RecyclerView
    // Handles the recyclerView data
    private var myAdapter = FavouritesAdapter(this)
    // Used for sharing data between fragments
    private val model: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Find the view this fragment is related to
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)
        // Observe the LiveData variables current data == get the data from the variable every time it is updated
        model.favouriteRecipesLiveData.observe(viewLifecycleOwner, Observer<List<Recipe>> { recipe ->
            myAdapter.setData(recipe)
        })
        if(model.favouriteRecipes.size > 0) {
            myAdapter.setData(model.favouriteRecipes)
        }

        recyclerView = view.findViewById(R.id.favouriteList)
        // Apply a LinearLayoutManager to list all the elements vertically
        recyclerView
            .apply {
                layoutManager = LinearLayoutManager(activity)
            }
        //set the adapter
        recyclerView.adapter = myAdapter
        return view
    }

    /**
     * Delete a recipe from the favourites list, tell the adapter to refresh the view.
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun deleteFavourite(recipe: Recipe) {
        model.removeFavouriteRecipe(recipe)
        myAdapter.notifyDataSetChanged()
    }

    /**
     * Open a new RecipeDetailsFragment, pass the id of the recipe that is clicked.
     * Add current view FavouritesFragment to backtrack to enable using the back button on phone.
     */
    override fun onRecipeClick(recipe: Recipe) {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            //makes it possible to return to previous view by pressing the back button
            addToBackStack(FavouritesFragment().javaClass.canonicalName)
            replace(R.id.flFragment, RecipeDetailsFragment(recipe.id))
            commit()
        }
    }
}