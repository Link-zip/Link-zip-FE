package umc.link.zip.presentation.mypage

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.widget.ImageView
import androidx.core.animation.addListener
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentMypageSettingBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class MypageSettingFragment : BaseFragment<FragmentMypageSettingBinding>(R.layout.fragment_mypage_setting) {

    private val navigator by lazy { findNavController() }
    private var isAlarmOn = true // 초기 상태를 true로 설정

    override fun initObserver() {
        // 옵저버 초기화 코드
    }

    override fun initView() {
        binding.ivMypageSettingToolbarBack.setOnClickListener {
            navigator.navigateUp()
        }
        binding.clMypageSettingNotification.setOnClickListener {
            navigator.navigate(R.id.action_mypageSettingFragment_to_noticeFragment)
        }
        binding.clMypageSettingPolicies.setOnClickListener {
            navigator.navigate(R.id.action_mypageSettingFragment_to_mypageSettingPdfFragment)
        }

        // 알람 아이콘 클릭 리스너 설정
        binding.ivMypageSettingAlarm.setOnClickListener {
            toggleAlarm()
        }
    }

    private fun toggleAlarm() {
        if (isAlarmOn) {
            setAlarmOffAnimation()
        } else {
            setAlarmOnAnimation()
        }
        isAlarmOn = !isAlarmOn // 상태를 반전시킴
    }

    private fun setAlarmOffAnimation() {
        val fadeOut = ObjectAnimator.ofFloat(binding.ivMypageSettingAlarm, "alpha", 1f, 0f)
        fadeOut.duration = 50
        fadeOut.addListener(onEnd = {
            binding.ivMypageSettingAlarm.setImageResource(R.drawable.toggle_mypage_setting_alarm_no)
            val fadeIn = ObjectAnimator.ofFloat(binding.ivMypageSettingAlarm, "alpha", 0f, 1f)
            fadeIn.duration = 50
            fadeIn.start()
        })
        fadeOut.start()
    }

    private fun setAlarmOnAnimation() {
        val fadeOut = ObjectAnimator.ofFloat(binding.ivMypageSettingAlarm, "alpha", 1f, 0f)
        fadeOut.duration = 50
        fadeOut.addListener(onEnd = {
            binding.ivMypageSettingAlarm.setImageResource(R.drawable.toggle_mypage_setting_alarm)
            val fadeIn = ObjectAnimator.ofFloat(binding.ivMypageSettingAlarm, "alpha", 0f, 1f)
            fadeIn.duration = 50
            fadeIn.start()
        })
        fadeOut.start()
    }
}
