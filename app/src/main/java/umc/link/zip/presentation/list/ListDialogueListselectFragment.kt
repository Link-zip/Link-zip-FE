package umc.link.zip.presentation.list

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.databinding.FragmentDialogueListselectBinding
import umc.link.zip.presentation.base.BaseBottomSheetDialogFragment
import umc.link.zip.util.extension.colorOf
import umc.link.zip.util.extension.repeatOnStarted

@AndroidEntryPoint
class ListDialogueListselectFragment : BaseBottomSheetDialogFragment<FragmentDialogueListselectBinding> (R.layout.fragment_dialogue_listselect){

    private val listUnreadListDialogSharedViewModel: ListUnreadListDialogSharedViewModel by viewModels({requireParentFragment()})

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun initObserver() {
        setStart()
        setOnClick()
    }

    override fun initView() {}

    private fun setStart() {
        // 기존 데이터 가져오기 위함
        reset()
        repeatOnStarted {
            listUnreadListDialogSharedViewModel.selectedData.collectLatest {
                    data ->
                when (data) {
                    "all" -> {
                        selected(binding.tvDialogueListselectItem1, binding.ivDialogueListselectChkAll)
                    }
                    "link" -> {
                        selected(binding.tvDialogueListselectItem2, binding.ivDialogueListselectChkLink)
                    }
                    "text" -> {
                        selected(binding.tvDialogueListselectItem3, binding.ivDialogueListselectChkText)
                    }
                }
            }
        }
    }

    private fun setOnClick() {
        binding.clDialogueListselectItem1.setOnClickListener {
            repeatOnStarted {
                reset()
                selected(binding.tvDialogueListselectItem1, binding.ivDialogueListselectChkAll)
                listUnreadListDialogSharedViewModel.setSelectedData("all")
            }
        }
        binding.clDialogueListselectItem2.setOnClickListener {
            repeatOnStarted {
                reset()
                selected(binding.tvDialogueListselectItem2, binding.ivDialogueListselectChkLink)
                listUnreadListDialogSharedViewModel.setSelectedData("link")
            }
        }
        binding.clDialogueListselectItem3.setOnClickListener {
            repeatOnStarted {
                reset()
                selected(binding.tvDialogueListselectItem3, binding.ivDialogueListselectChkText)
                listUnreadListDialogSharedViewModel.setSelectedData("text")
            }
        }
        binding.ivDialogueListselectClose.setOnClickListener {
            dismiss()
        }
    }

    private fun selected(tv: TextView, view: View) {
        tv.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
        view.visibility = View.VISIBLE
    }

    private fun reset() {
        with(binding){
            tvDialogueListselectItem1.setTextColor(
                colorOf(R.color.nav_selected)
            )
            tvDialogueListselectItem2.setTextColor(
                colorOf(R.color.nav_selected)
            )
            tvDialogueListselectItem3.setTextColor(
                colorOf(R.color.nav_selected)
            )
            ivDialogueListselectChkAll.visibility = View.INVISIBLE
            ivDialogueListselectChkLink.visibility = View.INVISIBLE
            ivDialogueListselectChkText.visibility = View.INVISIBLE
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        repeatOnStarted {
            listUnreadListDialogSharedViewModel.dismissDialog()
        }
    }
}