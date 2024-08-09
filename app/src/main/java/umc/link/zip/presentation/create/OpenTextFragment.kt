package umc.link.zip.presentation.create

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentOpenTextBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class OpenTextFragment : BaseFragment<FragmentOpenTextBinding>(R.layout.fragment_open_text){
    override fun initObserver() {

    }

    override fun initView() {
        binding.ivOpenTextToolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnOpenTextEdit.setOnClickListener {
            navigateToCustomTextCustom()
        }
    }

    private fun navigateToCustomTextCustom() {
        findNavController().navigate(R.id.action_openTextFragment_to_customTextCustomFragment)
    }
}