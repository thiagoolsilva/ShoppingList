/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.tls.authentication

import android.app.Application
//import timber.log.Timber
//import timber.log.Timber.DebugTree


class Application : Application() {

    override fun onCreate() {
        super.onCreate()

//        if (BuildConfig.DEBUG) {
//            Timber.plant(DebugTree())
//        }
    }
}