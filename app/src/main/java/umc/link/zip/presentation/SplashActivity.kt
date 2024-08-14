package umc.link.zip.presentation

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
            kakaoLogIn()
        }, 1000)
    }

    override fun initObserver() {
    }

    private fun kakaoLogIn() {
        // 토큰이 유효한지 확인
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        Log.d("login", "로그인 했었는데 유효기간 끝난듯?")
                        navigateToLoginActivity()
                    }
                    else {
                        //기타 에러
                        Log.d("login", "뭔지 모를 에러남")
                    }
                }
                else {
                    //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                    if (tokenInfo != null) {
                        Log.d("login", "Token Info : ${tokenInfo.id}")
                        Log.d("login", "Token Info2 : ${tokenInfo.expiresIn}")
                        Log.d("login", "로그인 했었네")
                        navigateToMainActivity()
                    }
                }
            }
        }
        else {
            Log.d("login", "로그인 했던 적 없는데?")
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