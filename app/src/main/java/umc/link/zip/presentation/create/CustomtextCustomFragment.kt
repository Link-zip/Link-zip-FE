package umc.link.zip.presentation.create

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomtextCustomBinding
import umc.link.zip.domain.model.link.LinkExtractModel
import umc.link.zip.domain.model.link.LinkSummaryModel
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.create.adapter.LinkExtractViewModel
import umc.link.zip.presentation.create.adapter.LinkSummaryViewModel
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.UiState

@AndroidEntryPoint
class CustomtextCustomFragment : BaseFragment<FragmentCustomtextCustomBinding>(R.layout.fragment_customtext_custom){

    private val createViewModel: CreateViewModel by activityViewModels()
    private val linkSummaryViewModel: LinkSummaryViewModel by activityViewModels()
    private val linkExtractViewModel: LinkExtractViewModel by activityViewModels()

    override fun initObserver() {
        // text 요약 API
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                linkSummaryViewModel.summaryResponse.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> {
                            // 로딩 상태 처리
                            Log.d("CustomtextCustomFragment", "Loading text summary data")
                        }

                        is UiState.Success<*> -> {
                            val data = state.data as LinkSummaryModel
                            // 요약
                            binding.etCustomTextSummaryText.setText(data.summary ?: "요약이 없습니다.")
                            Log.d("CustomtextCustomFragment", "텍스트 요약 가져오기 성공")

                        }

                        is UiState.Error -> {
                            Toast.makeText(requireContext(), "요약 가져오기 실패", Toast.LENGTH_SHORT).show()
                            Log.d("CustomtextCustomFragment", "텍스트 요약 가져오기 실패")
                        }

                        UiState.Empty -> Log.d("CustomtextCustomFragment", "isEmpty")
                    }
                }
            }
        }

        // 제목 API
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                linkExtractViewModel.extractResponse.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> {
                            // 로딩 상태 처리
                            Log.d("CustomtextCustomFragment", "Loading data")
                        }

                        is UiState.Success<*> -> {
                            val data = state.data as LinkExtractModel
                            // 제목
                            binding.etCustomTextCustomLinkTitle.setText(data.title ?: "제목 없음")
                            Log.d("CustomtextCustomFragment", "제목 가져오기 성공")
                        }

                        is UiState.Error -> {
                            Toast.makeText(requireContext(), "제목 가져오기 실패", Toast.LENGTH_SHORT).show()
                            Log.d("CustomtextCustomFragment", "제목 가져오기 실패")
                        }

                        UiState.Empty -> Log.d("CustomtextCustomFragment", "isEmpty")
                    }
                }
            }
        }

        repeatOnStarted {
            createViewModel.link.collectLatest { link ->
                binding.etCustomTextCustomLinkTitle.setText(link.title ?: "설정된 제목이 없습니다.")
                binding.etCustomTextSummaryText.setText(link.text ?: "텍스트 요약이 없습니다.")
            }
        }
    }

    override fun initView() {
        setOnClickListener()

        binding.etCustomTextCustomLinkTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                createViewModel.updateLinkInput(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etCustomTextSummaryText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                createViewModel.updateLinkInput(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setOnClickListener(){
        binding.ivCustomTextCustomToolbarBack.setOnClickListener{
            findNavController().navigateUp()
        }

        // 메모 커스텀 버튼
        binding.btnCustomTextCustomMemo.setOnClickListener{
            handleSaveAndNavigate(::navigateToMemo)
        }

        // 알림 커스텀 버튼
        binding.btnCustomTextCustomAlarm.setOnClickListener{
            handleSaveAndNavigate(::navigateToAlarm)
        }

        // 저장 버튼
        binding.btnCustomTextCustomSave.setOnClickListener {
            handleSaveAndNavigate(::navigateToOpenLink)
        }

        // Delete 버튼
        binding.ivCustomTextCustomDelete.setOnClickListener {
            createViewModel.clearLinkInput()
            binding.etCustomTextCustomLinkTitle.text.clear() // EditText의 내용을 직접 초기화
        }
    }

    private fun handleSaveAndNavigate(navigateAction: () -> Unit) {
        val updatedTitle = binding.etCustomTextCustomLinkTitle.text.toString()
        val updatedText = binding.etCustomTextSummaryText.text.toString()

        if (updatedTitle.isEmpty()) {
            // 제목이 비어있으면 토스트 메시지 표시
            Toast.makeText(requireContext(), "제목을 설정해주세요", Toast.LENGTH_SHORT).show()
        } else {
            // 제목이 비어있지 않으면 ViewModel에 제목 저장하고 이동
            createViewModel.updateTitle(updatedTitle)
            createViewModel.updateText(updatedText)
            navigateAction()
        }
    }

    private fun navigateToMemo() {
        findNavController().navigate(R.id.action_customtextCustomFragment_to_customtextMemoFragment)
    }

    private fun navigateToAlarm() {
        findNavController().navigate(R.id.action_customtextCustomFragment_to_customtextAlarmFragment)
    }

    private fun navigateToOpenLink() {
        findNavController().navigate(R.id.action_customtextCustomFragment_to_openTextFragment)
    }
}