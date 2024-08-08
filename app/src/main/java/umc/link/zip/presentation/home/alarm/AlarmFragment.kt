package umc.link.zip.presentation.home.alarm

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.databinding.FragmentAlarmBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.home.alarm.adapter.AlarmRVA
import umc.link.zip.domain.model.alarm.Alarm

@AndroidEntryPoint
class AlarmFragment : BaseFragment<FragmentAlarmBinding>(R.layout.fragment_alarm) {

    private val viewModel: AlarmViewModel by viewModels()

    // lateinit으로 선언하여 나중에 초기화
    private lateinit var alarmRVA: AlarmRVA

    override fun initObserver() {
        lifecycleScope.launch {
            viewModel.alarms.collect { alarms ->
                // 어댑터가 초기화된 상태인지 확인
                if (!::alarmRVA.isInitialized) {
                    // 어댑터를 초기화
                    initAlarmRVAdapter()
                }

                // 알림 데이터가 있을 경우와 없을 경우 UI를 업데이트합니다.
                if (alarms.isNullOrEmpty()) {
                    binding.clAlarmNone.visibility = View.VISIBLE
                    binding.profilePostRv.visibility = View.GONE
                } else {
                    binding.clAlarmNone.visibility = View.GONE
                    binding.profilePostRv.visibility = View.VISIBLE
                    alarmRVA.submitList(alarms)
                }
            }
        }
    }

    override fun initView() {
        // 업버튼 설정
        binding.ivAlarmToolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initAlarmRVAdapter() {
        // 어댑터를 초기화
        alarmRVA = AlarmRVA { alarm ->
            viewModel.updateAlarmStatus(alarm.id)
        }
        binding.profilePostRv.adapter = alarmRVA
/*
        // 더미 데이터 설정
        val dummyAlarms = listOf(
            Alarm(
                1,
                0,
                "original",
                "2024-03-20T08:00:00Z",
                "\"마이크로/나노 인플루언서 마케팅 전략\" 리마인드 알림이 도착했어요!",
                "트렌드 파악!"
            ),
            Alarm(
                2,
                1,
                "original",
                "2024-03-20T12:00:00Z",
                "어쩌고 저쩌고",
                "어쩌고 메모"
            ),
            Alarm(
                3,
                0,
                "reminder",
                "2024-03-20T18:00:00Z",
                "제목제목",
                "메모메모"
            ),
            Alarm(
                4,
                1,
                "reminder",
                "2024-03-20T18:00:00Z",
                "제목제목",
                "메모메모"
            ),
            Alarm(
                5,
                0,
                "original",
                "2024-03-20T18:00:00Z",
                "제목제목",
                "메모메모"
            ),
            Alarm(
                6,
                0,
                "reminder",
                "2024-03-20T18:00:00Z",
                "제목제목",
                "메모메모"
            )
        )

        // ViewModel에 더미 데이터를 설정합니다.
        viewModel.setAlarms(dummyAlarms)*/
    }
}
