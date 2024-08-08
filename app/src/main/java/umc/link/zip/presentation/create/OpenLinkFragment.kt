package umc.link.zip.presentation.create

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.databinding.FragmentOpenLinkBinding
import umc.link.zip.domain.model.create.Link
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class OpenLinkFragment : BaseFragment<FragmentOpenLinkBinding>(R.layout.fragment_open_link) {

    private val viewModel: CustomLinkViewModel by viewModels()

    override fun initObserver() {
        lifecycleScope.launch {
            viewModel.link.collect { link ->
                binding.tvOpenLinkZipname.text = link.zip.title
                binding.tvOpenLinkTitle.text = link.title
                binding.tvOpenLinkMemo.text = link.text
                binding.tvOpenLinkAlarm.text = link.createdAt
                setLike(link, binding.ivOpenLinkHeart) { updatedLink ->
                    viewModel.setLikes(updatedLink.likes)
                }
            }
        }
    }

    override fun initView() {
        // 예: 데이터를 설정하는 부분
    }

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
    }
}
