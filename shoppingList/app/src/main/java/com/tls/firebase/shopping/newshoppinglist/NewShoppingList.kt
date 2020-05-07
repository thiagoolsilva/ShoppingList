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

package com.tls.firebase.shopping.newshoppinglist

import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.view.inputmethod.EditorInfo
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
            if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                ((event.keyCode == KeyEvent.KEYCODE_ENTER) &&
                        (event.action == KeyEvent.ACTION_DOWN))
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
        Snackbar.make(parent, R.string.new_list_generic_error, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Save shopping list
     */
    private fun saveShoppingList() {
        val shoppingListName = newShoppingList.text.toString()
        if (shoppingListName.isNotEmpty()) {
            newShoppingListViewModel.saveShoppingListName(name = shoppingListName)
            hideKeyboard()
        } else {
            Snackbar.make(parent, getString(R.string.new_profile_empty_required_fields), Snackbar.LENGTH_SHORT).show()
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
