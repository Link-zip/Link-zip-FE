package umc.link.zip.presentation.create

import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomtextAlarmBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.create.adapter.LinkExtractViewModel
import umc.link.zip.util.extension.repeatOnStarted
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class CustomtextAlarmFragment : BaseFragment<FragmentCustomtextAlarmBinding>(R.layout.fragment_customtext_alarm),
    DatePickerDialogueFragment.DatePickerListener, TimePickerDialogueFragment.TimePickerListener {


    private val createViewModel: CreateViewModel by activityViewModels()
    private val linkExtractViewModel: LinkExtractViewModel by activityViewModels()

    override fun initObserver() {
        repeatOnStarted {
            createViewModel.link.collectLatest { link ->
                val alertDate = link.alertDate

                if (alertDate.isNullOrEmpty()) {
                    clearAlarm() // 알림이 없는 경우
                } else {
                    // 알림이 있는 경우, 날짜와 시간을 분리하여 표시
                    val date = alertDate.substringBefore("T")
                    val time = alertDate.substringAfter("T").removeSuffix("Z")

                    // 날짜와 시간 포맷팅
                    val formattedDate = formatDate(date)
                    val formattedTime = formatTime(time)

                    binding.tvCustomTextAlarmDate.text = formattedDate
                    binding.tvCustomTextAlarmTime.text = formattedTime

                    setAlarm() // 알림이 있는 경우 UI 업데이트
                }
            }
        }
    }

    override fun initView() {
        binding.ivCustomTextAlarmToolbarBack.setOnClickListener {
            navigator.navigateUp()
        }

        binding.tvCustomTextAlarmDelete.setOnClickListener {
            clearAlarm() // UI 초기화
        }

        binding.btnCustomTextAlarmComplete.setOnClickListener {
            val date = binding.tvCustomTextAlarmDate.text.toString()
                .replace(".", "-") // yyyy.MM.dd -> yyyy-MM-dd
            val time = binding.tvCustomTextAlarmTime.text.toString()

            if (binding.tvCustomTextAlarmDateNone.visibility == View.VISIBLE &&
                binding.tvCustomTextAlarmTimeNone.visibility == View.VISIBLE
            ) {
                repeatOnStarted {
                    createViewModel.clearAlertDate() // 뷰모델의 알림 날짜 초기화
                }
                navigator.navigateUp()
            }
            else {
                when {
                    date.isNotEmpty() && time.isNotEmpty() -> {
                        repeatOnStarted {
                            createViewModel.updateAlertDate(date, formatTimeForISO(time)) // 날짜와 시간 업데이트
                        }
                        navigator.navigateUp()
                    }

                    date.isEmpty() && time.isNotEmpty() -> {
                        Toast.makeText(requireContext(), "날짜를 선택해주세요", Toast.LENGTH_SHORT).show()
                    }

                    time.isEmpty() && date.isNotEmpty() -> {
                        Toast.makeText(requireContext(), "시간을 선택해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.btnCustomTextAlarmDate.setOnClickListener {
            val datePicker = DatePickerDialogueFragment()
            datePicker.setDatePickerListener(this)
            datePicker.show(childFragmentManager, "DatePicker")
        }

        binding.btnCustomTextAlarmTime.setOnClickListener {
            val timePicker = TimePickerDialogueFragment()
            timePicker.setTimePickerListener(this)
            timePicker.show(childFragmentManager, "TimePicker")
        }
    }

    override fun onDatePicked(date: String) {
        binding.tvCustomTextAlarmDate.text = formatDate(date)
        setAlarm() // 알람 설정 UI 업데이트
    }

    override fun onTimePicked(time: String) {
        binding.tvCustomTextAlarmTime.text = formatTime(time)
        setAlarm() // 알람 설정 UI 업데이트
    }

    private fun formatDate(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        return try {
            val parsedDate = inputFormat.parse(date)
            outputFormat.format(parsedDate)
        } catch (e: Exception) {
            ""
        }
    }

    private fun formatTime(time: String): String {
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("a hh:mm", Locale.ENGLISH)
        return try {
            val parsedTime = inputFormat.parse(time)
            outputFormat.format(parsedTime)
        } catch (e: Exception) {
            ""
        }
    }

    private fun formatTimeForISO(time: String): String {
        val inputFormat = SimpleDateFormat("a hh:mm", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return try {
            val parsedTime = inputFormat.parse(time)
            outputFormat.format(parsedTime)
        } catch (e: Exception) {
            ""
        }
    }

    private fun clearAlarm() {
        binding.tvCustomTextAlarmDate.visibility = View.GONE
        binding.tvCustomTextAlarmTime.visibility = View.GONE
        binding.tvCustomTextAlarmDateNone.visibility = View.VISIBLE
        binding.tvCustomTextAlarmTimeNone.visibility = View.VISIBLE
    }

    private fun setAlarm() {
        binding.tvCustomTextAlarmDate.visibility = View.VISIBLE
        binding.tvCustomTextAlarmTime.visibility = View.VISIBLE
        binding.tvCustomTextAlarmDateNone.visibility = View.GONE
        binding.tvCustomTextAlarmTimeNone.visibility = View.GONE
    }

    private val navigator by lazy {
        findNavController()
    }
}