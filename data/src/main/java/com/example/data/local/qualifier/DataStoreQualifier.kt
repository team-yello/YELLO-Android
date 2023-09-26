package com.example.data.local.qualifier

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class User

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class App
