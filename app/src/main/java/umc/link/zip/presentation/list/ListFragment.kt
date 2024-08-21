package umc.link.zip.presentation.list

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
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
        // openLinkFragment로 네비게이션
        val action = ListFragmentDirections.actionListFragmentToOpenLinkFragment(linkItem.id.toInt())
        navigator.navigate(action)
    }

    override fun initObserver() {
        repeatOnStarted {
            sharedViewModel.getSelectedItem().observe(viewLifecycleOwner) { selectedItem ->
                Log.d("ListFragment", selectedItem)
                when (selectedItem) {
                    "recent" -> {
                        repeatOnStarted {
                            listTabViewModel.setSelectedLineup("recent")
                        }
                        binding.vpList.setCurrentItem(2, false)
                    }
                    "like" -> {
                        repeatOnStarted {
                            listTabViewModel.setSelectedLineup("recent")
                        }
                        binding.vpList.setCurrentItem(1, false)
                    }
                    "old" -> {
                        repeatOnStarted {
                            listTabViewModel.setSelectedLineup("past")
                        }
                        binding.vpList.setCurrentItem(0, false)
                    }
                    "wait" -> {
                        repeatOnStarted {
                            listTabViewModel.setSelectedLineup("recent")
                        }
                        binding.vpList.setCurrentItem(0, false)
                    }
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

