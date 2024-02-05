package com.el.yello.presentation.event

import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityEventBinding
import com.example.ui.base.BindingActivity

class EventActivity : BindingActivity<ActivityEventBinding>(R.layout.activity_event) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
    }
}
