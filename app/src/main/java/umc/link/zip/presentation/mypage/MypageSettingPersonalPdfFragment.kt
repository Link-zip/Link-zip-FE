package umc.link.zip.presentation.mypage

import androidx.navigation.fragment.findNavController
import umc.link.zip.R
import umc.link.zip.databinding.FragmentMypageSettingPdfBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.mypage.adapter.PdfAdapter

class MypageSettingPersonalPdfFragment : BaseFragment<FragmentMypageSettingPdfBinding>(R.layout.fragment_mypage_setting_pdf){
    private val navigator by lazy { findNavController() }
    override fun initObserver() {
    }

    override fun initView() {
        binding.ivMypageSettingPdfToolbarBack.setOnClickListener{
            navigator.navigateUp()
        }
        initRVAdapter()
        binding.tvMypageSettingToolbarTitle.text = "개인 정보 처리 방침"
    }

    private fun initRVAdapter(){
        val recyclerView = binding.pdfRecyclerView
        recyclerView.adapter = PdfAdapter(resources, R.raw.link_zip_personal_policy)
    }

}