package umc.link.zip.presentation.login

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
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
            replaceFragment(ProfilesetFragment())
            // 소셜 로그인 추가 해야 함
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fragment_view_login, fragment)
            addToBackStack(null)
        }
        disableLoginBtn()
    }

    fun disableLoginBtn() {
        binding.btnLoginKakaologin.isClickable = false
    }

    fun enableLoginBtn() {
        binding.btnLoginKakaologin.isClickable = true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            if (supportFragmentManager.backStackEntryCount == 1) {
                enableLoginBtn()
            }
        } else {
            super.onBackPressed()
        }
    }
}