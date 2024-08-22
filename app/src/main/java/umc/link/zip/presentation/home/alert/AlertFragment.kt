package umc.link.zip.presentation.home.alert

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.databinding.FragmentAlertBinding
import umc.link.zip.domain.model.alert.Alert
import umc.link.zip.domain.model.alert.AlertGetModel
import umc.link.zip.domain.model.alert.AlertModel
import umc.link.zip.domain.model.search.SearchResult
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.home.alert.adapter.AlertGetViewModel
import umc.link.zip.presentation.home.alert.adapter.AlertRVA
import umc.link.zip.presentation.home.search.SearchFragmentDirections
import umc.link.zip.presentation.home.search.adapter.SearchResultRVA
import umc.link.zip.util.network.UiState

@AndroidEntryPoint
class AlertFragment : BaseFragment<FragmentAlertBinding>(R.layout.fragment_alert) {
    private val navigator by lazy { findNavController() }
    private val alertGetViewModel: AlertGetViewModel by viewModels()

    override fun initObserver() {

        lifecycleScope.launch {
            alertGetViewModel.getAlertResponse.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        Log.d("AlertFragment", "Loading get Alert data")
                    }
                    is UiState.Success -> {
                        val data = state.data as AlertModel
                        alertRVA.submitList(data.newAlerts)
                        binding.clAlertNone.visibility = View.GONE
                        binding.profilePostRv.visibility = View.VISIBLE
                        Log.d("AlertFragment", "get Alert 성공")
                    }
                    is UiState.Error -> {
                        // 에러 처리
                        binding.clAlertNone.visibility = View.VISIBLE
                        binding.profilePostRv.visibility = View.GONE
                        Log.e("AlertFragment", "Error loading alerts: ${state.error}")
                    }
                    UiState.Empty -> {
                        binding.clAlertNone.visibility = View.VISIBLE
                        binding.profilePostRv.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun initView() {
        // 업버튼 설정
        binding.ivAlertToolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // getAlert API 호출
        alertGetViewModel.getAlert()
    }

    private val alertRVA by lazy {
        AlertRVA(
            onItemClick = { link ->
                // 이동 api 호출

                // api 호출 success에 넣기
                /*val action = AlertFragmentDirections.actionAlertFragmentToOpenLinkFragment(link.link.id)
                navigator.navigate(action)*/
            }
        )
    }
}
