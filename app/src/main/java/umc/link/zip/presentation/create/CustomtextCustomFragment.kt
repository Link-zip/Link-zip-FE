package umc.link.zip.presentation.create

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.data.dto.link.request.LinkAddRequest
import umc.link.zip.databinding.FragmentCustomtextCustomBinding
import umc.link.zip.domain.model.link.LinkAddModel
import umc.link.zip.domain.model.link.LinkExtractModel
import umc.link.zip.domain.model.link.LinkSummaryModel
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.create.adapter.LinkAddViewModel
import umc.link.zip.presentation.create.adapter.LinkExtractViewModel
import umc.link.zip.presentation.create.adapter.LinkSummaryViewModel
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.network.UiState

@AndroidEntryPoint
class CustomtextCustomFragment : BaseFragment<FragmentCustomtextCustomBinding>(R.layout.fragment_customtext_custom){

    private val linkAddViewModel: LinkAddViewModel by activityViewModels()
    private val linkSummaryViewModel: LinkSummaryViewModel by activityViewModels()
    private val linkExtractViewModel: LinkExtractViewModel by activityViewModels()

    private var linkId: Int? = null

    private var setTitle: String? = null
    private var updateTitle: String? = null

    private var setSummary: String? = null
    private var updateSummary: String? = null

    private var isSuccess: Boolean = false
    private var isUpdated: Boolean = true


