package umc.link.zip.presentation.create

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.data.dto.link.request.LinkExtractRequest
import umc.link.zip.data.dto.link.request.LinkSummaryRequest
import umc.link.zip.databinding.FragmentCreateBinding
import umc.link.zip.domain.model.link.LinkExtractModel
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.create.adapter.LinkAddViewModel
import umc.link.zip.presentation.create.adapter.LinkExtractViewModel
import umc.link.zip.presentation.create.adapter.LinkSummaryViewModel
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.network.UiState

@AndroidEntryPoint
class CreateFragment : BaseFragment<FragmentCreateBinding>(R.layout.fragment_create) {

    private val linkAddViewModel: LinkAddViewModel by activityViewModels()
    private val linkSummaryViewModel: LinkSummaryViewModel by activityViewModels()
    private val linkExtractViewModel: LinkExtractViewModel by activityViewModels()

    private val fromZip by lazy {
        arguments?.getString("fromZip")
    }

    private var setTitle: String? = null
    private var setUrl: String? = null

    private var isAvail : Boolean = true


    override fun initObserver() {
        repeatOnStarted {
            linkAddViewModel.isSaveButtonVisible.collectLatest { isVisible ->
                binding.btnCreateSaveLink.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
                binding.ivCreateLinkOvalBlue.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
                binding.tvCreateLinkOvalBlue.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE

                binding.btnCreateSaveText.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
                binding.ivCreateTextOvalBlue.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
                binding.tvCreateTextOvalBlue.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
            }
        }

        repeatOnStarted {
            linkAddViewModel.isLinkIconVisible.collectLatest { isVisible ->
                binding.ivCreateLink.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
            }
        }

        repeatOnStarted {
            linkAddViewModel.isDeleteIconVisible.collectLatest { isVisible ->
                binding.ivCreateDelete.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
            }
        }
        // extract API 응답
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                linkExtractViewModel.extractResponse.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> {
                            // 로딩 상태 처리
                            Log.d("CustomlinkCustomFragment", "Loading extract data")
                        }

                        is UiState.Success<*> -> {
                            val data = state.data as LinkExtractModel
                            setTitle = data.title
                            setUrl = binding.etCreateLink.text.toString()
                            Log.d("CustomlinkCustomFragment", "setTitle: $setTitle setUrl: $setUrl")

                            if(isAvail) {
                                navigateToLink()
                                isAvail = false
                            }
                        }

                        is UiState.Error -> {
                            Toast.makeText(requireContext(), "제목 추출 실패", Toast.LENGTH_SHORT)
                                .show()
                            Log.d("CustomlinkCustomFragment", "제목 가져오기 실패")
                        }

                        UiState.Empty -> Log.d("CustomlinkCustomFragment", "isEmpty")
                    }
                }
            }
        }
    }

    override fun initView() {
        binding.viewModel = linkAddViewModel
        isAvail = false
        linkAddViewModel.resetState()
        if(fromZip!=null){
            binding.etCreateLink.setText(fromZip)
            linkAddViewModel.updateLinkInput(fromZip.toString())
        }

        binding.etCreateLink.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                linkAddViewModel.updateLinkInput(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        setOnClickListener()
    }


    private fun setOnClickListener(){
        // 업버튼
        binding.ivCreateToolbarBack.setOnClickListener {
            linkAddViewModel.clearLinkInput()
            binding.etCreateLink.text.clear() // EditText의 내용 초기화

            navigateToHome()
        }

        // 텍스트 요약
        binding.btnCreateSaveText.setOnClickListener {
            isAvail = true
            setUrl = binding.etCreateLink.text.toString()

            // URL에 맞는 더미 데이터 가져오기
            linkAddViewModel.fetchLinkByUrl(setUrl!!)
            Log.d("CreateFragment", "입력된 URL: $setUrl")

            // 텍스트 요약 API 호출
            linkSummaryViewModel.fetchLinkSummary(linkSummaryRequest = LinkSummaryRequest(setUrl!!))  // 텍스트 요약 API 호출

            // 제목, 썸네일 API 호출
            linkExtractViewModel.fetchLinkExtract(linkExtractRequest = LinkExtractRequest(setUrl!!))  // 제목, 썸네일 API 호출

            navigateToLoading()
        }

        // 링크 저장
        binding.btnCreateSaveLink.setOnClickListener {
            isAvail = true
            val url = binding.etCreateLink.text.toString()

            // URL에 맞는 더미 데이터 가져오기
            linkAddViewModel.fetchLinkByUrl(url)
            Log.d("CreateFragment", "입력된 URL: $url")

            // 제목, 썸네일 API 호출
            linkExtractViewModel.fetchLinkExtract(linkExtractRequest = LinkExtractRequest(url))  // 제목, 썸네일 API 호출
        }

        // Delete 버튼
        binding.ivCreateDelete.setOnClickListener {
            linkAddViewModel.clearLinkInput()
            binding.etCreateLink.text.clear() // EditText의 내용을 직접 초기화
        }
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_createFragment_to_homeFragmentTab)
    }

    private fun navigateToLink() {
        setTitle?.let { id ->
            val action =
                setUrl?.let {
                    CreateFragmentDirections.actionCreateFragmentToCustomlinkZipFragment(
                        id, it
                    )
                }
            Log.d("CreateFragment", "$id $setUrl  전달 navigate")
            if (action != null) {
                findNavController().navigate(action)
            }
        } ?: run {
            Log.d("CreateFragment", "제목, 링크  전달 실패")
        }
    }

    private fun navigateToLoading() {
        findNavController().navigate(R.id.action_createFragment_to_createLoadingFragment)
    }
}
