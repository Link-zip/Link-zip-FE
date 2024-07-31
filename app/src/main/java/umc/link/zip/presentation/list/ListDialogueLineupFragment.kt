package umc.link.zip.presentation.list

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import umc.link.zip.R
import umc.link.zip.databinding.FragmentDialogueLineupBinding
import umc.link.zip.presentation.base.BaseBottomSheetDialogFragment

class ListDialogueLineupFragment : BaseBottomSheetDialogFragment<FragmentDialogueLineupBinding>(R.layout.fragment_dialogue_lineup){

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme // 정의한 스타일 사용
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun initObserver() {

    }

    override fun initView() {

    }

}