package umc.link.zip.presentation.home.alarm

import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentAlarmBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class AlarmFragment : BaseFragment<FragmentAlarmBinding>(R.layout.fragment_alarm) {

    private val viewModel: AlarmViewModel by viewModels()
    private lateinit var adapter: AlarmAdapter

    override fun initObserver() {
        viewModel.alarms.observe(viewLifecycleOwner) { alarms ->
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

    override fun initView() {
        adapter = AlarmAdapter()
        binding.profilePostRv.adapter = adapter
    }
}
