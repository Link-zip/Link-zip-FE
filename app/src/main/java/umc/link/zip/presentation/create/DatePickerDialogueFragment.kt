package umc.link.zip.presentation.create

import android.widget.DatePicker
import androidx.fragment.app.activityViewModels
import umc.link.zip.R
import umc.link.zip.databinding.FragmentPickerDateBinding
import umc.link.zip.presentation.base.BaseBottomSheetDialogFragment
import umc.link.zip.presentation.create.adapter.CreateViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DatePickerDialogueFragment : BaseBottomSheetDialogFragment<FragmentPickerDateBinding>(R.layout.fragment_picker_date) {

    private val createViewModel: CreateViewModel by activityViewModels()

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    interface DatePickerListener {
        fun onDatePicked(date: String)
    }

    private var datePickerListener: DatePickerListener? = null

    override fun initObserver() {}

    override fun initView() {
        val initialDate = createViewModel.link.value?.alertDate?.substringBefore("T")
            ?: SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)

        val calendar = Calendar.getInstance().apply {
            time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(initialDate)
        }

        binding.customDatePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        binding.btnCustomDatePickerComplete.setOnClickListener {
            val datePicker: DatePicker = binding.customDatePicker
            val selectedDate = Calendar.getInstance().apply {
                set(datePicker.year, datePicker.month, datePicker.dayOfMonth)
            }.time

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate)

            datePickerListener?.onDatePicked(formattedDate)
            dismiss()
        }
    }

    fun setDatePickerListener(listener: DatePickerListener) {
        datePickerListener = listener
    }

}
