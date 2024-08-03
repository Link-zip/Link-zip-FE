package umc.link.zip.presentation.create

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomtextMemoBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class CustomtextMemoFragment : BaseFragment<FragmentCustomtextMemoBinding>(R.layout.fragment_customtext_memo){
    override fun initObserver() {

    }

    override fun initView() {
        binding.ivCustomTextMemoToolbarBack.setOnClickListener{
            navigateToCustom()
        }
        binding.clCustomTextMemoCompleteBtn.setOnClickListener {
            navigateToCustom()
        }
    }

    private fun navigateToCustom() {
        findNavController().navigate(R.id.action_customtextMemoFragment_to_customtextCustomFragment)
    }

}