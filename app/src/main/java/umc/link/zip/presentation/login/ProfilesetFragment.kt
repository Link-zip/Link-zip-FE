package umc.link.zip.presentation.login

import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentProfilesetBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class ProfilesetFragment : BaseFragment<FragmentProfilesetBinding>(R.layout.fragment_profileset){
    override fun initObserver() {

    }

    override fun initView() {
        setClickListener()
    }

    private fun setClickListener() {
        binding.btnProfilesetFinish.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_view_login, ProfilesetCompletedFragment())
                .commit()
        }

        binding.ivProfilesetToolbarBack.setOnClickListener {
            requireActivity().supportFragmentManager.findFragmentByTag("profileset")?.let {
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .remove(it)
                    .commit()
            }
        }
    }
}