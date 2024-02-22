package com.example.ui.extension

import android.app.Activity
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Parcelable
import kotlin.properties.ReadOnlyProperty

fun intExtra(defaultValue: Int = -1) = ReadOnlyProperty<Activity, Int> { thisRef, property ->
    thisRef.intent.extras?.getInt(
        property.name,
        defaultValue,
    ) ?: defaultValue
}

fun longExtra(defaultValue: Long = -1) = ReadOnlyProperty<Activity, Long> { thisRef, property ->
    thisRef.intent.extras?.getLong(
        property.name,
        defaultValue,
    ) ?: defaultValue
}

fun boolExtra(defaultValue: Boolean = false) =
    ReadOnlyProperty<Activity, Boolean> { thisRef, property ->
        thisRef.intent.extras?.getBoolean(
            property.name,
            defaultValue,
        ) ?: defaultValue
    }

fun stringExtra(defaultValue: String? = null) =
    ReadOnlyProperty<Activity, String?> { thisRef, property ->
        if (defaultValue == null) {
            thisRef.intent.extras?.getString(property.name)
        } else {
            thisRef.intent.extras?.getString(property.name, defaultValue)
        }
    }

inline fun <reified T : Parcelable> Intent.getCompatibleParcelableExtra(key: String): T? = when {
    SDK_INT >= TIRAMISU -> getParcelableExtra(key, T::class.java)
    else -> getParcelableExtra(key) as? T
}
