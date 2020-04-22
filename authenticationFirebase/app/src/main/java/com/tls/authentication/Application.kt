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
import timber.log.Timber

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        configkoin()
        configTimber()
    }

    private fun configTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun configkoin() {
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