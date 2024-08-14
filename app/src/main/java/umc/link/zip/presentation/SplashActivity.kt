package umc.link.zip.presentation

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.ActivitySplashBinding
import umc.link.zip.presentation.base.BaseActivity
import umc.link.zip.presentation.login.LoginActivity

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash){
    override fun initView() {
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            checkJwt()
        }, 1000)
    }

    override fun initObserver() {
    }

    private fun checkJwt() {
        val sharedPref = getSharedPreferences("linkzip_prefs", Context.MODE_PRIVATE)
        val accessToken = sharedPref.getString("access_token", null)

        if (accessToken != null) {
            Log.d("login", "JWT 발견 $accessToken")
            navigateToMainActivity()
        } else {
            Log.d("login", "JWT 없음")
            navigateToLoginActivity()
        }

        finish()
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