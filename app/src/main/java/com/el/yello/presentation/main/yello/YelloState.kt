package com.el.yello.presentation.main.yello

sealed class YelloState {
    object Lock : YelloState()
    data class Valid(val point: Int, val hasFourFriends: Boolean) : YelloState()
    data class Wait(val leftSec: Long) : YelloState()
}
