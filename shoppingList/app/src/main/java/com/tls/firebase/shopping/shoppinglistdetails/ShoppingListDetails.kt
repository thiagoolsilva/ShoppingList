/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.tls.firebase.shopping.shoppinglistdetails

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.presentation.ShoppingListItemViewModel
import com.example.presentation.model.ShoppingListItemView
import com.example.presentation.model.ViewState
import com.google.android.material.snackbar.Snackbar
import com.tls.firebase.R
import com.tls.firebase.util.hideKeyboard
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

        configViews()
        configReceivedShoppingId()
        configViewModel()
        configRecyclerView()
    }

    /**
     * config views
     */
    private fun configViews() {
        txtInputEdtText.setOnEditorActionListener { v, actionId, event ->
            if ((actionId == EditorInfo.IME_ACTION_DONE)
                || ((event.keyCode == KeyEvent.KEYCODE_ENTER)
                        && (event.action == KeyEvent.ACTION_DOWN))
            ) {
                handleInsertItemEvent()
            }
            return@setOnEditorActionListener false
        }
        addShoppingItem.setOnClickListener {
            handleInsertItemEvent()
        }
    }

    override fun onResume() {
        super.onResume()
        fetchNewShoppingItems()
    }

    /**
     * Config recycler view
     */
    private fun configRecyclerView() {
        adapter = ShoppingListItemAdapter {
            shoppingListItemViewModel.updateShoppingListItem(
                ItemDescription = it.description,
                uuid = it.uuid, shoppingListId = shoppingListId, checkStatus = it.check
            )
        }
        shoppingListItems.layoutManager = LinearLayoutManager(activity)
        shoppingListItems.adapter = adapter
    }

    /**
     * config received shopping id
     */
    private fun configReceivedShoppingId() {
        shoppingListId = arguments?.getString(ARGUMENT_SHOPPING_ID) ?: ""
        Timber.d("Received item %s", shoppingListId)
    }

    /**
     * Config view models
     */
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

        shoppingListItemViewModel.updateShoppingListItemState.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ViewState.Status.SUCCESS -> fetchNewShoppingItems()
                ViewState.Status.ERROR -> showGetItemsErrorMessage()
            }
        })
    }

    /**
     * Show save error message
     */
    private fun showSaveErrorMessage() {
        Snackbar.make(parent, getString(R.string.shop_list_try_again), Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Show a snackbar displaying a error message for empty text
     */
    private fun showSnackEmptyText() {
        Snackbar.make(parent, getString(R.string.shop_list_empty_content), Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Show get item error message
     */
    private fun showGetItemsErrorMessage() {
        Toast.makeText(activity, getString(R.string.shop_list_error_get_items), Toast.LENGTH_SHORT).show()
    }

    private fun handleInsertItemEvent() {
        val itemDescription = txtInputEdtText.text.toString()
        if (itemDescription.isEmpty()) {
            showSnackEmptyText()
        } else {
            shoppingListItemViewModel.saveShoppingListItem(itemDescription, shoppingListId)
        }
        txtInputEdtText.text?.clear()

        // Close soft keyboard by extension function
        hideKeyboard()
    }

    /**
     * Fetch new shopping items
     */
    private fun fetchNewShoppingItems() {
        shoppingListItemViewModel.getShoppingItems(shoppingListId = shoppingListId)
    }

    /**
     * reload adapter
     */
    private fun reloadAdapter(data: List<ShoppingListItemView>?) {
        adapter.submitList(data)
    }

}