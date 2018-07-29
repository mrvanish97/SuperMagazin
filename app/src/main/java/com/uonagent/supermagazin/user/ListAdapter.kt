package com.uonagent.supermagazin.user

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.uonagent.supermagazin.R
import com.uonagent.supermagazin.utils.CurrencyFormatter
import kotlinx.android.synthetic.main.list_item.view.*


class ListAdapter(private val list: List<ListItemModel>) :
        RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.title
        holder.cost.text = CurrencyFormatter.doubleToString(item.cost)
        Picasso.get()
                .load(item.photo)
                .resizeDimen(holder.dimen, holder.dimen)
                .into(holder.photo)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photo: ImageView = itemView.item_photo
        val title: TextView = itemView.item_title
        val cost: TextView = itemView.item_cost
        val dimen: Int = R.dimen.item_photo_size
    }
}