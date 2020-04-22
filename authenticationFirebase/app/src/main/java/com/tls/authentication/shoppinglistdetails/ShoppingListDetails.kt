/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.tls.authentication.shoppinglistdetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tls.authentication.R

class ShoppingListDetails : Fragment() {

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
        val shoppingId = arguments?.getString(ARGUMENT_SHOPPING_ID)
        Log.d("lopes-shoppingDetails", shoppingId ?: "")

    }

}