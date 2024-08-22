package umc.link.zip.presentation

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.data.UserPreferences
import umc.link.zip.databinding.ActivitySplashBinding
import umc.link.zip.presentation.base.BaseActivity
import umc.link.zip.presentation.login.LoginActivity
import umc.link.zip.presentation.login.LoginViewModel
import umc.link.zip.util.network.NetworkResult

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash){

    private val viewModel : LoginViewModel by viewModels()

    override fun initView() {
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            checkJwt()
        }, 1000)
    }

    override fun initObserver() {
        viewModel.checkJwtResult.observe(this) { result ->
            when(result) {
                is NetworkResult.Error -> {
                    Log.d("login", "Jwt 확인 error : ${result.exception}")
                }
                is NetworkResult.Fail -> {
                    Log.d("login", "Jwt 확인 실패 : ${result.statusCode}")
                }
                is NetworkResult.Success -> {
                    Log.d("login", "Jwt 확인 성공")
                    navigateToMainActivity()
                }
            }
        }
    }

    private fun checkJwt() {
        val userId = UserPreferences(this).getUserId()

        if (userId != null) {
            Log.d("login", "JWT 발견 $userId")
            viewModel.checkJwt()
        } else {
            Log.d("login", "JWT 없음")
            navigateToLoginActivity()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
