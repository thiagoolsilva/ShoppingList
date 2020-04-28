/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.tls.firebase.util

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textfield.TextInputLayout


fun SwipeRefreshLayout.disableLoading() {
    this.isRefreshing = false
}

fun TextInputLayout.disableErrorMessage() {
    this.error = null
    this.isErrorEnabled = false
}