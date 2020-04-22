/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.tls.authentication.shoppinglistdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.presentation.ShoppingListItemViewModel
import com.example.presentation.model.ShoppingListItemView
import com.example.presentation.model.ViewState
import com.tls.authentication.R
import kotlinx.android.synthetic.main.shopping_list_details.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class ShoppingListDetails : Fragment() {

    private val shoppingListItemViewModel: ShoppingListItemViewModel by inject()
    private lateinit var adapter: ShoppingListItemAdapter
    private lateinit var shoppingListId: String

    companion object {
        const val ARGUMENT_SHOPPING_ID = "shoppingId"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shopping_list_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configReceivedShoppingId()
        configViewModel()
        configRecyclerView()
    }

    private fun configRecyclerView() {
        adapter = ShoppingListItemAdapter()

        shoppingListItems.layoutManager = LinearLayoutManager(activity)
        shoppingListItems.adapter = adapter
    }

    private fun configReceivedShoppingId() {
        shoppingListId = arguments?.getString(ARGUMENT_SHOPPING_ID) ?: ""
        Timber.d("Received item %s", shoppingListId)
    }

    private fun configViewModel() {
        shoppingListItemViewModel.saveShoppingListItemState.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ViewState.Status.SUCCESS -> fetchNewShoppingItems()
                ViewState.Status.ERROR -> showSaveErrorMessage()
            }
        })

        shoppingListItemViewModel.shoppingListItemsState.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ViewState.Status.SUCCESS -> reloadAdapter(it.data)
                ViewState.Status.ERROR -> showGetItemsErrorMessage()
            }
        })
    }

    private fun showSaveErrorMessage() {
        Toast.makeText(activity, "Try again.", Toast.LENGTH_SHORT).show()
    }

    private fun showGetItemsErrorMessage() {
        Toast.makeText(activity, "Error. Try Again.", Toast.LENGTH_SHORT).show()
    }

    private fun fetchNewShoppingItems() {
        shoppingListItemViewModel.getShoppingItems(shoppingListId = shoppingListId)
    }

    private fun reloadAdapter(data: List<ShoppingListItemView>?) {
        adapter.submitList(data)
    }

}