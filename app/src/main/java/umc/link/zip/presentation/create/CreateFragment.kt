package umc.link.zip.presentation.create

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCreateBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.util.extension.repeatOnStarted

@AndroidEntryPoint
class CreateFragment : BaseFragment<FragmentCreateBinding>(R.layout.fragment_create) {

    private val viewModel: CreateViewModel by activityViewModels()

    override fun initObserver() {
        repeatOnStarted {
            viewModel.isSaveButtonVisible.collectLatest { isVisible ->
                binding.btnCreateSaveLink.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
                binding.ivCreateLinkOvalBlue.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
                binding.tvCreateLinkOvalBlue.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE

                binding.btnCreateSaveText.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
                binding.ivCreateTextOvalBlue.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
                binding.tvCreateTextOvalBlue.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
            }
        }

        repeatOnStarted {
            viewModel.isLinkIconVisible.collectLatest { isVisible ->
                binding.ivCreateLink.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
            }
        }

        repeatOnStarted {
            viewModel.isDeleteIconVisible.collectLatest { isVisible ->
                binding.ivCreateDelete.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
            }
        }
    }

    override fun initView() {
        binding.viewModel = viewModel // ViewModel을 데이터 바인딩에 설정

        binding.ivCreateToolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnCreateSaveText.setOnClickListener {
            navigateToLoading()
        }

        binding.btnCreateSaveLink.setOnClickListener {
            val url = binding.etCreateLink.text.toString()
            viewModel.fetchLinkByUrl(url)  // URL에 맞는 더미 데이터 가져오기
            navigateToLink()
        }

        binding.etCreateLink.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateLinkInput(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Delete 버튼 클릭 리스너 설정
        binding.ivCreateDelete.setOnClickListener {
            viewModel.clearLinkInput()
            binding.etCreateLink.text.clear() // EditText의 내용을 직접 초기화
        }
    }

    private fun navigateToText() {
        findNavController().navigate(R.id.action_createFragment_to_customtextZipFragment)
    }

    private fun navigateToLink() {
        findNavController().navigate(R.id.action_createFragment_to_customlinkZipFragment)
    }

    private fun navigateToLoading() {
        findNavController().navigate(R.id.action_createFragment_to_createLoadingFragment)
    }
}
