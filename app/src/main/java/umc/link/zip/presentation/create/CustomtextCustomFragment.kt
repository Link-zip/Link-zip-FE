package umc.link.zip.presentation.create

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomtextCustomBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class CustomtextCustomFragment : BaseFragment<FragmentCustomtextCustomBinding>(R.layout.fragment_customtext_custom){
    override fun initObserver() {

    }

    override fun initView() {
        binding.ivCustomTextCustomToolbarBack.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.btnCustomTextCustomMemo.setOnClickListener{
            navigateToMemo()
        }

        binding.btnCustomTextCustomAlarm.setOnClickListener{
            navigateToAlarm()
        }

        binding.clCustomTextCustomSaveBtn.setOnClickListener {
            navigateToOpenText()
        }
    }


    private fun navigateToMemo() {
        findNavController().navigate(R.id.action_customtextCustomFragment_to_customtextMemoFragment)
    }

    private fun navigateToAlarm() {
        findNavController().navigate(R.id.action_customtextCustomFragment_to_customtextAlarmFragment)
    }

    private fun navigateToOpenText() {
        findNavController().navigate(R.id.action_customtextCustomFragment_to_openTextFragment)
    }
}