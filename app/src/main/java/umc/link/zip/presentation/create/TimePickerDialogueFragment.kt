package umc.link.zip.presentation.create

import android.widget.TimePicker
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.databinding.FragmentPickerTimeBinding
import umc.link.zip.presentation.base.BaseBottomSheetDialogFragment
import umc.link.zip.presentation.create.adapter.CreateViewModel
import umc.link.zip.presentation.create.adapter.LinkAddViewModel
import umc.link.zip.util.extension.repeatOnStarted
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TimePickerDialogueFragment : BaseBottomSheetDialogFragment<FragmentPickerTimeBinding>(R.layout.fragment_picker_time) {

    private val linkAddViewModel: LinkAddViewModel by activityViewModels()

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    interface TimePickerListener {
        fun onTimePicked(time: String)
    }

    private var timePickerListener: TimePickerListener? = null

    override fun initObserver() {
        repeatOnStarted {
            linkAddViewModel.link.collectLatest { link ->
                val initialTime = link.alertDate?.substringAfter("T")?.removeSuffix("Z") ?: "00:00:00"
                val calendar = Calendar.getInstance().apply {
                    time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).parse(initialTime)
                }
                binding.customTimePicker.hour = calendar.get(Calendar.HOUR_OF_DAY)
                binding.customTimePicker.minute = calendar.get(Calendar.MINUTE)
            }
        }
    }

    override fun initView() {
        binding.btnCustomTimePickerComplete.setOnClickListener {
            val timePicker: TimePicker = binding.customTimePicker
            val selectedTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, timePicker.hour)
                set(Calendar.MINUTE, timePicker.minute)
            }.time

            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val formattedTime = timeFormat.format(selectedTime)

            linkAddViewModel.updateAlertTimeOnly(formattedTime)
            timePickerListener?.onTimePicked(formattedTime)
            dismiss()
        }
    }

    fun setTimePickerListener(listener: TimePickerListener) {
        timePickerListener = listener
    }
}