    override fun initObserver() {
        // text 요약 API 응답
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                linkSummaryViewModel.summaryResponse.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> {
                            // 로딩 상태 처리
                            Log.d("CustomtextCustomFragment", "Loading 텍스트 요약")
                        }

                        is UiState.Success<*> -> {
                            val data = state.data as LinkSummaryModel
                            // 요약
                            setSummary = data.summary
                            Log.d("CustomtextCustomFragment", "setSummary: $setSummary")
                        }

                        is UiState.Error -> {
                            Toast.makeText(requireContext(), "요약 가져오기 실패", Toast.LENGTH_SHORT).show()
                            Log.d("CustomtextCustomFragment", "텍스트 요약 가져오기 실패")
                        }

                        UiState.Empty -> Log.d("CustomtextCustomFragment", "텍스트 요약 isEmpty")
                    }
                }
            }
        }

        // 제목 API 응답 (썸네일 사용 x)
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                linkExtractViewModel.extractResponse.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> {
                            // 로딩 상태 처리
                            Log.d("CustomtextCustomFragment", "Loading 제목")
                        }

                        is UiState.Success<*> -> {
                            val data = state.data as LinkExtractModel
                            // 제목
                            setTitle = data.title
                            Log.d("CustomtextCustomFragment", "setTitle: $setTitle")
                        }

                        is UiState.Error -> {
                            Toast.makeText(requireContext(), "제목 가져오기 실패", Toast.LENGTH_SHORT).show()
                            Log.d("CustomtextCustomFragment", "제목 가져오기 실패")
                        }

                        UiState.Empty -> Log.d("CustomtextCustomFragment", "제목 isEmpty")
                    }
                }
            }
        }
        // 제목 설정
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                linkAddViewModel.link.collectLatest { link ->
                    if (isUpdated == true) {

                        updateTitle = link.title
                        Log.d("CustomtextCustomFragment", "updateTitle: $updateTitle")
                        if (updateTitle == "default") {
                            binding.etCustomTextCustomLinkTitle.setText(setTitle)
                            Log.d("CustomtextCustomFragment", "제목 설정: $setTitle")
                        } else {
                            binding.etCustomTextCustomLinkTitle.setText(updateTitle)
                            Log.d("CustomtextCustomFragment", "제목 설정: $updateTitle")
                        }
                        isUpdated = false
                    }
                }
            }
        }
        // 텍스트 요약 설정
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                linkAddViewModel.link.collectLatest { link ->
                    updateSummary = link.text
                    Log.d("CustomtextCustomFragment", "updateSummary: $updateSummary")
                    if (updateSummary == "default"){
                        binding.etCustomTextSummaryText.setText(setSummary)
                        Log.d("CustomtextCustomFragment", "텍스트 요약 설정: $setSummary")
                    } else {
                        binding.etCustomTextSummaryText.setText(updateSummary)
                        Log.d("CustomtextCustomFragment", "텍스트 요약 설정: $updateSummary")
                    }
                }
            }
        }
    }

    override fun initView() {
        setOnClickListener()
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
            handleSaveAndNavigate(::navigateToOpenText)
        }

        // Delete 버튼
        binding.ivCustomTextCustomDelete.setOnClickListener {
            linkAddViewModel.clearLinkInput()
            binding.etCustomTextCustomLinkTitle.text.clear() // EditText의 내용을 직접 초기화
        }
    }

    private fun handleSaveAndNavigate(navigateAction: () -> Unit) {
        isSuccess = false

        updateTitle = binding.etCustomTextCustomLinkTitle.text.toString()
        updateSummary = binding.etCustomTextSummaryText.text.toString()

        linkAddViewModel.updateText(text = updateSummary!!)
        linkAddViewModel.updateTitle(title = updateTitle!!)
        Log.d("CustomtextCustomFragment", "updateTitle: $updateTitle\nupdateSummary: $updateSummary")

        if (!isSuccess) {
            isSuccess = true
            if (updateTitle!!.isEmpty()) {
                // 제목이 비어있으면 토스트 메시지 표시
                Toast.makeText(requireContext(), "제목을 설정해주세요", Toast.LENGTH_SHORT).show()
            } else {
                // 제목이 비어있지 않으면 이동
                navigateAction()
            }
        }
    }

    private fun navigateToMemo() {
        findNavController().navigate(R.id.action_customtextCustomFragment_to_customtextMemoFragment)
    }

    private fun navigateToAlarm() {
        findNavController().navigate(R.id.action_customtextCustomFragment_to_customtextAlarmFragment)
    }

    private fun navigateToOpenText() {
        val zipId = 209 // 임시
        updateTitle = binding.etCustomTextCustomLinkTitle.text.toString()
        val memoText = linkAddViewModel.link.value.memo
        updateSummary = binding.etCustomTextSummaryText.text.toString()
        val url = linkAddViewModel.link.value.url
        val alertDate = linkAddViewModel.link.value.alertDate.toString()

        // ADD API 호출
        val linkAddRequest = LinkAddRequest(
            zip_id = zipId,
            title = updateTitle!!,
            memo = memoText,
            text = updateSummary,
            url = url,
            alert_date = alertDate
        )
        linkAddViewModel.addLink(linkAddRequest)
        Log.d("CustomtextCustomFragment", "ADD API 호출\nzip_id=${zipId}\ntitle=${updateTitle}\nmemo=${memoText}\ntext=${updateSummary}\nurl=${url}\nalert_date=${alertDate}")

        // ADD API 응답 후 이동 (linkId 설정)
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                linkAddViewModel.addResponse.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> {
                            // 로딩 상태 처리
                            Log.d("CustomtextCustomFragment", "Loading ADD data")
                        }
                        is UiState.Success<*> -> {
                            val data = state.data as LinkAddModel
                            linkId = data.link_id  // 응답으로 받은 링크 ID 설정
                            Log.d("CustomtextCustomFragment", "링크 추가 성공 linkId: $linkId")

                            // 성공적으로 linkId를 받았을 때 화면 이동
                            linkId?.let { id ->
                                val action =
                                    CustomtextCustomFragmentDirections.actionCustomtextCustomFragmentToOpenTextFragment(
                                        id, true
                                    )
                                Log.d("CustomlinkCustomFragment", "linkId: $id")
                                findNavController().navigate(action)
                            } ?: run {
                                Log.d("CustomtextCustomFragment", "linkId 가져오기 실패")
                            }
                        }

                        is UiState.Error -> {
                            Toast.makeText(requireContext(), "링크 추가 실패", Toast.LENGTH_SHORT)
                                .show()
                            Log.d("CustomtextCustomFragment", "링크 추가 실패")
                        }

                        UiState.Empty -> Log.d("CustomtextCustomFragment", "isEmpty")
                    }
                }
            }
        }
    }
}