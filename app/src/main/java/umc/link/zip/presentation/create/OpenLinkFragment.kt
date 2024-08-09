package umc.link.zip.presentation.create

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentOpenLinkBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class OpenLinkFragment : BaseFragment<FragmentOpenLinkBinding>(R.layout.fragment_open_link) {

    private val viewModel: CustomLinkViewModel by viewModels()

    override fun initObserver() {
    }

    override fun initView() {
        binding.ivOpenLinkToolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnOpenLinkEdit.setOnClickListener {
            navigateToCustomLinkCustom()
        }
    }

    private fun navigateToCustomLinkCustom() {
        findNavController().navigate(R.id.action_openLinkFragment_to_customLinkCustomFragment)
    }


    /*
    private fun setLike(link: Link, view: ImageView, onLikeChanged: (Link) -> Unit) {
        // 초기 상태 설정
        if (link.likes == 1) {
            view.setImageResource(R.drawable.ic_heart_selected)
        } else {
            view.setImageResource(R.drawable.ic_heart_unselected)
        }

        // 클릭 리스너 설정
        view.setOnClickListener {
            link.likes = if (link.likes == 1) 0 else 1
            if (link.likes == 1) {
                view.setImageResource(R.drawable.ic_heart_selected)
            } else {
                view.setImageResource(R.drawable.ic_heart_unselected)
            }
            // 좋아요 상태가 변경되었음을 외부에 알림
            onLikeChanged(link)
        }
    }*/
}
