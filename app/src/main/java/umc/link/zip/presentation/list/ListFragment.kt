package umc.link.zip.presentation.list

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentListBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.list.adapter.ListVPA
import umc.link.zip.util.extension.repeatOnStarted

@AndroidEntryPoint
class ListFragment : BaseFragment<FragmentListBinding>(R.layout.fragment_list) {
//    private val viewModel: ListViewModel by viewModels()
    private var _listVPA: ListVPA? = null
    private val listVPA get() = _listVPA
    private val navigator by lazy { findNavController() }

    override fun initObserver() {
        repeatOnStarted {
            /* 필요하다면
            viewModel.someEvent.collect { event ->
            }
             */
        }
    }

    override fun initView() {
        initListVPAdapter()
        setupButtonListeners()
    }

    private fun initListVPAdapter() {
        _listVPA = ListVPA(this)
        with(binding) {
            vpList.adapter = listVPA
            TabLayoutMediator(tabList, vpList) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()
        }
    }

    private fun setupButtonListeners() {
        binding.ivListAlarm.setOnClickListener {
            navigator.navigate(R.id.action_listFragment_to_alarmFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _listVPA = null
    }

    companion object {
        private val tabTitles = listOf("미열람 링크", "좋아요 누른 링크", "최근 저장한 링크")
    }
}

