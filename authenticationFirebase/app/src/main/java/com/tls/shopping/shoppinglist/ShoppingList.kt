/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.tls.shopping.shoppinglist

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.presentation.LoginViewModel
import com.example.presentation.ShoppingListViewModel
import com.example.presentation.model.ShoppingListView
import com.example.presentation.model.ViewState
import com.example.shared.exception.UserNotLogged
import com.tls.authentication.R
import com.tls.authentication.util.disableLoading
import com.tls.authentication.util.hideKeyboard
import com.tls.authentication.util.navigateWithAnimation
import kotlinx.android.synthetic.main.shopping_list.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class ShoppingList : Fragment() {

    // To get a refreshed LoginViewModel instance I don't used sharedViewModel
    private val loginViewModel: LoginViewModel by inject()
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
        configMenu()
    }

    override fun onResume() {
        super.onResume()
        // always fetch new data from repository on onResume event
        fetchShoppingList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.shopping_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> disconnectUser()
            R.id.profile -> goToProfileScreen()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Config fragment to handle menu events
     */
    private fun configMenu() {
        setHasOptionsMenu(true)
    }

    /**
     * Config views
     */
    private fun configViews() {
        fb.setOnClickListener {
            val action = ShoppingListDirections.actionShoppingListToNewShoppingList()
            findNavController().navigate(action)
        }
        hideKeyboard()
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
        shoppingListViewModel.shoppingListState.observe(this, Observer {
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
        error?.let {
            when (it) {
                is UserNotLogged ->  navigateWithAnimation(R.id.LoginFragment) //findNavController().navigate(R.id.signInFragment)
                else -> Toast.makeText(activity, "List not updated. Try Again!", Toast.LENGTH_SHORT).show()
            }
        }
        // hide swipe refresh layout
        swipeRefreshLayout.disableLoading()
    }

    /**
     * Update adapter and notify data set
     */
    private fun updateList(data: List<ShoppingListView>?) {
        data?.let {
            adapter.submitList(data)
        }
        // hide swipe refresh layout
        swipeRefreshLayout.disableLoading()
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
    private fun fetchShoppingList() = shoppingListViewModel.fetchShoppingList()

    /**
     * Disconnect current user
     */
    private fun disconnectUser() {
        Timber.d("fire disconnect user event")
        loginViewModel.disconnect()
        findNavController().popBackStack()
    }

    /**
     * Go to profile screen
     */
    private fun goToProfileScreen() {
        Timber.d("fired profile screen event")
        val direction = ShoppingListDirections.actionShoppingListToProfileFragment()
        findNavController().navigate(direction)
    }

}