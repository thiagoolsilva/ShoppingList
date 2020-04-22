/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.tls.authentication.shoppinglistdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.model.ShoppingListItemView
import com.tls.authentication.R

class ShoppingListItemAdapter :
    ListAdapter<ShoppingListItemView, ShoppingListItemAdapter.ShoppingListItemViewHolder>(
        DIFF_CALLBACK
    ) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ShoppingListItemView>() {

            override fun areItemsTheSame(
                oldItem: ShoppingListItemView,
                newItem: ShoppingListItemView
            ): Boolean = oldItem.uuid == newItem.uuid

            override fun areContentsTheSame(
                oldItem: ShoppingListItemView,
                newItem: ShoppingListItemView
            ): Boolean = oldItem.uuid == newItem.uuid

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListItemViewHolder {
        return ShoppingListItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.shopping_details_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ShoppingListItemViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    class ShoppingListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindTo(item: ShoppingListItemView) {

            val itemName = itemView.findViewById(R.id.itemName) as TextView
            val checkBox = itemView.findViewById(R.id.itemCheckState) as CheckBox

            itemName.text = item.description
            checkBox.isChecked = item.check
        }
    }
}