/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.tls.authentication.shoppinglist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.presentation.ShoppingListViewModel
import com.example.presentation.model.ShoppingListView
import com.example.presentation.model.ViewState
import com.example.shared.exception.UserNotLogged
import com.tls.authentication.R
import com.tls.authentication.util.hideKeyboard
import kotlinx.android.synthetic.main.shopping_list.*
import org.koin.android.ext.android.inject

class ShoppingList : Fragment() {

    private val shoppingListViewModel: ShoppingListViewModel by inject()
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

        configRecyclerView()
        configViewModel()
        configureSwipeRefreshLayout()
        configViews()
        hideKeyboard()
    }

    override fun onResume() {
        super.onResume()
        // always fetch new data from repository on onResume event
        fetchShoppingList()
    }

    /**
     * Config views
     */
    private fun configViews() {
        fb.setOnClickListener {
            val action = ShoppingListDirections.actionShoppingListToNewShoppingList()
            findNavController().navigate(action)
        }
    }

    /**
     * Config Swipe refresh layout
     */
    private fun configureSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener {
            fetchShoppingList()
        }
    }

    /**
     * Config viewModel
     */
    private fun configViewModel() {
        shoppingListViewModel.shoppingListState.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ViewState.Status.SUCCESS -> updateList(it.data)
                ViewState.Status.ERROR -> showListErrorMessage(it.error)
            }
        })
    }

    /**
     * Show list error message
     */
    private fun showListErrorMessage(error: Throwable?) {
        if (error is UserNotLogged) {
            // TODO go to not logged screen
            Toast.makeText(activity, "User not logged", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "List not updated. Try Again!", Toast.LENGTH_SHORT).show()
        }
        // hide swipe refresh layout
        swipeRefreshLayout.isRefreshing = false
    }

    /**
     * Update adapter and notify data set
     */
    private fun updateList(data: List<ShoppingListView>?) {
        data?.let {
            adapter.submitList(data)
        }
        // hide swipe refresh layout
        swipeRefreshLayout.isRefreshing = false
    }

    /**
     * config recycler view
     */
    private fun configRecyclerView() {
        adapter = ShoppingListAdapter(findNavController())

        shoppingList.layoutManager = LinearLayoutManager(activity)
        shoppingList.adapter = adapter
    }

    /**
     * Refresh shopping List data
     */
    private fun fetchShoppingList() {
        shoppingListViewModel.fetchShoppingList()
    }

}