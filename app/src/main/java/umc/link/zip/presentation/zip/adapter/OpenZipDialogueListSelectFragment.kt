package umc.link.zip.presentation.zip.adapter

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.databinding.FragmentDialogueLineupBinding
import umc.link.zip.databinding.FragmentDialogueListselectBinding
import umc.link.zip.presentation.base.BaseBottomSheetDialogFragment
import umc.link.zip.presentation.list.ListUnreadListDialogSharedViewModel

@AndroidEntryPoint
class OpenZipDialogueListSelectFragment : BaseBottomSheetDialogFragment<FragmentDialogueListselectBinding>(R.layout.fragment_dialogue_listselect){

    private val OpenZipListDialogSharedViewModel: OpenZipListDialogSharedViewModel by activityViewModels()

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun initObserver() {}

    override fun initView() {
        setStart()
        setOnClick()
    }

    private fun setStart() {
        // 기존 데이터 가져오기 위함
        reset()
        lifecycleScope.launch {
            OpenZipListDialogSharedViewModel.selectedData.collectLatest {
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
            lifecycleScope.launch {
                reset()
                selected(binding.tvDialogueListselectItem1, binding.ivDialogueListselectChkAll)
                OpenZipListDialogSharedViewModel.setSelectedData("all")
            }
        }
        binding.clDialogueListselectItem2.setOnClickListener {
            lifecycleScope.launch {
                reset()
                selected(binding.tvDialogueListselectItem2, binding.ivDialogueListselectChkLink)
                OpenZipListDialogSharedViewModel.setSelectedData("link")
            }
        }
        binding.clDialogueListselectItem3.setOnClickListener {
            lifecycleScope.launch {
                reset()
                selected(binding.tvDialogueListselectItem3, binding.ivDialogueListselectChkText)
                OpenZipListDialogSharedViewModel.setSelectedData("text")
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
        binding.tvDialogueListselectItem1.setTextColor(ContextCompat.getColor(binding.root.context, R.color.nav_selected))
        binding.tvDialogueListselectItem2.setTextColor(ContextCompat.getColor(binding.root.context, R.color.nav_selected))
        binding.tvDialogueListselectItem3.setTextColor(ContextCompat.getColor(binding.root.context, R.color.nav_selected))
        binding.ivDialogueListselectChkAll.visibility = View.INVISIBLE
        binding.ivDialogueListselectChkLink.visibility = View.INVISIBLE
        binding.ivDialogueListselectChkText.visibility = View.INVISIBLE
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        lifecycleScope.launch {
            OpenZipListDialogSharedViewModel.dismissDialog()
        }
    }
}