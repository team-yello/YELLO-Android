package com.example.domain

import com.example.domain.entity.vote.StoredVote

interface YelloDataStore {
    var userToken: String
    var refreshToken: String
    var deviceToken: String
    var isLogin: Boolean
    var yelloId: String
    var isFirstLogin: Boolean
    var storedVote: StoredVote?
    var disabledNoticeUrl: String?

    fun clearLocalPref()
}
