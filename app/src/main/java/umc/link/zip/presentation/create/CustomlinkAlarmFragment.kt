package umc.link.zip.presentation.create

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomlinkAlarmBinding
import umc.link.zip.presentation.base.BaseFragment
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class CustomlinkAlarmFragment : BaseFragment<FragmentCustomlinkAlarmBinding>(R.layout.fragment_customlink_alarm) {

    private val viewModel: CreateViewModel by activityViewModels()

    override fun initObserver() {
        lifecycleScope.launch {
            viewModel.link.collectLatest { link ->
                val alertDate = link.alertDate

                // 날짜와 시간 부분 분리
                val date = alertDate.substringBefore("T")
                val time = alertDate.substringAfter("T").removeSuffix("Z")

                // 날짜와 시간 형식 변환
                val formattedDate = formatDate(date)
                val formattedTime = formatTime(time)

                // UI에 바인딩
                binding.tvCustomLinkAlarmDate.text = formattedDate
                binding.tvCustomLinkAlarmTime.text = formattedTime
            }
        }
    }

    override fun initView() {
        binding.ivCustomLinkAlarmToolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.clCustomLinkAlarmCompleteBtn.setOnClickListener {
            navigateToCustom()
        }

        binding.tvCustomLinkAlarmDelete.setOnClickListener {
            clearAlarm()
        }
    }

    private fun navigateToCustom() {
        findNavController().navigate(R.id.action_customlinkAlarmFragment_to_customlinkCustomFragment)
    }

    private fun formatDate(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        val parsedDate = inputFormat.parse(date)
        return outputFormat.format(parsedDate)
    }

    private fun formatTime(time: String): String {
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("a hh:mm", Locale.ENGLISH) // 언어 영어로 고정
        val parsedTime = inputFormat.parse(time)
        return outputFormat.format(parsedTime)
    }

    private fun clearAlarm() {
        // 날짜, 시간 visibility gone으로 설정
        binding.tvCustomLinkAlarmDate.visibility = View.GONE
        binding.tvCustomLinkAlarmTime.visibility = View.GONE

        // 초기화 메시지 visibility visible로 설정
        binding.tvCustomLinkAlarmDateNone.visibility = View.VISIBLE
        binding.tvCustomLinkAlarmTimeNone.visibility = View.VISIBLE
    }
}
