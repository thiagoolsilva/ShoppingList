/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tls.firebase.shopping.shoppinglistdetails

import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.model.ShoppingListItemView
import com.tls.firebase.R
import com.tls.firebase.util.toEditable

class ShoppingListItemAdapter constructor(private val itemChangedEvent: (item: ShoppingListItemView) -> Unit) :
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
        holder.bindTo(getItem(position), itemChangedEvent)
    }

    class ShoppingListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * Bind received view
         */
        fun bindTo(
            item: ShoppingListItemView,
            itemChangedEvent: (item: ShoppingListItemView) -> Unit
        ) {
            // bind all view components
            val itemName = itemView.findViewById(R.id.itemName) as EditText
            val checkBox = itemView.findViewById(R.id.itemCheckState) as CheckBox

            // set text description from repository
            itemName.text = item.description.toEditable()
            itemName.setOnEditorActionListener { v, actionId, event ->
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                    ((event.keyCode == KeyEvent.KEYCODE_ENTER) &&
                            (event.action == KeyEvent.ACTION_DOWN))
                ) {
                    handleItemChangeEvent(checkBox, itemName, item, itemChangedEvent)
                }
                return@setOnEditorActionListener false
            }

            // set checkbox check from repository
            checkBox.isChecked = item.check
            checkBox.setOnClickListener {
                handleItemChangeEvent(checkBox, itemName, item, itemChangedEvent)
            }
        }

        /**
         * Handle item events
         */
        private fun handleItemChangeEvent(
            checkBox: CheckBox,
            editText: EditText,
            item: ShoppingListItemView,
            itemChangedEvent: (item: ShoppingListItemView) -> Unit
        ) {
            // change data from checked event
            item.check = checkBox.isChecked

            item.description = editText.text.toString()
            editText.clearFocus()

            itemChangedEvent(item)
        }
    }
}
