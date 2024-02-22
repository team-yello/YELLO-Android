package com.el.yello.util.manager

import com.amplitude.api.Amplitude
import com.amplitude.api.Identify
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object AmplitudeManager {

    private val amplitude = Amplitude.getInstance()

    fun trackEventWithProperties(eventName: String, properties: JSONObject? = null) {
        if (properties == null) {
            amplitude.logEvent(eventName)
        } else {
            amplitude.logEvent(eventName, properties)
        }
    }

    fun updateUserProperties(propertyName: String, values: String) {
        val identify = Identify().set(propertyName, values)
        amplitude.identify(identify)
    }

    fun updateUserIntProperties(propertyName: String, values: Int) {
        val identify = Identify().set(propertyName, values)
        amplitude.identify(identify)
    }

    fun setUserDataProperties(propertyName: String) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val identify = Identify().setOnce(propertyName, LocalDateTime.now().format(formatter))
        amplitude.identify(identify)
    }
}
