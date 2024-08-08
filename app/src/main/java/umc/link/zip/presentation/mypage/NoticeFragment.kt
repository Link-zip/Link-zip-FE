package umc.link.zip.presentation.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import umc.link.zip.R
import umc.link.zip.databinding.FragmentNoticeBinding
import umc.link.zip.domain.model.notice.Notice
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.mypage.adapter.NoticeRVA

class NoticeFragment :BaseFragment<FragmentNoticeBinding> (R.layout.fragment_notice){
    private val navigator by lazy { findNavController() }
    override fun initObserver() {

    }

    override fun initView() {
        binding.ivNoticeToolbarBack.setOnClickListener{
            navigator.navigateUp()
        }
        initNoticeRVAdapter()
    }

    private val noticeRVA by lazy {
        NoticeRVA{
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
        setupClickListener()
    }

    private fun initNoticeRVAdapter(){
        binding.rvNotice.adapter = noticeRVA
        val list = listOf(
            Notice("1", "공지사항 제목", "2024.06.29"),
            Notice("2", "공지사항 제목2", "2024.06.29"),
            Notice("3", "공지사항 제목3", "2024.06.29"),
            Notice("4", "공지사항 제목4", "2024.06.29"),
        )
        noticeRVA.submitList(list)
    }
    private fun setupClickListener() {

    }

}