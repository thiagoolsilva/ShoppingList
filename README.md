# Project description

It is a shopping list app that will help user to create its shopping list using online/offline feature by firebase.

The goal of the project is to demonstrate best practices by using up to date tech-stack and presenting modern Android application.

[![License Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=true)](http://www.apache.org/licenses/LICENSE-2.0)
![minSdkVersion 22](https://img.shields.io/badge/minSdkVersion-22-brightgreen?style=true)
![compileSdkVersion 29](https://img.shields.io/badge/compileSdkVersion-29-brightgreen)

![CI](https://github.com/thiagoolsilva/ShoppingList/workflows/CI/badge.svg)

## Project characteristics

This project brings to table set of best practices, tools, and solutions:

* 100% [Kotlin](https://kotlinlang.org/)
* Modularization (app, presentation, domain, data, shared)
* [Android Jetpack](https://developer.android.com/jetpack)
* A single-activity architecture, using the [Navigation component](https://developer.android.com/guide/navigation/navigation-getting-started)
* Testing
* Dependency Injection
* Material design
* [Firebase](https://firebase.google.com/) (Authentication and Firestone)

## App Screenshot

<img src="misc/shopping-list.gif" width="600"/>

## Tech-stack

* Tech-stack
    * [Kotlin](https://kotlinlang.org/) + [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - perform background operations
    * [Koin](https://insert-koin.io/) - dependency injection
    * [Firebase authentication](https://firebase.google.com/docs/auth) - authentication
    * [Firebase firestone](https://firebase.google.com/docs/firestore) - remote database
	* [kotlinx-coroutines-play-services](https://github.com/Kotlin/kotlinx.coroutines/tree/master/integration/kotlinx-coroutines-play-services) - extension function to handle firebase requests
    * [Jetpack](https://developer.android.com/jetpack)
        * [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) - deal with whole in-app navigation
        * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - notify views about database change
        * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - perform an action when lifecycle state changes
        * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - store and manage UI-related data in a lifecycle conscious way
        * [Constraintlayout](https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintLayout) - allows to build  flexible layout
		* [Test](https://developer.android.com/training/testing/) -  tests
	* [Timber](https://github.com/JakeWharton/timber) - logging
	* [core-ktx](https://developer.android.com/kotlin/ktx) - Kotlin extensions
    * [swiperefreshlayout](https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout) - visual refresh layout
    * [and more...](https://github.com/thiagoolsilva/ShoppingList)
* Architecture
    * Clean Architecture (at module level)
    * MVVM (presentation layer)
* Tests
    * [Unit Tests](https://en.wikipedia.org/wiki/Unit_testing) ([JUnit](https://junit.org/junit4/))
    * [core-testing](https://androidx.tech/artifacts/arch.core/core-testing/) - used to sync background tasks
    * [kotlinx-coroutines-test](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/) - used to sync coroutines jobs
	* [mockk](https://mockk.io/) - mock objects using kotlin's style
* Gradle

## Architecture

The application is separated  in modules as displayed below

<img src="misc/modulos-app.png" width="600"/>

## What this project does not cover?

The project does not cover e2e and integration tests once it will be implemented as soon as possible.

## Getting started

There are a few ways to open this project.

### Android Studio

1. Android Studio -> File -> New -> From Version control -> Git
2. Enter `https://github.com/thiagoolsilva/ShoppingList` into URL field

### Command-line + Android Studio

1. Run `git clone https://github.com/thiagoolsilva/ShoppingList`
2. Android Studio -> File -> Open

### Projects

Other high-quality projects will help you to find solutions that work for your project:

* [books jetpack](https://github.com/nglauber/books_jetpack) - a showcase that uses jetpack components
* [Android sunflower](https://github.com/googlesamples/android-sunflower) complete `Jetpack` sample covering all  libraries
* [Clean Architecture boilerplate](https://github.com/bufferapp/android-clean-architecture-boilerplate) - contains nice  diagrams of Clean Architecture layers

## Author

<img src="misc/myAvatar.png" width="40"/>

Follow me

[![Follow me](https://img.shields.io/badge/Medium-thiagoolsilva-yellowgreen)](https://medium.com/@thiagolopessilva)
[![Linkedin](https://img.shields.io/badge/Linkedin-thiagoolsilva-blue)](https://www.linkedin.com/in/thiago-lopes-silva-2b943a25/)
[![Twitter](https://img.shields.io/twitter/follow/thiagoolsilva?style=social)](https://twitter.com/thiagoolsilva)   

## License
```
Copyright (c) 2020  Thiago Lopes da Silva

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
`
