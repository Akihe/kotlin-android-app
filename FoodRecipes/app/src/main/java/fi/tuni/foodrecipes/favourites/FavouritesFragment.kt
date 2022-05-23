package fi.tuni.foodrecipes.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fi.tuni.foodrecipes.adapters.FavouritesAdapter
import fi.tuni.foodrecipes.R
import fi.tuni.foodrecipes.SharedViewModel
import fi.tuni.foodrecipes.home.Recipe
import fi.tuni.foodrecipes.listeners.FavouriteListListener
import kotlin.concurrent.thread

class FavouritesFragment : Fragment(R.layout.fragment_favourites), FavouriteListListener {

    lateinit var recyclerView : RecyclerView
    private var myAdapter = FavouritesAdapter(this)
    private val model: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_favourites, container, false)
        model.favouriteRecipesLiveData.observe(viewLifecycleOwner, Observer<List<Recipe>> { recipe ->
            myAdapter.setData(recipe)
        })

        recyclerView = view.findViewById(R.id.favouriteList)
        recyclerView
            .apply {
                layoutManager = LinearLayoutManager(activity)
            }
        thread {
            activity?.runOnUiThread {
                recyclerView.adapter = myAdapter
            }
        }

        return view
    }

    override fun deleteFavourite(recipe: Recipe) {
        model.removeFavouriteRecipe(recipe)
        myAdapter.notifyDataSetChanged()
    }
}