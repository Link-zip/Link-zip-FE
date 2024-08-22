package umc.link.zip.presentation.zip.adapter

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.databinding.FragmentDeletelinkBinding
import umc.link.zip.databinding.FragmentDeletezipBinding
import umc.link.zip.databinding.FragmentDialogueLineupBinding
import umc.link.zip.presentation.base.BaseBottomSheetDialogFragment
import umc.link.zip.presentation.base.BaseDialogFragment
import umc.link.zip.presentation.zip.OnDialogDismissListener
import umc.link.zip.util.extension.repeatOnStarted

@AndroidEntryPoint
class LinkDeleteDialogueFragment : BaseDialogFragment<FragmentDeletelinkBinding>(R.layout.fragment_deletelink) {

    private val linkDeleteViewModel: LinkDeleteViewModel by viewModels(ownerProducer = { requireParentFragment() })
    private var selectedLinkIds: List<Int> = emptyList()

    var dismissListener: OnDialogDismissListener? = null


    companion object {
        private const val ARG_SELECTED_IDS = "selected_ids"

        fun newInstance(selectedIds: List<Int>): LinkDeleteDialogueFragment {
            val fragment = LinkDeleteDialogueFragment()
            val args = Bundle()
            args.putIntegerArrayList(ARG_SELECTED_IDS, ArrayList(selectedIds))
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener?.onDialogDismiss()  // dismiss 이벤트 발생 시 인터페이스 메서드 호출
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedLinkIds = arguments?.getIntegerArrayList(ARG_SELECTED_IDS)?.toList() ?: emptyList()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        // 다이얼로그 배경을 투명하게 설정
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 다이얼로그 크기를 wrap_content로 설정하여 중앙에 위치하도록 설정
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return dialog
    }

    override fun initObserver() {
        setOnClick()
    }

    override fun initView() {
        Log.d("DeleteDialogueFragment", "DeleteDialogueFragment instance")
    }

    private fun setOnClick() {
        binding.fragmentDeletezipDeleteBtn.setOnClickListener {
            repeatOnStarted {
                selectedLinkIds.forEach { id ->
                    linkDeleteViewModel.deleteLink(id)
                }
                Log.d("Delete Dialog", "Delete Dialog : Yes, IDs: $selectedLinkIds")
            }
            // 다이얼로그를 닫음
            dismiss()
        }

        binding.fragmentDeletezipCancleBtn.setOnClickListener {
            // 다이얼로그 닫기
            dismiss()
        }
    }
}

