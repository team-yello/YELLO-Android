package com.example.domain

import com.example.domain.entity.vote.StoredVote
import java.util.Date

interface YelloDataStore {
    var userToken: String
    var refreshToken: String
    var deviceToken: String
    var isLogin: Boolean
    var yelloId: String
    var isFirstLogin: Boolean
    var storedVote: StoredVote?
    var disabledNoticeUrl: String?
    var disabledNoticeDate: String?

    fun clearLocalPref()
}
