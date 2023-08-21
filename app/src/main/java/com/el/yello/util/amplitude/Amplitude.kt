package com.el.yello.util.amplitude

import androidx.lifecycle.MutableLiveData
import com.amplitude.api.Amplitude
import com.amplitude.api.Identify
import com.example.domain.entity.onboarding.GroupList
import com.example.ui.view.UiState
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
    fun updateUserProperties(propertyName: String, values: String) {
        val identify = Identify().set(propertyName, values)
        amplitude.identify(identify)
    }

    fun updateUserIntProperties(propertyName: String, values: Int) {
        val identify = Identify().set(propertyName, values)
        amplitude.identify(identify)
    }
}
