package m.v.nixoscompanionapp.ui.option_search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import m.v.nixoscompanionapp.R
import org.json.JSONArray
import org.json.JSONObject

class OptionSearchHitsAdapter : RecyclerView.Adapter<OptionSearchItemViewHolder>() {

    var optionSearchHits : JSONArray = JSONArray()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionSearchItemViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.recycler_view_option_search_item,
                parent,
                false
            ) as CardView
        return OptionSearchItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: OptionSearchItemViewHolder, position: Int) {
        holder.optionName.text = getItemName(position)
        holder.optionDescription.text = getItemDescription(position)
    }

    private fun getItemName(position: Int): String {
        val source = getItemSource(position)
        return source.getString("option_name")
    }

    private fun getItemDescription(position: Int): String {
        val source = getItemSource(position)
        return source.getString("option_description")
    }

    private fun getItemSource(position: Int): JSONObject {
        return optionSearchHits.getJSONObject(position).getJSONObject("_source")
    }

    override fun getItemCount(): Int = optionSearchHits.length()
}
