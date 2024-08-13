package umc.link.zip.presentation.create

import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.activityViewModels
import umc.link.zip.R
import umc.link.zip.databinding.FragmentPickerTimeBinding
import umc.link.zip.presentation.base.BaseBottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TimePickerDialogueFragment : BaseBottomSheetDialogFragment<FragmentPickerTimeBinding>(R.layout.fragment_picker_time) {

    private val createViewModel: CreateViewModel by activityViewModels()

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    interface TimePickerListener {
        fun onTimePicked(time: String)
    }

    private var timePickerListener: TimePickerListener? = null

    override fun initObserver() {}

    override fun initView() {
        val initialTime = createViewModel.link.value?.alertDate?.substringAfter("T")?.removeSuffix("Z")
            ?: "00:00:00"

        val calendar = Calendar.getInstance().apply {
            time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).parse(initialTime)
        }

        binding.customTimePicker.hour = calendar.get(Calendar.HOUR_OF_DAY)
        binding.customTimePicker.minute = calendar.get(Calendar.MINUTE)

        binding.btnCustomTimePickerComplete.setOnClickListener {
            val timePicker: TimePicker = binding.customTimePicker
            val selectedTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, timePicker.hour)
                set(Calendar.MINUTE, timePicker.minute)
            }.time

            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val formattedTime = timeFormat.format(selectedTime)

            timePickerListener?.onTimePicked(formattedTime)
            dismiss()
        }
    }

    fun setTimePickerListener(listener: TimePickerListener) {
        timePickerListener = listener
    }
}
