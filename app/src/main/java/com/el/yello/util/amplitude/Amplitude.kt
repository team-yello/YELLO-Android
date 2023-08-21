package com.el.yello.util.amplitude

import com.amplitude.api.Amplitude
import com.amplitude.api.Identify
import org.json.JSONObject

object AmplitudeUtils {

    private val amplitude = Amplitude.getInstance()

    fun trackEventWithProperties(eventName: String, properties: JSONObject? = null) {
        if (properties == null) {
            amplitude.logEvent(eventName)
        } else {
            amplitude.logEvent(eventName, properties)
        }
    }
}
