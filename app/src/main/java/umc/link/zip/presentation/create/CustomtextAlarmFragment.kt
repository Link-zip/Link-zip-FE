package umc.link.zip.presentation.create

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomtextAlarmBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class CustomtextAlarmFragment : BaseFragment<FragmentCustomtextAlarmBinding>(R.layout.fragment_customtext_alarm){
    override fun initObserver() {

    }

    override fun initView() {
        binding.ivCustomTextAlarmToolbarBack.setOnClickListener{
            findNavController().navigateUp()
        }
        binding.clCustomTextAlarmSaveBtn.setOnClickListener {
            navigateToCustom()
        }
    }

    private fun navigateToCustom() {
        findNavController().navigate(R.id.action_customtextAlarmFragment_to_customtextCustomFragment)
    }

}