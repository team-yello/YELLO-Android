package com.yello.presentation.main.yello

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.YelloDataStore
import com.example.domain.entity.type.YelloState
import com.example.domain.entity.type.YelloState.Lock
import com.example.domain.entity.type.YelloState.Valid
import com.example.domain.entity.type.YelloState.Wait
import com.example.domain.repository.VoteRepository
import com.example.ui.view.UiState
import com.example.ui.view.UiState.Empty
import com.example.ui.view.UiState.Failure
import com.example.ui.view.UiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

@HiltViewModel
class YelloViewModel @Inject constructor(
    private val voteRepository: VoteRepository,
    private val dataStore: YelloDataStore,
) : ViewModel() {
    private val _yelloState = MutableLiveData<UiState<YelloState>>()
    val yelloState: LiveData<UiState<YelloState>>
        get() = _yelloState

    private val _leftTime = MutableLiveData<Long>()
    val leftTime: LiveData<Long>
        get() = _leftTime

    private val _point = MutableLiveData<Int>()
    private val point: Int
        get() = _point.value ?: 0

    private val _isDecreasing = MutableLiveData(false)
    private val isDecreasing: Boolean
        get() = _isDecreasing.value ?: false

    init {
        getVoteState()
    }

    fun decreaseTime() {
        viewModelScope.launch {
            leftTime.value ?: return@launch
            if (isDecreasing) return@launch
            _isDecreasing.value = true
            while (requireNotNull(leftTime.value) > 0) {
                delay(1000L)
                if (requireNotNull(leftTime.value) <= 0) return@launch
                _leftTime.value = leftTime.value?.minus(1)
            }

            getVoteState()
            _isDecreasing.value = false
        }
    }

    fun getVoteState() {
        viewModelScope.launch {
            voteRepository.getVoteAvailable()
                .onSuccess { voteState ->
                    Timber.d("GET VOTE STATE SUCCESS : $voteState")
                    if (voteState == null) {
                        _yelloState.value = Empty
                        return@launch
                    }
                    if (voteState.isStart) {
                        _yelloState.value = Success(Valid(voteState.point))
                        return@launch
                    }

                    _yelloState.value = Success(Wait(voteState.leftTime))
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        Timber.e("GET VOTE STATE FAILURE : $t")
                        when (t.code()) {
                            CODE_NO_FRIEND -> _yelloState.value = Success(Lock)
                            else -> _yelloState.value = Failure(t.message())
                        }
                    }
                    Timber.e("GET VOTE STATE ERROR : $t")
                }
        }
    }

    companion object {
        const val SEC_MAX_LOCK_TIME = 2400L

        private const val CODE_NO_FRIEND = 400
    }
}
