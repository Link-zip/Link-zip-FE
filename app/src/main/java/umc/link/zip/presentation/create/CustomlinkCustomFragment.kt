package umc.link.zip.presentation.create

import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomlinkCustomBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.util.extension.repeatOnStarted
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CustomlinkCustomFragment : BaseFragment<FragmentCustomlinkCustomBinding>(R.layout.fragment_customlink_custom){

    private val viewModel: CreateViewModel by activityViewModels()

    override fun initObserver() {
        repeatOnStarted {
            viewModel.link.collectLatest { link ->
                // EditText 제목 설정
                binding.etCustomLinkCustomLinkTitle.setText(link.title ?: "설정된 제목이 없습니다.")
            }
        }
    }

    override fun initView() {
        setOnClickListener()

        binding.etCustomLinkCustomLinkTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateLinkInput(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setOnClickListener(){
        binding.ivCustomLinkCustomToolbarBack.setOnClickListener{
            findNavController().navigateUp()
        }

        // 메모 커스텀 버튼
        binding.btnCustomLinkCustomMemo.setOnClickListener{
            handleSaveAndNavigate(::navigateToMemo)
        }

        // 알림 커스텀 버튼
        binding.btnCustomLinkCustomAlarm.setOnClickListener{
            handleSaveAndNavigate(::navigateToAlarm)
        }

        // 저장 버튼
        binding.clCustomLinkCustomSaveBtn.setOnClickListener {
            handleSaveAndNavigate(::navigateToOpenLink)
        }

        // Delete 버튼
        binding.ivCustomLinkCustomDelete.setOnClickListener {
            viewModel.clearLinkInput()
            binding.etCustomLinkCustomLinkTitle.text.clear() // EditText의 내용을 직접 초기화
        }
    }

    private fun handleSaveAndNavigate(navigateAction: () -> Unit) {
        val updatedTitle = binding.etCustomLinkCustomLinkTitle.text.toString()
        if (updatedTitle.isEmpty()) {
            // 제목이 비어있으면 토스트 메시지 표시
            Toast.makeText(requireContext(), "제목을 설정해주세요", Toast.LENGTH_SHORT).show()
        } else {
            // 제목이 비어있지 않으면 ViewModel에 제목 저장하고 이동
            viewModel.updateTitle(updatedTitle)
            navigateAction()
        }
    }

    private fun navigateToMemo() {
        findNavController().navigate(R.id.action_customlinkCustomFragment_to_customlinkMemoFragment)
    }

    private fun navigateToAlarm() {
        findNavController().navigate(R.id.action_customlinkCustomFragment_to_customlinkAlarmFragment)
    }

    private fun navigateToOpenLink() {
        findNavController().navigate(R.id.action_customlinkCustomFragment_to_openLinkFragment)
    }
}
