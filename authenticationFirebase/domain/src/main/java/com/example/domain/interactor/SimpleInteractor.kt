/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.example.domain.interactor

interface SimpleInteractor<T> {

    fun execute() : T

}