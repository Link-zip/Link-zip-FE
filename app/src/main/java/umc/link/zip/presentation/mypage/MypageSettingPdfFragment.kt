package umc.link.zip.presentation.mypage

import androidx.navigation.fragment.findNavController
import umc.link.zip.R
import umc.link.zip.databinding.FragmentMypageSettingPdfBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.mypage.adapter.PdfAdapter

class MypageSettingPdfFragment : BaseFragment<FragmentMypageSettingPdfBinding>(R.layout.fragment_mypage_setting_pdf){
    private val navigator by lazy { findNavController() }
    override fun initObserver() {
    }

    override fun initView() {
        binding.ivMypageSettingPdfToolbarBack.setOnClickListener{
            navigator.navigateUp()
        }
        initRVAdapter()
    }

    private fun initRVAdapter(){
        val recyclerView = binding.pdfRecyclerView
        recyclerView.adapter = PdfAdapter(resources, R.raw.link_zip_service_policy)
    }

}