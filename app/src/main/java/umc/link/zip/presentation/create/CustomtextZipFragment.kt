package umc.link.zip.presentation.create

import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomtextZipBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.create.adapter.LinkAddViewModel

@AndroidEntryPoint
class CustomtextZipFragment : BaseFragment<FragmentCustomtextZipBinding>(R.layout.fragment_customtext_zip){

    private val linkAddViewModel: LinkAddViewModel by activityViewModels()
    private val navigator by lazy { findNavController() }
    override fun initObserver() {

    }

    override fun initView() {
        binding.ivCustomTextZipToolbarBack.setOnClickListener{
            navigateToCreate()
        }

        binding.clCustomTextZipNextBtn.setOnClickListener{
            navigateToCustom()
        }
    }

    private fun navigateToCreate() {
        val action = CustomtextZipFragmentDirections.actionCustomtextZipFragmentToCreateFragment(linkAddViewModel.link.value.url)
        navigator.navigate(action, NavOptions.Builder()
            .setPopUpTo(R.id.createFragment, false)
            .build())
    }

    private fun navigateToCustom() {
        navigator.navigate(R.id.action_customtextZipFragment_to_customtextCustomFragment)
    }
}