package umc.link.zip.presentation.mypage

import android.content.Intent
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.data.UserPreferences
import umc.link.zip.databinding.FragmentMypageWithdrawalBinding
import umc.link.zip.domain.model.mypage.CheckNicknmModel
import umc.link.zip.domain.model.mypage.WithdrawalModel
import umc.link.zip.presentation.SplashActivity
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.network.UiState

@AndroidEntryPoint
class MypageWithdrawalFragment : BaseFragment<FragmentMypageWithdrawalBinding>(R.layout.fragment_mypage_withdrawal) {

    private val navigator by lazy { findNavController() }
    private val viewModel: MypageWithdrawalViewModel by viewModels()
    override fun initObserver() {
        repeatOnStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        UiState.Empty -> Log.d("DeleteUser", "Empty")
                        is UiState.Error ->  // 에러 상태 처리
                            Log.e("DeleteUser", "Error fetching data", state.error)
                        UiState.Loading -> Log.d("DeleteUser", "Loading data")
                        is UiState.Success ->
                        {
                            val data = state.data as WithdrawalModel
                            if(data.isDeleted){
                                Log.d("DeleteUser", "회원 삭제 성공")
                                UserPreferences(requireContext()).deleteUserId()
                                val splashIntent = Intent(requireContext(), SplashActivity::class.java)
                                splashIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(splashIntent)
                                activity?.finish()
                            }else{
                                Log.d("DeleteUser", "삭제 실패")
                            }
                        }
                    }
                }
            }
        }
    }

    override fun initView() {
        binding.ivMypageWithdrawalToolbarBack.setOnClickListener{
            navigator.navigateUp()
        }

        binding.clMypageWithdrawalBtn.setOnClickListener{
            viewModel.deleteUser()
        }
    }

}