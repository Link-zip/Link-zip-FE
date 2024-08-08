package umc.link.zip.presentation.home.alarm

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.databinding.FragmentAlarmBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.home.alarm.adapter.AlarmRVA

@AndroidEntryPoint
class AlarmFragment : BaseFragment<FragmentAlarmBinding>(R.layout.fragment_alarm) {

    private val viewModel: AlarmViewModel by viewModels()
    private lateinit var adapter: AlarmRVA

    override fun initObserver() {
        lifecycleScope.launch {
            viewModel.alarms.collect { alarms ->
                if (!::adapter.isInitialized) {
                    // adapter가 초기화되지 않았을 경우 return
                    return@collect
                }
                if (alarms.isNullOrEmpty()) {
                    binding.clAlarmNone.visibility = View.VISIBLE
                    binding.profilePostRv.visibility = View.GONE
                } else {
                    binding.clAlarmNone.visibility = View.GONE
                    binding.profilePostRv.visibility = View.VISIBLE
                    adapter.submitList(alarms)
                }
            }
        }
    }

    override fun initView() {
        // 업버튼
        binding.ivAlarmToolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // 어댑터
        adapter = AlarmRVA()
        binding.profilePostRv.adapter = adapter
    }
}
