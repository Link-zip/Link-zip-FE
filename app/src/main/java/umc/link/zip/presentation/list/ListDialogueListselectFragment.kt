package umc.link.zip.presentation.list

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import umc.link.zip.R
import umc.link.zip.databinding.FragmentDialogueListselectBinding
import umc.link.zip.presentation.base.BaseBottomSheetDialogFragment

class ListDialogueListselectFragment : BaseBottomSheetDialogFragment<FragmentDialogueListselectBinding> (R.layout.fragment_dialogue_listselect){

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme // 정의한 스타일 사용
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme)

    }

    override fun initObserver() {

    }

    override fun initView() {

    }

 //각 버튼 클릭시 움직이고 적용되는 거 구현 필요



}