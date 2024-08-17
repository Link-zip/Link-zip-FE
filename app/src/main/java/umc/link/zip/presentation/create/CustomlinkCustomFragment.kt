package umc.link.zip.presentation.create

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomlinkCustomBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.create.adapter.LinkExtractViewModel
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.network.NetworkResult

@AndroidEntryPoint
class CustomlinkCustomFragment : BaseFragment<FragmentCustomlinkCustomBinding>(R.layout.fragment_customlink_custom){

    private val viewModel: CreateViewModel by activityViewModels()
    private val linkExtractViewModel: LinkExtractViewModel by activityViewModels()

    override fun initObserver() {
        repeatOnStarted {
            viewModel.link.collectLatest { link ->
                // EditText 제목 설정
                binding.etCustomLinkCustomLinkTitle.setText(link.title ?: "설정된 제목이 없습니다.")
            }
        }

        // 제목, 썸네일 API 받아옴
        repeatOnStarted {
            linkExtractViewModel.extractResponse.collectLatest { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val linkExtractModel = result.data
                        // 제목
                        binding.etCustomLinkCustomLinkTitle.setText(linkExtractModel.title ?: "제목 없음")
                        // 썸네일
                        Glide.with(this@CustomlinkCustomFragment)
                            .load(linkExtractModel.thumb)
                            .placeholder(R.drawable.clip) // 로딩 중 placeholder 이미지 (선택 사항)
                            .error(R.drawable.clip) // 로딩 실패 시 error 이미지 (선택 사항)
                            .into(binding.ivCustomLinkCustomTopImg)
                        Log.d("CustomtextCustomFragment", "제목/썸네일 가져오기 성공")
                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(requireContext(), "링크 추출 실패", Toast.LENGTH_SHORT).show()
                        Log.d("CustomtextCustomFragment", "제목/썸네일 가져오기 실패")
                    }
                    else -> {
                        // 로딩 상태 또는 기타 상태 처리
                    }
                }
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
        binding.btnCustomLinkCustomSave.setOnClickListener {
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
