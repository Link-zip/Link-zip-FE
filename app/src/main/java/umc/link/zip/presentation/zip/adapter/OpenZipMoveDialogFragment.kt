package umc.link.zip.presentation.zip.adapter

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.databinding.FragmentDialogueLineupBinding
import umc.link.zip.databinding.FragmentDialogueMoveOpenzipBinding
import umc.link.zip.domain.model.link.LinkGetModel
import umc.link.zip.domain.model.link.MoveLinkToNewZipModel
import umc.link.zip.domain.model.zip.ZipGetItemModel
import umc.link.zip.domain.model.zip.ZipGetModel
import umc.link.zip.domain.repository.ZipRepository
import umc.link.zip.presentation.base.BaseBottomSheetDialogFragment
import umc.link.zip.presentation.zip.OnDialogDismissListener
import umc.link.zip.presentation.zip.ZipFragmentDirections
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.network.UiState
import umc.link.zip.util.network.onSuccess
import javax.inject.Inject

@AndroidEntryPoint
class OpenZipMoveDialogFragment : BaseBottomSheetDialogFragment<FragmentDialogueMoveOpenzipBinding>(R.layout.fragment_dialogue_move_openzip){

    var dismissListener: OnDialogDismissListener? = null

    private val openZipMoveDialogSharedViewModel: OpenZipMoveDialogSharedViewModel by viewModels(ownerProducer = {requireParentFragment()})
    private val sharedViewModel : OpenZipMoveDialogSharedViewModel by viewModels()

    private val zipId by lazy { arguments?.getInt(ARG_ZIP_ID) ?: 0 }
    private val linkId by lazy { arguments?.getInt(ARG_LINK_ID) ?: 0 }

    companion object {
        private const val ARG_ZIP_ID = "zip_id"
        private const val ARG_LINK_ID = "link_id"

        fun newInstance(zipId: Int, linkId : Int): OpenZipMoveDialogFragment {
            val fragment = OpenZipMoveDialogFragment()
            val args = Bundle().apply {
                putInt(ARG_ZIP_ID, zipId)
                putInt(ARG_LINK_ID, linkId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private val adapter by lazy {
        OpenZipMoveDialogAdapter(
            onItemSelected = { zipItem, isSelected ->
                if (isSelected) {
                    binding.fragmentMakezipMakeBtn.setBackgroundResource(R.drawable.shape_rect_005773_fill)
                    binding.ivProfilesetGrayshadow.visibility = View.GONE
                    binding.ivProfilesetBlueshadow.visibility = View.VISIBLE
                    //navigate 함수 호출 필요
                    navigateToOpenZip(zipItem.zip_id)

                } else {
                    binding.fragmentMakezipMakeBtn.setBackgroundResource(R.drawable.shape_rect_666666_fill)
                    binding.ivProfilesetGrayshadow.visibility = View.VISIBLE
                    binding.ivProfilesetBlueshadow.visibility = View.GONE

                }
            }
        )
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 다이얼로그 dismiss 리스너 설정
        dialog.setOnDismissListener {
            dismissListener?.onDialogDismiss()
        }

        return dialog
    }

    override fun initObserver() {
        // StateFlow를 관찰하여 RecyclerView Adapter에 데이터를 전달
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.uiState.collectLatest { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            // 로딩 상태 처리
                            Log.d("OpenZipFragment", "Loading data")
                        }

                        is UiState.Success<*> -> {
                            val data = uiState.data as ZipGetModel
                            adapter.submitList(data.zips, excludeZipId = zipId)
                            Log.d("OpenZipFragment", "Fetched data size: ${data.zips}")
                        }

                        is UiState.Error -> {
                            // 에러 상태 처리
                            Log.e("OpenZipFragment", "Error fetching data", uiState.error)
                        }

                        UiState.Empty -> {
                            Log.e("OpenZipFragment", "UiState.Empty")
                        }
                    }
                }
            }
        }

        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.uiState_link.collectLatest { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            // 로딩 상태 처리
                            Log.d("OpenZipFragment", "Loading data")
                        }

                        is UiState.Success<*> -> {
                            Log.d("OpenZipMoveDialogFragment", "Dismiss()")
                            dismiss()
                        }

                        is UiState.Error -> {
                            // 에러 상태 처리
                            Log.e("OpenZipFragment", "Error fetching data", uiState.error)
                        }

                        UiState.Empty -> {
                            Log.e("OpenZipFragment", "UiState.Empty")
                        }
                    }
                }
            }
        }
    }

    override fun initView() {
        Log.d("DialogOpenZip", "OpenZipMoveDialogSharedViewModel instance: $openZipMoveDialogSharedViewModel")
        binding.fragmentZipRecyclerview.adapter = adapter
        sharedViewModel.getZipList("latest")
    }

    override fun onDismiss(dialog: DialogInterface) {
        repeatOnStarted {
            dismissListener?.onDialogDismiss()  // 콜백 호출
            openZipMoveDialogSharedViewModel.dismissDialog()
            sharedViewModel.getZipList("latest")
        }
        dismissListener?.onDialogDismiss()  // 콜백 호출
        super.onDismiss(dialog)
    }

    private fun navigateToOpenZip(zipId: Int)
    {
        binding.fragmentMakezipMakeBtn.setOnClickListener {
            sharedViewModel.moveLinkToNewZip(linkId, zipId)
        }
    }

}
