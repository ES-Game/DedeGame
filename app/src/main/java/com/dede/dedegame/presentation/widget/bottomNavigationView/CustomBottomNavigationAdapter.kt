package com.dede.dedegame.presentation.widget.bottomNavigationView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R

class CustomBottomNavigationAdapter(
    private val context: Context,
    private val items: List<CustomBottomNavigationItem>
) : RecyclerView.Adapter<CustomBottomNavigationAdapter.ViewHolder>() {

    private var previousPosition = RecyclerView.NO_POSITION
    private var selectedPosition = 0
    private var onNavigationItemSelectedListener : OnEventListener ?= null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.navigation_icon)
        val label: TextView = itemView.findViewById(R.id.navigation_label)
        val cvOutSide: CardView = itemView.findViewById(R.id.cvOutSide)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_bottom_navigation_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.icon.setImageResource(item.iconResId)
        holder.label.text = item.label
        if (position == selectedPosition) {
            holder.cvOutSide.setCardBackgroundColor(ContextCompat.getColor(context, R.color.orange_300))
        } else {
            holder.cvOutSide.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_item_bottom_bar_inside))
        }
        holder.itemView.setOnClickListener {
            if (position != selectedPosition){
                previousPosition = selectedPosition
                selectedPosition = position
                if (previousPosition == RecyclerView.NO_POSITION){
                    notifyItemChanged(selectedPosition, 1)
                } else{
                    notifyItemChanged(previousPosition, 1)
                    notifyItemChanged(selectedPosition, 1)
                }
                if (onNavigationItemSelectedListener != null){
                    onNavigationItemSelectedListener?.OnNavigationItemSelectedListener(position)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun setOnItemSelectedListener(listener: OnEventListener){
        this.onNavigationItemSelectedListener = listener
    }

    interface OnEventListener {
        fun OnNavigationItemSelectedListener(pos: Int)
    }

}