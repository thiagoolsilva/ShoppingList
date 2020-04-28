/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.tls.firebase.shopping.newshoppinglist

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.presentation.NewShoppingListViewModel
import com.example.presentation.model.ViewState
import com.example.shared.exception.UserNotLogged
import com.google.android.material.snackbar.Snackbar
import com.tls.firebase.R
import com.tls.firebase.util.hideKeyboard
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
        configViews()
    }

    /**
     * Config view event
     */
    private fun configViews() {
        newShoppingList.setOnEditorActionListener { v, actionId, event ->
            if ((actionId == EditorInfo.IME_ACTION_DONE)
                || ((event.keyCode == KeyEvent.KEYCODE_ENTER)
                        && (event.action == KeyEvent.ACTION_DOWN))
            ) {
                saveShoppingList()
            }
            return@setOnEditorActionListener false
        }
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
                ViewState.Status.ERROR -> handleErrorStatus(it.error)
            }
        })
    }

    /**
     * Show generic error message
     */
    private fun showGenericErrorMessage() {
        Snackbar.make(parent, "Action not performed. Try again!", Snackbar.LENGTH_SHORT ).show()
    }

    /**
     * Save shopping list
     */
    private fun saveShoppingList() {
        val shoppingListName = newShoppingList.text.toString()
        if (!shoppingListName.isEmpty()) {
            newShoppingListViewModel.saveShoppingListName(name = shoppingListName)
            hideKeyboard()
        } else {
            Snackbar.make(parent, "Empty shoppping list name", Snackbar.LENGTH_SHORT ).show()
        }
    }

    /**
     * Show error message
     */
    private fun handleErrorStatus(error: Throwable?) {
        error?.let {
            when (it) {
                is UserNotLogged -> goToMainScreen()
                else -> showGenericErrorMessage()
            }
        }
    }

    /**
     * Go to shoppingListScreen
     */
    private fun goToShoppingListScreen() {
        findNavController().popBackStack()
    }

    /**
     * Go to main screen
     */
    private fun goToMainScreen() {
        findNavController().popBackStack(R.id.splashScreen, false)
    }

}