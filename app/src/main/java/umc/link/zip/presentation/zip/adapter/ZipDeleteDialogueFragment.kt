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
import umc.link.zip.data.dto.zip.request.ZipRmRequest
import umc.link.zip.databinding.FragmentDeletezipBinding
import umc.link.zip.databinding.FragmentDialogueLineupBinding
import umc.link.zip.presentation.base.BaseBottomSheetDialogFragment
import umc.link.zip.presentation.base.BaseDialogFragment
import umc.link.zip.util.extension.repeatOnStarted

@AndroidEntryPoint
class ZipDeleteDialogueFragment : BaseDialogFragment<FragmentDeletezipBinding>(R.layout.fragment_deletezip)
{
    private val ZipDeleteViewModel: ZipDeleteViewModel by viewModels(ownerProducer = {requireParentFragment()})

    override fun getTheme(): Int {
        return androidx.appcompat.R.style.Theme_AppCompat_Dialog
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
                //ZipDeleteViewModel.deleteZip(ZipRmRequest())
                Log.d("Delete Dialog", "Delete Dialog : Yes")
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
