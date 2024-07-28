package umc.link.zip.presentation.create

import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomtextAlarmBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class SavelinkAlarmFragment : BaseFragment<FragmentCustomtextAlarmBinding>(R.layout.fragment_customtext_alarm){
    override fun initObserver() {

    }

    override fun initView() {
        binding.tvCustomTextAlarmDate.setOnClickListener {
            showDatePickerBottomSheet()
        }
    }

    private fun showDatePickerBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetBinding = FragmentCustomtextAlarmBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)

//        bottomSheetBinding.btnCustomTextAlarmComplete.setOnClickListener {
//            val year = bottomSheetBinding.datePicker.year
//            val month = bottomSheetBinding.datePicker.month + 1 // DatePicker months are 0-based
//            val day = bottomSheetBinding.datePicker.dayOfMonth
//            // 선택한 날짜를 사용하여 원하는 작업 수행
//            bottomSheetDialog.dismiss()
//        }

        bottomSheetDialog.show()
    }
}