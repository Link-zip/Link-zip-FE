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
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_view_login, ProfilesetFragment(), "profileset")
                .commit()
            // 소셜 로그인 추가 해야 함
        }
    }
}