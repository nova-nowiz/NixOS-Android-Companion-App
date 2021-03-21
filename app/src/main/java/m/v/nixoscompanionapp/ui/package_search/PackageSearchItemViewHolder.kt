package m.v.nixoscompanionapp.ui.package_search

import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import m.v.nixoscompanionapp.R

class PackageSearchItemViewHolder(cardView: CardView) : RecyclerView.ViewHolder(cardView) {
    val packageTitle : TextView = cardView.findViewById(R.id.packageTitle)
    val packageDescription : TextView = cardView.findViewById(R.id.packageDescription)
    val packageName : TextView = cardView.findViewById(R.id.packageName)
    val packageVersion : TextView = cardView.findViewById(R.id.packageVersion)
}