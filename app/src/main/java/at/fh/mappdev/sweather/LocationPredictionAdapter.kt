package at.fh.mappdev.sweather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class LocationPredictionAdapter(private val clickListener: (String) -> Unit) : RecyclerView.Adapter<PredictionViewHolder>() {

    private var predictionList = listOf<LocationActivity.Prediction>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val predictionItemView = inflater.inflate(R.layout.item_location_prediction, parent, false)
        return PredictionViewHolder(predictionItemView, clickListener)
    }

    override fun getItemCount(): Int {
        //return list size
        return predictionList.size
    }

    override fun onBindViewHolder(holder: PredictionViewHolder, position: Int) {
        val prediction = predictionList[position]
        holder.bindItem(prediction)
        holder.itemView.setOnClickListener {
            clickListener(prediction.id)
        }
    }

    fun updateList(newList: List<LocationActivity.Prediction>) {
        predictionList = newList
        notifyDataSetChanged()
    }
}

//Class LessonViewHolder with Higher Order Function clickListener
class PredictionViewHolder(itemView: View, private val clickListener: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {
    fun bindItem(prediction: LocationActivity.Prediction) {
        itemView.findViewById<TextView>(R.id.locationName).text = prediction.name
        itemView.findViewById<TextView>(R.id.locationCountry).text = prediction.country
    }
}