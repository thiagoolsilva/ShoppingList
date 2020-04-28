/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.tls.firebase.shopping.shoppinglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.model.ShoppingListView
import com.tls.firebase.R
import timber.log.Timber

class ShoppingListAdapter constructor(private val navController: NavController) :
    ListAdapter<ShoppingListView, ShoppingListAdapter.BasicShoppingListViewHolder>(
        DIFF_CALLBACK
    ) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ShoppingListView>() {

            override fun areItemsTheSame(
                oldItem: ShoppingListView,
                newItem: ShoppingListView
            ): Boolean = oldItem.shoppingListId == newItem.shoppingListId

            override fun areContentsTheSame(
                oldItem: ShoppingListView,
                newItem: ShoppingListView
            ): Boolean = oldItem.shoppingListId == newItem.shoppingListId

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasicShoppingListViewHolder {
        return BasicShoppingListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.shopping_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BasicShoppingListViewHolder, position: Int) {
        holder.bindTo(getItem(position), navController)
    }

    class BasicShoppingListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * Bind received view
         */
        fun bindTo(shoppingListView: ShoppingListView, navController: NavController) {
            val cardView = itemView.findViewById(R.id.itemName) as TextView
            cardView.text = shoppingListView.name

            itemView.setOnClickListener {
                val selectedShoppingId = shoppingListView.shoppingListId
                val selectedShoppingName = shoppingListView.name
                Timber.d(
                    "Selected item id %s with name %s",
                    selectedShoppingId,
                    selectedShoppingName
                )

                val direction = ShoppingListDirections.actionShoppingListToShoppingListDetails(
                    selectedShoppingId,
                    selectedShoppingName
                )
                navController.navigate(direction)
            }
        }
    }
}
