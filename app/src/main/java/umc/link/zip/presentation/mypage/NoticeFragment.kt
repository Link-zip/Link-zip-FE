package umc.link.zip.presentation.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.databinding.FragmentNoticeBinding
import umc.link.zip.domain.model.list.UnreadModel
import umc.link.zip.domain.model.notice.Notice
import umc.link.zip.domain.model.notice.NoticeList
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.home.search.SearchFragmentDirections
import umc.link.zip.presentation.list.ListUnreadViewModel
import umc.link.zip.presentation.list.adapter.ListUnreadRVA
import umc.link.zip.presentation.mypage.adapter.NoticeRVA
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.network.UiState

@AndroidEntryPoint
class NoticeFragment :BaseFragment<FragmentNoticeBinding> (R.layout.fragment_notice){
    private val navigator by lazy { findNavController() }
    private val viewModel: NoticeViewModel by viewModels()

    private val noticeRVA by lazy {
        NoticeRVA{
            int -> val action = NoticeFragmentDirections.actionNoticeFragmentToNoticeDetailFragment(int)
            navigator.navigate(action)
        }
    }

    override fun initObserver() {

        // StateFlow를 관찰하여 RecyclerView Adapter에 데이터를 전달
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            // 로딩 상태 처리
                            Log.d("NoticeFragment", "Loading data")
                        }

                        is UiState.Success<*> -> {
                            val data = uiState.data as NoticeList
                            Log.d("NoticeFragment", "Fetched data size: ${data.notice_data}")
                            noticeRVA.submitList(data.notice_data)
                        }

                        is UiState.Error -> {
                            // 에러 상태 처리
                            Log.e("NoticeFragment", "Error fetching data", uiState.error)
                        }

                        UiState.Empty -> Log.d("NoticeFragment", "isEmpty")
                    }
                }
            }
        }
    }

    private fun fnNoticeApi(){
        viewModel.getNotice()
    }

    override fun initView() {
        binding.ivNoticeToolbarBack.setOnClickListener{
            navigator.navigateUp()
        }
        initNoticeRVAdapter()
        fnNoticeApi()
    }

    override fun onResume() {
        super.onResume()
        fnNoticeApi()
    }

    private fun initNoticeRVAdapter(){
        binding.rvNotice.adapter = noticeRVA
        /*
        val list = listOf(
            Notice("1", "공지사항 제목", "2024.06.29"),
            Notice("2", "공지사항 제목2", "2024.06.29"),
            Notice("3", "공지사항 제목3", "2024.06.29"),
            Notice("4", "공지사항 제목4", "2024.06.29"),
        )
        noticeRVA.submitList(list)
         */
    }
    private fun setupClickListener() {

    }

}