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
import umc.link.zip.data.dto.link.request.LinkAddRequest
import umc.link.zip.data.dto.link.request.LinkSummaryRequest
import umc.link.zip.databinding.FragmentCustomlinkCustomBinding
import umc.link.zip.domain.model.create.CreateLink
import umc.link.zip.domain.model.link.LinkAddModel
import umc.link.zip.domain.model.link.LinkExtractModel
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.create.adapter.CreateViewModel
import umc.link.zip.presentation.create.adapter.LinkAddViewModel
import umc.link.zip.presentation.create.adapter.LinkExtractViewModel
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.network.UiState

@AndroidEntryPoint
class CustomlinkCustomFragment :
    BaseFragment<FragmentCustomlinkCustomBinding>(R.layout.fragment_customlink_custom) {

    private val linkAddViewModel: LinkAddViewModel by activityViewModels()
    private val linkExtractViewModel: LinkExtractViewModel by activityViewModels()

    private var linkId: Int? = null

    override fun initObserver() {

        // 제목, 썸네일 API 응답
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                linkExtractViewModel.extractResponse.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> {
                            // 로딩 상태 처리
                            Log.d("CustomlinkCustomFragment", "Loading data")
                        }

                        is UiState.Success<*> -> {
                            val data = state.data as LinkExtractModel
                            // 제목
                            binding.etCustomLinkCustomLinkTitle.setText(data.title ?: "제목 없음")
                            // 썸네일
                            val thumbnailUrl = data.thumb
                            if (thumbnailUrl == null) {
                                binding.ivCustomLinkCustomTopImg.setImageResource(R.drawable.iv_link_thumbnail_default)
                            } else {
                                Glide.with(binding.ivCustomLinkCustomTopImg.context)
                                    .load(thumbnailUrl)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.ivCustomLinkCustomTopImg)
                            }
                            Log.d("CustomlinkCustomFragment", "제목/썸네일 가져오기 성공")

                        }

                        is UiState.Error -> {
                            Toast.makeText(requireContext(), "제목/썸네일 추출 실패", Toast.LENGTH_SHORT)
                                .show()
                            Log.d("CustomlinkCustomFragment", "제목/썸네일 가져오기 실패")
                        }

                        UiState.Empty -> Log.d("CustomlinkCustomFragment", "isEmpty")
                    }
                }
            }
        }
    }

    override fun initView() {
        setOnClickListener()

        binding.etCustomLinkCustomLinkTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                linkAddViewModel.updateLinkInput(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setOnClickListener() {
        binding.ivCustomLinkCustomToolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // 메모 커스텀 버튼
        binding.btnCustomLinkCustomMemo.setOnClickListener {
            handleSaveAndNavigate(::navigateToMemo)
        }

        // 알림 커스텀 버튼
        binding.btnCustomLinkCustomAlarm.setOnClickListener {
            handleSaveAndNavigate(::navigateToAlarm)
        }

        // 저장 버튼
        binding.btnCustomLinkCustomSave.setOnClickListener {
            handleSaveAndNavigate(::navigateToOpenLink)
        }

        // Delete 버튼
        binding.ivCustomLinkCustomDelete.setOnClickListener {
            linkAddViewModel.clearLinkInput()
            binding.etCustomLinkCustomLinkTitle.text.clear() // EditText의 내용을 직접 초기화
        }
    }

    private fun handleSaveAndNavigate(navigateAction: () -> Unit) {
        val zipId = 99 // 임시
        val updatedTitle = binding.etCustomLinkCustomLinkTitle.text.toString()
        val memoText = linkAddViewModel.link.value.memo
        val text = linkAddViewModel.link.value.text
        val url = linkAddViewModel.link.value.url
        val alertDate = linkAddViewModel.link.value.alertDate.toString()

        if (updatedTitle.isEmpty()) {
            // 제목이 비어있으면 토스트 메시지 표시
            Toast.makeText(requireContext(), "제목을 설정해주세요", Toast.LENGTH_SHORT).show()
        } else {
            // 제목이 비어있지 않으면 ViewModel에 제목 저장하고 이동
            linkAddViewModel.updateTitle(updatedTitle)

            // ADD API 호출
            val linkAddRequest = LinkAddRequest(
                zip_id = zipId,
                title = updatedTitle,
                memo = memoText,
                text = text,
                url = url,
                alert_date = alertDate
            )
            linkAddViewModel.addLink(linkAddRequest)
            Log.d("CustomlinkCustomFragment", "ADD API 호출")

            // ADD API 응답 후 이동 (linkId 설정)
            repeatOnStarted {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    linkAddViewModel.addResponse.collectLatest { state ->
                        when (state) {
                            is UiState.Loading -> {
                                // 로딩 상태 처리
                                Log.d("CustomlinkCustomFragment", "Loading data")
                            }
                            is UiState.Success<*> -> {
                                val data = state.data as LinkAddModel
                                linkId = data.link_id  // 응답으로 받은 링크 ID 설정

                                // 링크 ID가 존재할 경우 다음 화면으로 이동
                                navigateAction()
                            }

                            is UiState.Error -> {
                                Toast.makeText(requireContext(), "링크 추가 실패", Toast.LENGTH_SHORT)
                                    .show()
                                Log.d("CustomlinkCustomFragment", "링크 추가 실패")
                            }

                            UiState.Empty -> Log.d("CustomlinkCustomFragment", "isEmpty")
                        }
                    }
                }
            }

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
        linkId?.let { id ->
            val action =
                CustomlinkCustomFragmentDirections.actionCustomlinkCustomFragmentToOpenLinkFragment(
                    id
                )
            Log.d("CustomlinkCustomFragment", "linkId: $id")
            findNavController().navigate(action)
        } ?: run {
            Log.d("CustomlinkCustomFragment", "linkId 가져오기 실패")
        }
    }

}
