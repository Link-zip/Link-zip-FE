package umc.link.zip.presentation.create

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCreateLoadingBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.util.extension.repeatOnStarted

@AndroidEntryPoint
class CreateLoadingFragment : BaseFragment<FragmentCreateLoadingBinding>(R.layout.fragment_create_loading) {

    private val viewModel: CreateLoadingViewModel by viewModels()

    override fun initObserver() {
        // currentVisibleView 관찰
        repeatOnStarted {
            viewModel.currentVisibleView.collect { index ->
                updateVisibility(index)
            }
        }

        // animationFinished 관찰
        repeatOnStarted {
            viewModel.animationFinished.collect { isFinished ->
                if (isFinished) {
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
}
