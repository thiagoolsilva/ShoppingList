/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.interactor

import androidx.lifecycle.MutableLiveData

interface BaseInteractor<in T, R> {

    fun execute(parameter: T, result:MutableLiveData<Result<R>>)

}