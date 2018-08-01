package com.uonagent.supermagazin.utils.recycleviewadapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.uonagent.supermagazin.R
import com.uonagent.supermagazin.user.UserRepository
import com.uonagent.supermagazin.utils.CurrencyFormatter
import com.uonagent.supermagazin.utils.models.ItemModel
import kotlinx.android.synthetic.main.list_item.view.*


class ItemListAdapter(private val list: List<ItemModel>, private val context: Context?) :
        RecyclerView.Adapter<ItemListAdapter.ListViewHolder>() {

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
        UserRepository.loadItemPhotoFromStorage(item.photo, holder.photo, context)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photo: ImageView = itemView.item_photo
        val title: TextView = itemView.item_title
        val cost: TextView = itemView.item_cost
    }


}