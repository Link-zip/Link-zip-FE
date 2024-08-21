package umc.link.zip.presentation.list

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.databinding.FragmentListBinding
import umc.link.zip.domain.model.list.Link
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.home.SharedViewModel
import umc.link.zip.presentation.list.adapter.ListVPA
import umc.link.zip.util.extension.repeatOnStarted

@AndroidEntryPoint
class ListFragment : BaseFragment<FragmentListBinding>(R.layout.fragment_list), OnItemClickListener {
//    private val viewModel: ListViewModel by viewModels()
    private var _listVPA: ListVPA? = null
    private val listVPA get() = _listVPA
    private val navigator by lazy { findNavController() }

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val listTabViewModel: ListTabViewModel by viewModels()

    override fun onItemClicked(linkItem: Link) {
        val action = ListFragmentDirections.actionListFragmentToOpenLinkFragment(linkItem.id.toInt())
        // 이전 페이지로 적용되도록 내비게이션 설정
        navigator.navigate(action, NavOptions.Builder()
            .setPopUpTo(R.id.listFragment, false)
            .build())
    }


    override fun initObserver() {
        repeatOnStarted {
            sharedViewModel.getSelectedItem().observe(viewLifecycleOwner) { selectedItem ->
                if (selectedItem.isNullOrEmpty()) {
                    // 저장된 탭 인덱스를 복원
                    sharedViewModel.getSelectedTabIndex().observe(viewLifecycleOwner) { index ->
                        index?.let {
                            binding.vpList.setCurrentItem(it, false)
                        }
                    }
                } else {
                    when (selectedItem) {
                        "recent" -> {
                            binding.vpList.setCurrentItem(2, false)
                        }
                        "like" -> {
                            binding.vpList.setCurrentItem(1, false)
                        }
                        "old" -> {
                            listTabViewModel.setSelectedLineup("past")
                            binding.vpList.setCurrentItem(0, false)
                        }
                        "wait" -> {
                            listTabViewModel.setSelectedLineup("recent")
                            binding.vpList.setCurrentItem(0, false)
                        }
                    }
                    // 이 후 다시 초기화해서 뷰페이저가 변경된 상태를 반영하도록 함
                    sharedViewModel.selectItem("")
                }
            }
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
            navigator.navigate(R.id.action_listFragment_to_alertFragment)
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

