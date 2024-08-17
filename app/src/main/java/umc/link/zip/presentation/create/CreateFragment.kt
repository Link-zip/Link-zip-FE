package umc.link.zip.presentation.create

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.data.dto.link.request.LinkExtractRequest
import umc.link.zip.data.dto.link.request.LinkSummaryRequest
import umc.link.zip.databinding.FragmentCreateBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.create.adapter.LinkExtractViewModel
import umc.link.zip.presentation.create.adapter.LinkSummaryViewModel
import umc.link.zip.util.extension.repeatOnStarted

@AndroidEntryPoint
class CreateFragment : BaseFragment<FragmentCreateBinding>(R.layout.fragment_create) {

    private val createViewModel: CreateViewModel by activityViewModels()
    private val linkSummaryViewModel: LinkSummaryViewModel by activityViewModels()
    private val linkExtractViewModel: LinkExtractViewModel by activityViewModels()

    override fun initObserver() {
        repeatOnStarted {
            createViewModel.isSaveButtonVisible.collectLatest { isVisible ->
                binding.btnCreateSaveLink.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
                binding.ivCreateLinkOvalBlue.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
                binding.tvCreateLinkOvalBlue.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE

                binding.btnCreateSaveText.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
                binding.ivCreateTextOvalBlue.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
                binding.tvCreateTextOvalBlue.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
            }
        }

        repeatOnStarted {
            createViewModel.isLinkIconVisible.collectLatest { isVisible ->
                binding.ivCreateLink.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
            }
        }

        repeatOnStarted {
            createViewModel.isDeleteIconVisible.collectLatest { isVisible ->
                binding.ivCreateDelete.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
            }
        }
    }

    override fun initView() {
        binding.viewModel = createViewModel


        binding.etCreateLink.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                createViewModel.updateLinkInput(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        setOnClickListener()
    }


    private fun setOnClickListener(){
        binding.ivCreateToolbarBack.setOnClickListener {
            createViewModel.clearLinkInput()
            binding.etCreateLink.text.clear() // EditText의 내용 초기화

            findNavController().navigateUp()
        }

        // 텍스트 요약
        binding.btnCreateSaveText.setOnClickListener {
            val url = binding.etCreateLink.text.toString()

            // 텍스트 요약 API 호출
            linkSummaryViewModel.fetchLinkSummary(linkSummaryRequest = LinkSummaryRequest(url))  // 텍스트 요약 API 호출

            // 제목, 썸네일 API 호출
            linkExtractViewModel.fetchLinkExtract(linkExtractRequest = LinkExtractRequest(url))  // 제목, 썸네일 API 호출

            navigateToLoading()
        }

        // 링크 저장
        binding.btnCreateSaveLink.setOnClickListener {
            val url = binding.etCreateLink.text.toString()

            // 제목, 썸네일 API 호출
            linkExtractViewModel.fetchLinkExtract(linkExtractRequest = LinkExtractRequest(url))  // 제목, 썸네일 API 호출

            navigateToLink()
        }

        // Delete 버튼
        binding.ivCreateDelete.setOnClickListener {
            createViewModel.clearLinkInput()
            binding.etCreateLink.text.clear() // EditText의 내용을 직접 초기화
        }
    }

    private fun navigateToLink() {
        findNavController().navigate(R.id.action_createFragment_to_customlinkZipFragment)
    }

    private fun navigateToLoading() {
        findNavController().navigate(R.id.action_createFragment_to_createLoadingFragment)
    }
}
