package umc.link.zip.presentation.login

import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.ActivityLoginBinding
import umc.link.zip.presentation.base.BaseActivity

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    override fun initView() {
        setClickListener()
    }

    override fun initObserver() {

    }

    private fun setClickListener() {
        binding.btnLoginKakaologin.setOnClickListener {
            binding.btnLoginKakaologin.isClickable = false
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_view_login, ProfilesetFragment(), "profileset")
                .commit()
            // 소셜 로그인 추가 해야 함
        }
    }

    /*fun changeFragment(index: Int){
        when(index){
            1 -> {
                binding.clLoginMain.visibility = View.GONE
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_view_login, ProfilesetFragment(), "profileset")
                    .commit()
            }

            2 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_view_login, ProfilesetCompletedFragment())
                    .commit()
            }

            3 -> {
                binding.clLoginMain.visibility = View.VISIBLE
                supportFragmentManager.findFragmentByTag("profileset")?.let {
                    supportFragmentManager
                        .beginTransaction()
                        .remove(it)
                        .commit()
                }
            }
        }
    }*/
}