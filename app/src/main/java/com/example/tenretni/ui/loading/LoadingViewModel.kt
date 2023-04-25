package com.example.tenretni.ui.loading

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import com.example.tenretni.core.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoadingViewModel: ViewModel() {
    private var _timerCounter = 0
    private val _loadingUiState = MutableStateFlow<LoadingUiState>(LoadingUiState.Loading(_timerCounter))
    val loadingUiState = _loadingUiState.asStateFlow()




    private var timer = object : CountDownTimer(Constants.Loading.LOADING_TIME, Constants.Loading.LOADING_INCREMENT){
        override fun onTick(millisUntilFinished: Long) {
            _timerCounter++
            _loadingUiState.update {
                LoadingUiState.Loading(_timerCounter)
            }

        }

        override fun onFinish() {
            cancel()
            _loadingUiState.update {
                LoadingUiState.Finished

            }

        }


    }

    init {
        timer.start()
    }


}