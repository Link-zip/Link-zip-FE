package umc.link.zip.presentation.mypage

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.databinding.FragmentNoticeDetailBinding
import umc.link.zip.domain.model.notice.Notice
import umc.link.zip.domain.model.notice.NoticeList
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.network.UiState
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class NoticeDetailFragment :BaseFragment<FragmentNoticeDetailBinding> (R.layout.fragment_notice_detail){
    private val navigator by lazy { findNavController() }
    private val viewModel: NoticeViewModel by viewModels()
    override fun initObserver() {
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.detail.collectLatest { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            // 로딩 상태 처리
                            Log.d("NoticeDetailFragment", "Loading data")
                        }

                        is UiState.Success<*> -> {
                            val data = uiState.data as Notice
                            Log.d("NoticeDetailFragment", "Fetched data size: ${data}")
                            fnBinding(data)
                        }

                        is UiState.Error -> {
                            // 에러 상태 처리
                            Log.e("NoticeDetailFragment", "Error fetching data", uiState.error)
                        }

                        UiState.Empty -> Log.d("NoticeDetailFragment", "isEmpty")
                    }
                }
            }
        }

    }

    private fun fnBinding(data :Notice) {
        with(binding){
            tvNoticeDetailTitle.text = data.title
            tvNoticeDetailDate.text = modifyDate(data.createdAt)
            tvNoticeDetailContent.text = data.content
        }
    }

    private fun modifyDate(data : String) : String{
        return try {
            val date = data.substringBefore("T")
            // 입력 형식: 밀리초와 'Z'를 포함
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            // 출력 형식
            val outputFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

            // 문자열을 Date 객체로 파싱
            val parsedDate = inputFormat.parse(date)

            // Date 객체를 문자열로 포맷팅
            parsedDate?.let { outputFormat.format(it) } ?: "Invalid Date"
        } catch (e: Exception) {
            "Invalid Date"
        }
    }

    override fun initView() {
        val noticeId = requireArguments().getInt("noticeId")
        viewModel.getNoticeDetail(noticeId)

        binding.ivNoticeDetailToolbarBack.setOnClickListener {
            navigator.navigateUp()
        }
    }


}