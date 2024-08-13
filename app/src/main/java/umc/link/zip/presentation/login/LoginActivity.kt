package umc.link.zip.presentation.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.data.dto.request.LoginRequest
import umc.link.zip.data.service.LoginService
import umc.link.zip.databinding.ActivityLoginBinding
import umc.link.zip.di.NetworkModule
import umc.link.zip.presentation.MainActivity
import umc.link.zip.presentation.base.BaseActivity
import umc.link.zip.util.network.NetworkResult

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val retrofit by lazy { NetworkModule.providesRetrofit(
        NetworkModule.providesOkHttpClient(),
        NetworkModule.providesConverterFactory()
    ) }

    private val loginService by lazy {
        retrofit.create(LoginService::class.java)
    }

    private val viewModel : LoginViewModel by viewModels()

    override fun initView() {
        setClickListener()
    }

    override fun initObserver() {
        viewModel.loginResult.observe(this) { result ->
            when(result) {
                is NetworkResult.Error -> {}
                is NetworkResult.Fail -> {}
                is NetworkResult.Success -> {
                    /*//신규 회원인 경우
                    replaceFragment(ProfilesetFragment())
                    //기존 회원인 경우
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()*/
                }
            }
        }
    }

    // 카카오 로그인
    // 카카오계정으로 로그인 공통 callback 구성
    // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.d("login", "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.d("login", "카카오계정으로 로그인 성공")
            Log.d("login", "Access Token : ${token.accessToken}")
            Log.d("login", "Access Token Expires : ${token.accessTokenExpiresAt}")
            Log.d("login", "Refresh Token : ${token.refreshToken}")
            Log.d("login", "Refresh Token Expires : ${token.refreshTokenExpiresAt}")
            sendLoginRequest(token)
        }
    }

    private fun sendLoginRequest(token: OAuthToken) {
        val request = LoginRequest(
            accessToken = token.accessToken,
            accessTokenExpiresAt = token.accessTokenExpiresAt.toString(),
            refreshToken = token.refreshToken,
            refreshTokenExpiresAt = token.refreshTokenExpiresAt.toString()
        )

        viewModel.login(request)

        /*loginService.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("login", "Login successful: ${it.message}")
                        // Handle success, navigate to another screen, etc.
                        replaceFragment(ProfilesetFragment())
                    }
                } else {
                    Log.e("login", "Login failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("login", "Login request failed", t)
            }
        })*/
    }

    private fun setClickListener() {
        binding.btnLoginKakaologin.setOnClickListener {
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e(TAG, "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        Log.d("login", "카카오계정으로 로그인 성공")
                        Log.d("login", "Access Token : ${token.accessToken}")
                        Log.d("login", "Access Token Expires : ${token.accessTokenExpiresAt}")
                        Log.d("login", "Refresh Token : ${token.refreshToken}")
                        Log.d("login", "Refresh Token Expires : ${token.refreshTokenExpiresAt}")
                        Log.d("login", "ID Token : ${token.idToken}")
                        replaceFragment(ProfilesetFragment())
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
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
}