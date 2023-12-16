package com.pepito.partypalpro.recycleradapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pepito.partypalpro.GuestDetailActivity
import com.pepito.partypalpro.R
import com.pepito.partypalpro.databinding.ItemGuestBinding
import com.pepito.partypalpro.storage.Guest

class GuestAdapter(private val context: Context, private var guestList: List<Guest>) :
    RecyclerView.Adapter<GuestAdapter.GuestViewHolder>() {

    class GuestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemGuestBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_guest, parent, false)
        return GuestViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) {
        val currentGuest = guestList[position]

        holder.binding.textViewName.text = currentGuest.name
        holder.binding.textViewRSVP.text = currentGuest.rsvpStatus

        // Set onClickListener to handle item click
        holder.itemView.setOnClickListener {
            // Start GuestDetailActivity with the selected guest's ID
            val intent = Intent(context, GuestDetailActivity::class.java)
            intent.putExtra("guestId", currentGuest.id) // Pass the guest's ID to GuestDetailActivity
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = guestList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<Guest>) {
        guestList = newList
        notifyDataSetChanged()
    }
}

