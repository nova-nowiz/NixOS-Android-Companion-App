package m.v.nixoscompanionapp.ui.package_search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import m.v.nixoscompanionapp.R
import org.json.JSONArray
import org.json.JSONObject

class PackageSearchHitsAdapter : RecyclerView.Adapter<PackageSearchItemViewHolder>() {

    var packageSearchHits : JSONArray = JSONArray()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageSearchItemViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.recycler_view_package_search_item,
                parent,
                false
            ) as CardView
        return PackageSearchItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: PackageSearchItemViewHolder, position: Int) {
        holder.packageTitle.text = getItemTitle(position)
        holder.packageDescription.text = getItemDescription(position)
        holder.packageName.text = getItemName(position)
        holder.packageVersion.text = getItemVersion(position)
    }

    private fun getItemTitle(position: Int): String {
        val source = getItemSource(position)
        return source.getString("package_attr_name")
    }

    private fun getItemDescription(position: Int): String {
        val source = getItemSource(position)
        return source.getString("package_description")
    }

    private fun getItemName(position: Int): String {
        val source = getItemSource(position)
        return source.getString("package_pname")
    }

    private fun getItemVersion(position: Int): String {
        val source = getItemSource(position)
        return source.getString("package_pversion")
    }

    private fun getItemSource(position: Int): JSONObject {
        return packageSearchHits.getJSONObject(position).getJSONObject("_source")
    }

    override fun getItemCount(): Int = packageSearchHits.length()
}