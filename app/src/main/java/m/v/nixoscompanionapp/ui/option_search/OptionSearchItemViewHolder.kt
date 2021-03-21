package m.v.nixoscompanionapp.ui.option_search

import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import m.v.nixoscompanionapp.R

class OptionSearchItemViewHolder(cardView: CardView) : RecyclerView.ViewHolder(cardView) {
    val optionName : TextView = cardView.findViewById(R.id.optionName)
    val optionDescription : TextView = cardView.findViewById(R.id.optionDescription)
}