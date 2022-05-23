package fi.tuni.foodrecipes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fi.tuni.foodrecipes.R
import fi.tuni.foodrecipes.home.Recipe
import fi.tuni.foodrecipes.listeners.FavouriteListListener

/**
 * Used for updating recyclerViews elements on Favourites fragment
 */
class FavouritesAdapter(private val listener: FavouriteListListener) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    //Holds all the favourite recipes
    private val adapterData = mutableListOf<Recipe>()

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recipe, viewGroup, false)

        return CustomAdapter.ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: CustomAdapter.ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        // Add a clickListener to each element
        viewHolder.itemView.setOnClickListener {
            listener.onRecipeClick(adapterData[position])
        }

        viewHolder.button.setOnClickListener {
            listener.deleteFavourite(adapterData[position])
        }

        viewHolder.textView.text = adapterData[position].title
        // Load image from url and set to imageView
        Picasso.get().load(adapterData[position].image).into(viewHolder.imageView)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = adapterData.size

    // Set adapterData, called from FavouritesFragment
    fun setData(data: List<Recipe>) {
        adapterData.apply {
            clear()
            addAll(data)
        }
    }
}