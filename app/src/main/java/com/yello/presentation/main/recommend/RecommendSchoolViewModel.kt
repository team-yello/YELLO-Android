package com.yello.presentation.main.recommend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.entity.RecommendModel

class RecommendSchoolViewModel : ViewModel() {

    private val _recommendResult: MutableLiveData<List<RecommendModel>> = MutableLiveData()
    val recommendResult: LiveData<List<RecommendModel>> = _recommendResult

    fun addListFromLocal() {
        val mockList = listOf(
            RecommendModel(1, "이강민", "디자인대 파트업무과 23학번", null),
            RecommendModel(2, "전채연", "아요대 당번일과 25학번", null),
        )
        _recommendResult.value = mockList
    }
}
