package umc.link.zip.presentation.mypage

import android.content.Intent
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.data.UserPreferences
import umc.link.zip.data.dto.TokenRefreshManager
import umc.link.zip.databinding.FragmentMypageBinding
import umc.link.zip.domain.model.mypage.UserInfoModel
import umc.link.zip.presentation.SplashActivity
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.network.UiState
import javax.inject.Inject

@AndroidEntryPoint
class MypageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {
    private val viewModel: MypageProfileViewModel by activityViewModels()
    private val navigator by lazy { findNavController() }

    //토큰 리프레시
    @Inject
    lateinit var tokenRefreshManager: TokenRefreshManager

    private fun refreshToken() {
        lifecycleScope.launch {
            val newToken = tokenRefreshManager.refreshToken()
            if (newToken != null) {
                Log.d("MyFragment", "New Token: $newToken")
            } else {
                Log.d("MyFragment", "Failed to refresh token")
            }
        }
    }

    override fun initObserver() {
        refreshToken()
        //닉네임 가져오기
        repeatOnStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userInfoState.collect { state ->
                    when (state) {
                        UiState.Empty -> Log.d("getUserInfo", "No data")
                        is UiState.Error -> Log.e("getUserInfo", "Error fetching data", state.error)
                        UiState.Loading -> Log.d("getUserInfo", "Loading data")
                        is UiState.Success ->
                        {
                            val data = state.data as UserInfoModel
                            binding.tvMypageNickname.text = data.nickname
                        }
                    }
                }
            }
        }
    }

    private fun fnGetUserInfoApi(){
        viewModel.getUserInfo()
    }

    override fun initView() {
        fnGetUserInfoApi()
        navigateToMypageProfile()
        navigateToMypageSetting()

        //로그아웃 기능 구현
        binding.tvMypageLogout.setOnClickListener {
            fnLogout()
        }
    }

    private fun navigateToMypageProfile() {
        binding.clMypageProfile.setOnClickListener {
            navigator.navigate(R.id.action_mypageFragment_to_mypageProfileFragment)
        }
    }
    private fun navigateToMypageSetting() {
        binding.clMypageSetting.setOnClickListener {
            navigator.navigate(R.id.action_mypageFragment_to_mypageSettingFragment)
        }
    }

    private fun fnLogout(){
        UserPreferences(requireContext()).deleteUserId()
        val splashIntent = Intent(requireContext(), SplashActivity::class.java)
        splashIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(splashIntent)
        activity?.finish()
    }
}