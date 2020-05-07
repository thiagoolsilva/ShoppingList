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

package com.tls.firebase.shopping.shoppinglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
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
import com.tls.firebase.R
import com.tls.firebase.util.disableLoading
import com.tls.firebase.util.hideKeyboard
import com.tls.firebase.util.navigateWithAnimation
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
        error?.let {
            when (it) {
                is UserNotLogged -> navigateWithAnimation(R.id.LoginFragment) // findNavController().navigate(R.id.signInFragment)
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
