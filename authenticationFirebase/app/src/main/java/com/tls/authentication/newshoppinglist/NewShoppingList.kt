/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.tls.authentication.newshoppinglist

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.presentation.NewShoppingListViewModel
import com.example.presentation.model.ViewState
import com.example.shared.exception.UserNotLogged
import com.tls.authentication.R
import kotlinx.android.synthetic.main.new_shopping_list.*
import org.koin.android.ext.android.inject


class NewShoppingList : Fragment() {

    private val newShoppingListViewModel: NewShoppingListViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // set fragment to handle onCreateOptionMenu event
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.new_shopping_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_shopping_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_done -> saveShoppingList()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Config view model observer
     */
    private fun configViewModel() {
        newShoppingListViewModel.shoppingListState.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ViewState.Status.SUCCESS -> goToShoppingListScreen()
                ViewState.Status.ERROR -> showErrorMessage(it.error)
            }
        })
    }

    /**
     * Save shopping list
     */
    private fun saveShoppingList() {
        val shoppingListName = newShoppingList.text.toString()
        if (!shoppingListName.isEmpty()) newShoppingListViewModel.saveShoppingListName(
            name = shoppingListName
        ) else Toast.makeText(activity, "Empty shoppping list name", Toast.LENGTH_SHORT).show()
    }

    /**
     * Show error message
     */
    private fun showErrorMessage(error: Throwable?) {
        error?.let {
            if (it is UserNotLogged) {
                // TODO go to not logged screen
                findNavController().popBackStack()
            } else {
                Toast.makeText(activity, "Shopping list not saved. Try again", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    /**
     * Go to shoppingListScreen
     */
    private fun goToShoppingListScreen() {
        findNavController().popBackStack()
    }

}