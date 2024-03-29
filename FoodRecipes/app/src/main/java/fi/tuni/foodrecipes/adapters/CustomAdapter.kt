package fi.tuni.foodrecipes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fi.tuni.foodrecipes.R
import fi.tuni.foodrecipes.home.Recipe
import fi.tuni.foodrecipes.listeners.OnRecipeClickListener

/**
 * Used for updating recyclerViews elements on Home fragment
 */
class CustomAdapter(private val listener: OnRecipeClickListener) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * Holds all the Recipes displayed on recyclerView
     */
    private val adapterData = mutableListOf<Recipe>()

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.recipeTitle)
        val imageView : ImageView = view.findViewById(R.id.imageView)
        val button : ImageButton = view.findViewById(R.id.favButton)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recipe, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        // Add a clickListener to each element
        viewHolder.itemView.setOnClickListener {
            listener.onRecipeClick(adapterData[position])
        }

        viewHolder.button.setOnClickListener {
            listener.onRecipeButtonClick(adapterData[position])
        }
        // Assign the title
        viewHolder.textView.text = adapterData[position].title
        // Load the image from url in to the imageView
        Picasso.get().load(adapterData[position].image).into(viewHolder.imageView)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = adapterData.size

    //Set the adapterData, called from HomeFragment
    fun setData(data: List<Recipe>) {
        adapterData.apply {
            clear()
            addAll(data)
        }
    }
}