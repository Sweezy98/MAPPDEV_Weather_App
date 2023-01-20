package at.fh.mappdev.sweather

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavouriteAdapter(private val clickListener: (UserFavouritesQuery.UserFavourite?) -> Unit, private val clickListenerRemove: (UserFavouritesQuery.UserFavourite?) -> Unit) : RecyclerView.Adapter<FavouriteViewHolder>() {

    private var favouriteList = listOf<UserFavouritesQuery.UserFavourite?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val favouriteItemView = inflater.inflate(R.layout.item_location_favourite, parent, false)
        return FavouriteViewHolder(favouriteItemView, clickListener, clickListenerRemove)
    }

    override fun getItemCount(): Int {
        //return list size
        return favouriteList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val favourite = favouriteList[position]
        holder.bindItem(favourite)
        holder.itemView.setOnClickListener {
            clickListener(favourite)
        }
    }

    fun updateList(newList: List<UserFavouritesQuery.UserFavourite?>) {
        favouriteList = newList
        notifyDataSetChanged()
    }

    fun isFavourite(name: String, lat: Double, lon: Double): String? {
        for (favourite in favouriteList) {
            if (favourite?.name == name && favourite.lat == lat && favourite.lon == lon) {
                return favourite._id
            }
        }
        return null
    }

    fun removeFavourite(id: String) {
        favouriteList = favouriteList.filter { it?._id != id }
        notifyDataSetChanged()
    }
}

class FavouriteViewHolder(itemView: View, private val clickListener: (UserFavouritesQuery.UserFavourite?) -> Unit, private val clickListenerRemove: (UserFavouritesQuery.UserFavourite?) -> Unit) : RecyclerView.ViewHolder(itemView) {
    fun bindItem(favourite: UserFavouritesQuery.UserFavourite?) {
        itemView.findViewById<TextView>(R.id.favouriteName).text = favourite!!.name
        itemView.findViewById<ImageButton>(R.id.defavoriseBtn).setOnClickListener {
            clickListenerRemove(favourite)
        }
    }
}