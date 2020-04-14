/*
 * Copyright (c) 2020  Thiago Lopes da Silva
 * All Rights Reserved.
 *
 */

package com.tls.authentication

import android.app.Application
import com.example.presentation.di.authenticationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        // config koin dependency injection
        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@Application)
            modules(
                listOf(
                    authenticationModule
                )
            )
        }
    }
}