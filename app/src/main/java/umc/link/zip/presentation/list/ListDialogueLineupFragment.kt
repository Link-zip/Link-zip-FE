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
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.databinding.FragmentDialogueLineupBinding
import umc.link.zip.presentation.base.BaseBottomSheetDialogFragment

@AndroidEntryPoint
class ListDialogueLineupFragment : BaseBottomSheetDialogFragment<FragmentDialogueLineupBinding>(R.layout.fragment_dialogue_lineup) {

    private val listUnreadLineDialogSharedViewModel: ListUnreadLineDialogSharedViewModel by activityViewModels()

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
            listUnreadLineDialogSharedViewModel.selectedData.collectLatest {
                data ->
                when (data) {
                    "latest" -> {
                        selected(binding.tvViewDialogueLineupItem1, binding.ivDialogueLineupChkLatest)
                    }
                    "oldest" -> {
                        selected(binding.tvViewDialogueLineupItem2, binding.ivDialogueLineupChkOldest)
                    }
                    "ganada" -> {
                        selected(binding.tvViewDialogueLineupItem3, binding.ivDialogueLineupChkGanada)
                    }
                    "visit" -> {
                        selected(binding.tvViewDialogueLineupItem4, binding.ivDialogueLineupChkVisit)
                    }
                }
            }
        }
    }

    private fun setOnClick() {
        binding.clDialogueLineupItem1.setOnClickListener {
            lifecycleScope.launch {
                reset()
                selected(binding.tvViewDialogueLineupItem1, binding.ivDialogueLineupChkLatest)
                listUnreadLineDialogSharedViewModel.setSelectedData("latest")
            }
        }
        binding.clDialogueLineupItem2.setOnClickListener {
            lifecycleScope.launch {
                reset()
                selected(binding.tvViewDialogueLineupItem2, binding.ivDialogueLineupChkOldest)
                listUnreadLineDialogSharedViewModel.setSelectedData("oldest")
            }
        }
        binding.clDialogueLineupItem3.setOnClickListener {
            lifecycleScope.launch {
                reset()
                selected(binding.tvViewDialogueLineupItem3, binding.ivDialogueLineupChkGanada)
                listUnreadLineDialogSharedViewModel.setSelectedData("ganada")
            }
        }
        binding.clDialogueLineupItem4.setOnClickListener {
            lifecycleScope.launch {
                reset()
                selected(binding.tvViewDialogueLineupItem4, binding.ivDialogueLineupChkVisit)
                listUnreadLineDialogSharedViewModel.setSelectedData("visit")
            }
        }
        binding.ivDialogueLineupClose.setOnClickListener {
            dismiss()
        }
    }

    private fun selected(tv: TextView, view: View) {
        tv.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
        view.visibility = View.VISIBLE
    }

    private fun reset() {
        binding.tvViewDialogueLineupItem1.setTextColor(ContextCompat.getColor(binding.root.context, R.color.nav_selected))
        binding.tvViewDialogueLineupItem2.setTextColor(ContextCompat.getColor(binding.root.context, R.color.nav_selected))
        binding.tvViewDialogueLineupItem3.setTextColor(ContextCompat.getColor(binding.root.context, R.color.nav_selected))
        binding.tvViewDialogueLineupItem4.setTextColor(ContextCompat.getColor(binding.root.context, R.color.nav_selected))
        binding.ivDialogueLineupChkLatest.visibility = View.INVISIBLE
        binding.ivDialogueLineupChkOldest.visibility = View.INVISIBLE
        binding.ivDialogueLineupChkGanada.visibility = View.INVISIBLE
        binding.ivDialogueLineupChkVisit.visibility = View.INVISIBLE
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        lifecycleScope.launch {
            listUnreadLineDialogSharedViewModel.dismissDialog()
        }
    }
}