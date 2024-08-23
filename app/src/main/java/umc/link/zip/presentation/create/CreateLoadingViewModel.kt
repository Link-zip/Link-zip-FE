package umc.link.zip.presentation.create

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateLoadingViewModel : ViewModel() {

    private val _currentVisibleView = MutableStateFlow(0)
    val currentVisibleView: StateFlow<Int> get() = _currentVisibleView.asStateFlow()

    private val _animationFinished = MutableStateFlow(false)
    val animationFinished: StateFlow<Boolean> get() = _animationFinished.asStateFlow()

    init {
        startAnimation()
    }

    private fun startAnimation() {
        object : CountDownTimer(15000, 200) {
            var currentIndex = 0
            override fun onTick(millisUntilFinished: Long) {
                _currentVisibleView.value = currentIndex
                currentIndex = (currentIndex + 1) % 3
            }

            override fun onFinish() {
                _animationFinished.value = true // 애니메이션이 끝났음을 알림
            }
        }.start()
    }
}
