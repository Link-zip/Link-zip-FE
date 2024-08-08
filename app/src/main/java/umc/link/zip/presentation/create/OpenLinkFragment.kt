package umc.link.zip.presentation.create

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentOpenLinkBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class OpenLinkFragment : BaseFragment<FragmentOpenLinkBinding>(R.layout.fragment_open_link){
    private val viewModel: LinkViewModel by viewModels()

    override fun initObserver() {
        viewModel.linkData.observe(viewLifecycleOwner) { linkData ->
            binding.tvOpenLinkZipname.text = linkData.zipId
            binding.tvOpenLinkTitle.text = linkData.title
            binding.tvOpenLinkMemo.text = linkData.memo
            binding.tvOpenLinkAlarm.text = linkData.alertDate
        }
    }

    override fun initView() {
        // 추가적인 초기화 작업이 필요하다면 이곳에 작성
    }
}
