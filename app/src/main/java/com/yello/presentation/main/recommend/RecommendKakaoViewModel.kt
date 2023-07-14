package com.yello.presentation.main.recommend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.entity.RecommendModel

class RecommendKakaoViewModel : ViewModel() {

    private val _recommendResult: MutableLiveData<List<RecommendModel>> = MutableLiveData()
    val recommendResult: LiveData<List<RecommendModel>> = _recommendResult

    fun addListFromLocal() {
        val mockList = listOf(
            RecommendModel(1, "김상호", "기획대 손씻기과 19학번", null),
            RecommendModel(2, "이강민", "디자인대 파트업무과 23학번", null),
            RecommendModel(3, "전채연", "아요대 당번일과 25학번", null),
            RecommendModel(4, "박민주", "안드로이드안드로이드대 국희의콩나물국과 29학번", null),
            RecommendModel(1, "김상호", "기획대 손씻기과 19학번", null),
            RecommendModel(2, "이강민", "디자인대 파트업무과 23학번", null),
            RecommendModel(3, "전채연", "아요대 당번일과 25학번", null),
            RecommendModel(4, "박민주", "안드로이드안드로이드대 국희의콩나물국과 29학번", null),
            RecommendModel(1, "김상호", "기획대 손씻기과 19학번", null),
            RecommendModel(2, "이강민", "디자인대 파트업무과 23학번", null),
            RecommendModel(3, "전채연", "아요대 당번일과 25학번", null),
            RecommendModel(4, "박민주", "안드로이드안드로이드대 국희의콩나물국과 29학번", null)
        )
        _recommendResult.value = mockList
    }
}