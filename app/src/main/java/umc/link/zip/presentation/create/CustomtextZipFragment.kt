package umc.link.zip.presentation.create

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomtextZipBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class CustomtextZipFragment : BaseFragment<FragmentCustomtextZipBinding>(R.layout.fragment_customtext_zip){
    override fun initObserver() {

    }

    override fun initView() {
        binding.ivCustomTextZipToolbarBack.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.clCustomTextZipNextBtn.setOnClickListener{
            navigateToCustom()
        }
    }


    private fun navigateToCustom() {
        findNavController().navigate(R.id.action_customtextZipFragment_to_customtextCustomFragment)
    }
}