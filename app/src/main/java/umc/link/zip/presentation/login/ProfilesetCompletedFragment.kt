package umc.link.zip.presentation.login

import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentProfilesetcompletedBinding
import umc.link.zip.presentation.MainActivity
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class ProfilesetCompletedFragment : BaseFragment<FragmentProfilesetcompletedBinding>(R.layout.fragment_profilesetcompleted){
    override fun initObserver() {

    }

    override fun initView() {
        setClickListener()
    }

    private fun setClickListener() {
        binding.btnProfilesetcompletedStart.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
        }
    }
}