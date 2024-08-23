package umc.link.zip.presentation.create

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCreateLoadingBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.create.adapter.LinkSummaryViewModel
import umc.link.zip.util.extension.repeatOnStarted

@AndroidEntryPoint
class CreateLoadingFragment : BaseFragment<FragmentCreateLoadingBinding>(R.layout.fragment_create_loading) {

    private val createViewModel: CreateLoadingViewModel by viewModels()
    private val linkSummaryViewModel: LinkSummaryViewModel by activityViewModels()


    override fun initObserver() {
        // currentVisibleView 관찰
        repeatOnStarted {
            createViewModel.currentVisibleView.collect { index ->
                updateVisibility(index)
            }
        }

        // animationFinished 관찰
        repeatOnStarted {
            createViewModel.animationFinished.collect { isFinished ->
                if (isFinished) {
                    navigateToCreateFragment()
                }
            }
        }

        repeatOnStarted {
            linkSummaryViewModel.isSummaryLoaded.collectLatest { isLoaded ->
                if (isLoaded) {
                    navigateToCustomTextZipFragment()
                }
            }
        }
    }

    override fun initView() {
        // 추가로 초기화할 뷰가 있으면 여기에 작성
    }

    private fun updateVisibility(visibleIndex: Int) {
        binding.clCreateLoading1.visibility = if (visibleIndex == 0) View.VISIBLE else View.INVISIBLE
        binding.clCreateLoading2.visibility = if (visibleIndex == 1) View.VISIBLE else View.INVISIBLE
        binding.clCreateLoading3.visibility = if (visibleIndex == 2) View.VISIBLE else View.INVISIBLE
    }

    private fun navigateToCustomTextZipFragment() {
        findNavController().navigate(R.id.action_createLoadingFragment_to_customTextZipFragment)
    }

    private fun navigateToCreateFragment() {
        findNavController().navigate(R.id.action_createLoadingFragment_to_createFragment)
    }
}
