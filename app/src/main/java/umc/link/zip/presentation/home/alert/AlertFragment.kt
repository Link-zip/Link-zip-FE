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

    private var linkId: Int? = null

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
        lifecycleScope.launch {
            alertGetViewModel.confirmAlertResponse.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        Log.d("AlertFragment", "Loading confirm Alert data")
                    }
                    is UiState.Success -> {
                        val data = state.data as AlertModel
                        alertRVA.submitList(data.newAlerts)

                        val action = linkId?.let {
                            AlertFragmentDirections.actionAlertFragmentToOpenLinkFragment(
                                it
                            )
                        }
                        if (action != null) {
                            navigator.navigate(action)
                        }
                        Log.d("AlertFragment", "${linkId}번 링크 알림 확인 성공")
                    }
                    is UiState.Error -> {
                        // 에러 처리
                        Log.e("AlertFragment", "Error loading confirm alerts: ${state.error}")
                    }
                    UiState.Empty -> {
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

        binding.profilePostRv.adapter = alertRVA

        // getAlert API 호출
        alertGetViewModel.getAlert()
    }

    private val alertRVA by lazy {
        AlertRVA(
            onItemClick = { alert ->
                linkId = alert.link.id
                alertGetViewModel.confirmAlert()
            }
        )
    }
}
