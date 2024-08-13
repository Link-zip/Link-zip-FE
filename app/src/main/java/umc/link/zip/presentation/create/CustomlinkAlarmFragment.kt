package umc.link.zip.presentation.create

import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomlinkAlarmBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.util.extension.repeatOnStarted
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class CustomlinkAlarmFragment : BaseFragment<FragmentCustomlinkAlarmBinding>(R.layout.fragment_customlink_alarm),
    DatePickerDialogueFragment.DatePickerListener, TimePickerDialogueFragment.TimePickerListener {

    private val viewModel: CreateViewModel by activityViewModels()

    override fun initObserver() {
        repeatOnStarted {
            viewModel.link.collectLatest { link ->
                val alertDate = link.alertDate

                if (alertDate.isNullOrEmpty()) {
                    clearAlarm() // 알림이 없는 경우
                } else {
                    val date = alertDate.substringBefore("T")
                    val time = alertDate.substringAfter("T").removeSuffix("Z")

                    // 날짜와 시간 포맷팅
                    val formattedDate = formatDate(date)
                    val formattedTime = formatTime(time)

                    binding.tvCustomLinkAlarmDate.text = formattedDate
                    binding.tvCustomLinkAlarmTime.text = formattedTime

                    setAlarm() // 알림이 있는 경우
                }
            }
        }
    }

    override fun initView() {
        binding.ivCustomLinkAlarmToolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvCustomLinkAlarmDelete.setOnClickListener {
            clearAlarm()
            // 알림 삭제 후 데이터 null 설정
            repeatOnStarted {viewModel.updateAlertDate(null, null)}
        }

        binding.btnCustomLinkAlarmComplete.setOnClickListener {
            val date = binding.tvCustomLinkAlarmDate.text.toString().replace(".", "-") // yyyy.MM.dd -> yyyy-MM-dd
            val time = binding.tvCustomLinkAlarmTime.text.toString()

            when {
                date.isNotEmpty() && time.isNotEmpty() -> {
                    repeatOnStarted{viewModel.updateAlertDate(date, formatTimeForISO(time))}
                    findNavController().navigateUp()
                }
                date.isEmpty() && time.isEmpty() -> {
                    repeatOnStarted{viewModel.updateAlertDate(null, null)}
                    findNavController().navigateUp()
                }
                date.isEmpty() -> {
                    Toast.makeText(requireContext(), "날짜를 선택해주세요", Toast.LENGTH_SHORT).show()
                }
                time.isEmpty() -> {
                    Toast.makeText(requireContext(), "시간을 선택해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }

        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.btnCustomLinkAlarmDate.setOnClickListener {
            val datePicker = DatePickerDialogueFragment()
            datePicker.setDatePickerListener(this)
            datePicker.show(childFragmentManager, "DatePicker")
        }

        binding.btnCustomLinkAlarmTime.setOnClickListener {
            val timePicker = TimePickerDialogueFragment()
            timePicker.setTimePickerListener(this)
            timePicker.show(childFragmentManager, "TimePicker")
        }
    }

    override fun onDatePicked(date: String) {
        binding.tvCustomLinkAlarmDate.text = formatDate(date)
        setAlarm()
    }

    override fun onTimePicked(time: String) {
        binding.tvCustomLinkAlarmTime.text = formatTime(time)
        setAlarm()
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
        binding.tvCustomLinkAlarmDate.visibility = View.GONE
        binding.tvCustomLinkAlarmTime.visibility = View.GONE
        binding.tvCustomLinkAlarmDateNone.visibility = View.VISIBLE
        binding.tvCustomLinkAlarmTimeNone.visibility = View.VISIBLE
    }

    private fun setAlarm() {
        binding.tvCustomLinkAlarmDate.visibility = View.VISIBLE
        binding.tvCustomLinkAlarmTime.visibility = View.VISIBLE
        binding.tvCustomLinkAlarmDateNone.visibility = View.GONE
        binding.tvCustomLinkAlarmTimeNone.visibility = View.GONE
    }
}
