/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.tls.authentication.shoppinglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.presentation.model.BasicShoppingListView
import com.tls.authentication.R
import kotlinx.android.synthetic.main.shopping_list.*

class ShoppingList : Fragment() {

    private lateinit var adapter: ShoppingListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shopping_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ShoppingListAdapter()

        shoppingList.layoutManager = LinearLayoutManager(activity)
        shoppingList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        val items = listOf(BasicShoppingListView("lopes", 1), BasicShoppingListView("lopes2", 2))
        adapter.submitList(items)
    }

}