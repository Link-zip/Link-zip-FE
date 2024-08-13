package umc.link.zip.presentation.zip.adapter

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.databinding.FragmentDialogueLineupBinding
import umc.link.zip.databinding.FragmentDialogueListselectBinding
import umc.link.zip.presentation.base.BaseBottomSheetDialogFragment
import umc.link.zip.util.extension.repeatOnStarted

@AndroidEntryPoint
class OpenZipDialogueLineupFragment : BaseBottomSheetDialogFragment<FragmentDialogueLineupBinding>(R.layout.fragment_dialogue_lineup){

    private val openZipLineDialogSharedViewModel: OpenZipLineDialogSharedViewModel by viewModels(ownerProducer = {requireParentFragment()})

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

    override fun initView() {
        Log.d("DialogOpenZip", "OpenZipLineDialogSharedViewModel instance: $openZipLineDialogSharedViewModel")
    }

    private fun setStart() {
        // 기존 데이터 가져오기 위함
        reset()
        repeatOnStarted {
            openZipLineDialogSharedViewModel.selectedData.collectLatest {
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
            repeatOnStarted {
                reset()
                selected(binding.tvViewDialogueLineupItem1, binding.ivDialogueLineupChkLatest)
                openZipLineDialogSharedViewModel.setSelectedData("latest")
            }
        }

        binding.clDialogueLineupItem2.setOnClickListener {
            repeatOnStarted {
                reset()
                selected(binding.tvViewDialogueLineupItem2, binding.ivDialogueLineupChkOldest)
                openZipLineDialogSharedViewModel.setSelectedData("oldest")
            }
        }

        binding.clDialogueLineupItem3.setOnClickListener {
            repeatOnStarted {
                reset()
                selected(binding.tvViewDialogueLineupItem3, binding.ivDialogueLineupChkGanada)
                openZipLineDialogSharedViewModel.setSelectedData("ganada")
            }
        }

        binding.clDialogueLineupItem4.setOnClickListener {
            repeatOnStarted {
                reset()
                selected(binding.tvViewDialogueLineupItem4, binding.ivDialogueLineupChkVisit)
                openZipLineDialogSharedViewModel.setSelectedData("visit")
            }
        }
    }

    private fun reset() {
        binding.tvViewDialogueLineupItem1.setTextAppearance(R.style.dialogue_lineup_item_unselected)
        binding.tvViewDialogueLineupItem2.setTextAppearance(R.style.dialogue_lineup_item_unselected)
        binding.tvViewDialogueLineupItem3.setTextAppearance(R.style.dialogue_lineup_item_unselected)
        binding.tvViewDialogueLineupItem4.setTextAppearance(R.style.dialogue_lineup_item_unselected)

        binding.ivDialogueLineupChkLatest.visibility = View.INVISIBLE
        binding.ivDialogueLineupChkOldest.visibility = View.INVISIBLE
        binding.ivDialogueLineupChkGanada.visibility = View.INVISIBLE
        binding.ivDialogueLineupChkVisit.visibility = View.INVISIBLE
    }

    private fun selected(textView: TextView, view: View) {
        textView.setTextAppearance(R.style.dialogue_lineup_item_selected)
        view.visibility = View.VISIBLE
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        repeatOnStarted {
            openZipLineDialogSharedViewModel.dismissDialog()
        }
    }
}
