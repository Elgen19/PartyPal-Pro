package com.pepito.partypalpro.recycleradapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pepito.partypalpro.R

class MenuAdapter(private var menuItems: List<String>, private var prices: List<Double>) :
    RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNameTextView: TextView = itemView.findViewById(R.id.menuItemNameTextView)
        val itemPriceTextView: TextView = itemView.findViewById(R.id.menuItemPriceTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menuItem = menuItems[position]
        val price = prices[position]

        holder.itemNameTextView.text = menuItem
        holder.itemPriceTextView.text = "$" + String.format("%.2f", price)
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }


    fun updateMenu(menuItems: List<String>, prices: List<Double>) {
        this.menuItems = menuItems
        this.prices = prices
        notifyDataSetChanged()
    }

    fun getTotalPrice(): Double {
        var totalPrice = 0.0
        for (price in prices) {
            totalPrice += price
        }
        // Format totalPrice to have only two decimal places
        val formattedTotalPrice = String.format("%.2f", totalPrice)
        // Parse the formatted string back to a double
        return formattedTotalPrice.toDouble()
    }

}

