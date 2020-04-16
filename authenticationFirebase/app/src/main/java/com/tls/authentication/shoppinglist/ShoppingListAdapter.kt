/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.tls.authentication.shoppinglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.model.BasicShoppingListView
import com.tls.authentication.R

class ShoppingListAdapter :
    ListAdapter<BasicShoppingListView, ShoppingListAdapter.BasicShoppingListViewHolder>(
        DIFF_CALLBACK
    ) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BasicShoppingListView>() {

            override fun areItemsTheSame(
                oldItem: BasicShoppingListView,
                newItem: BasicShoppingListView
            ): Boolean = oldItem.shoppingListId == newItem.shoppingListId

            override fun areContentsTheSame(
                oldItem: BasicShoppingListView,
                newItem: BasicShoppingListView
            ): Boolean = oldItem.shoppingListId == newItem.shoppingListId

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasicShoppingListViewHolder {
        return BasicShoppingListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_shopping_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BasicShoppingListViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    class BasicShoppingListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindTo(basicShoppingListView: BasicShoppingListView) {
            val cardView = itemView.findViewById(R.id.itemName) as TextView
            cardView.text = basicShoppingListView.name
        }

    }
}