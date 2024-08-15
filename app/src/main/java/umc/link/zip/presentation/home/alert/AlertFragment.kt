package umc.link.zip.presentation.home.alert

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.databinding.FragmentAlertBinding
import umc.link.zip.domain.model.alert.Alert
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.home.alert.adapter.AlertRVA

@AndroidEntryPoint
class AlertFragment : BaseFragment<FragmentAlertBinding>(R.layout.fragment_alert) {

    private val viewModel: AlertViewModel by viewModels()

    // lateinit으로 선언하여 나중에 초기화
    private lateinit var alertRVA: AlertRVA

    override fun initObserver() {
        lifecycleScope.launch {
            viewModel.alerts.collect { alerts ->
                // 어댑터가 초기화된 상태인지 확인
                if (!::alertRVA.isInitialized) {
                    // 어댑터를 초기화
                    initAlertRVAdapter()
                }

                // 알림 데이터가 있을 경우와 없을 경우 UI를 업데이트합니다.
                if (alerts.isNullOrEmpty()) {
                    binding.clAlertNone.visibility = View.VISIBLE
                    binding.profilePostRv.visibility = View.GONE
                } else {
                    binding.clAlertNone.visibility = View.GONE
                    binding.profilePostRv.visibility = View.VISIBLE
                    alertRVA.submitList(alerts)
                }
            }
        }
    }

    override fun initView() {
        // 업버튼 설정
        binding.ivAlertToolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initAlertRVAdapter() {
        // 어댑터를 초기화
        alertRVA = AlertRVA { alarm ->
            viewModel.updateAlertStatus(alarm.id)
        }
        binding.profilePostRv.adapter = alertRVA

        // 더미 데이터 설정
        val dummyAlerts = listOf(
            Alert(
                1,
                0,
                "original",
                "2024-03-20T08:00:00Z",
                "\"마이크로/나노 인플루언서 마케팅 전략\" 리마인드 알림이 도착했어요!",
                "트렌드 파악!"
            ),
            Alert(
                2,
                0,
                "original",
                "2024-03-20T12:00:00Z",
                "제목1",
                "메모1"
            ),
            Alert(
                3,
                0,
                "reminder",
                "2024-03-20T18:00:00Z",
                "제목2\n최대 2줄",
                "메모메모"
            ),
            Alert(
                4,
                0,
                "reminder",
                "2024-03-20T18:00:00Z",
                "제목3",
                "메모3"
            ),
            Alert(
                5,
                0,
                "original",
                "2024-03-20T18:00:00Z",
                "제목4",
                "메모4"
            ),
            Alert(
                6,
                0,
                "reminder",
                "2024-03-20T18:00:00Z",
                "제목5\n2줄을 넘기면 끝부분이 요약됩니다 제목제목제목제목제목제목",
                "글이 길어지면 끝부분이 ...으로 요약되는 것을 확인할 수 있습니다"
            )
        )

        // ViewModel에 더미 데이터를 설정합니다.
        viewModel.setAlerts(dummyAlerts)
    }
}
