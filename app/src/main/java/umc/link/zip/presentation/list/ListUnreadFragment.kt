package umc.link.zip.presentation.list

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import umc.link.zip.presentation.base.BaseFragment

import umc.link.zip.R
import umc.link.zip.databinding.FragmentListRvBinding
import umc.link.zip.domain.model.List.Link
import umc.link.zip.domain.model.List.Zip
import umc.link.zip.presentation.list.adapter.ListUnreadRVA
import umc.link.zip.util.extension.setOnSingleClickListener

@AndroidEntryPoint
class ListUnreadFragment : BaseFragment<FragmentListRvBinding>(R.layout.fragment_list_rv) {
    private val navigator by lazy {
        findNavController()
    }
    private val viewModel: ListUnreadViewModel by viewModels({requireParentFragment()})

    private val listUnreadLineDialogSharedViewModel: ListUnreadLineDialogSharedViewModel by activityViewModels()
    private val listUnreadListDialogSharedViewModel: ListUnreadListDialogSharedViewModel by activityViewModels()

    private var userSelectedLineup = "latest"
    private var userSelectedListselect = "all"
    private val listUnreadRVA by lazy {
        ListUnreadRVA{
            /* 링크 페이지 연결
            linkId ->
            val action =
                ListUnreadFragmentDirections.actionListUnreadFragmentToLinkFragment(linkId)
            navigator.navigate(action)
             */
        }
    }

    override fun initObserver() {
        // lineup
        viewLifecycleOwner.lifecycleScope.launch {
            listUnreadLineDialogSharedViewModel.selectedData.collectLatest { data ->
                userSelectedLineup = data
                setLineupOnDialog(userSelectedLineup)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            listUnreadLineDialogSharedViewModel.dialogDismissed.collectLatest { dismissed ->
                if (dismissed) {
                    setLineupDismissDialog(userSelectedLineup)
                    listUnreadLineDialogSharedViewModel.resetDialogDismissed()
                }
            }
        }

        //listselect
        viewLifecycleOwner.lifecycleScope.launch {
            listUnreadListDialogSharedViewModel.selectedData.collectLatest { data ->
                userSelectedListselect = data
                setListOnDialog(userSelectedListselect)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            listUnreadListDialogSharedViewModel.dialogDismissed.collectLatest { dismissed ->
                if (dismissed) {
                    setListDismissDialog(userSelectedListselect)
                    listUnreadListDialogSharedViewModel.resetDialogDismissed()
                }
            }
        }
    }

    private fun setLineupOnDialog(selected: String) {
        when (selected) {
            "latest" -> {
                binding.ivListRvDrawerbtnLineup.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnLineup.context, R.drawable.drawerbtn_lineup_early_selected))
            }
            "oldest" -> {
                binding.ivListRvDrawerbtnLineup.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnLineup.context, R.drawable.drawerbtn_lineup_old_selected))
            }
            "ganada" -> {
                binding.ivListRvDrawerbtnLineup.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnLineup.context, R.drawable.drawerbtn_lineup_ganada_selected))
            }
            "visit" -> {
                binding.ivListRvDrawerbtnLineup.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnLineup.context, R.drawable.drawerbtn_lineup_visit_selected))
            }
        }
    }

    private fun setLineupDismissDialog(selected: String) {
        when (selected) {
            "latest" -> {
                binding.ivListRvDrawerbtnLineup.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnLineup.context, R.drawable.drawerbtn_lineup_early_unselected))
            }
            "oldest" -> {
                binding.ivListRvDrawerbtnLineup.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnLineup.context, R.drawable.drawerbtn_lineup_old_unselected))
            }
            "ganada" -> {
                binding.ivListRvDrawerbtnLineup.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnLineup.context, R.drawable.drawerbtn_lineup_ganada_unselected))
            }
            "visit" -> {
                binding.ivListRvDrawerbtnLineup.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnLineup.context, R.drawable.drawerbtn_lineup_visit_unselected))
            }
        }
    }

    private fun setListOnDialog(selected: String) {
        when (selected) {
            "all" -> {
                binding.ivListRvDrawerbtnAll.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnAll.context, R.drawable.drawerbtn_allselect_selected))
            }
            "link" -> {
                binding.ivListRvDrawerbtnAll.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnAll.context, R.drawable.drawerbtn_linkselect_selected))
            }
            "text" -> {
                binding.ivListRvDrawerbtnAll.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnAll.context, R.drawable.drawerbtn_textlinkselect_selected))
            }
        }
    }

    private fun setListDismissDialog(selected: String) {
        when (selected) {
            "all" -> {
                binding.ivListRvDrawerbtnAll.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnAll.context, R.drawable.drawerbtn_allselect_unselected))
            }
            "link" -> {
                binding.ivListRvDrawerbtnAll.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnAll.context, R.drawable.drawerbtn_linkselect_unselected))
            }
            "text" -> {
                binding.ivListRvDrawerbtnAll.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnAll.context, R.drawable.drawerbtn_textlinkselect_unselected))
            }
        }
    }



    override fun initView() {
        initPostListRVAdapter()
        setupClickListener()
        setLineupDismissDialog(userSelectedLineup)
        setListDismissDialog(userSelectedListselect)
    }

    private fun initPostListRVAdapter() {
        binding.rvList.adapter = listUnreadRVA
        val zip = Zip("1", "Zip Title", "blue")
        val zip2 = Zip("1", "인사이트", "yellow")
        val list = listOf(
            Link("1", "테스트입니다1", "url", "텍스트", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff", 1, "2024.7.28", zip),
            Link("2", "마이크로/나노 인플루언서 마케팅 전략 ## 최대 2줄", "url", "", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff", 0, "2024.7.29", zip2),
            Link("3", "테스트입니다3", "url", "텍스트", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff", 1, "2024.7.30", zip),
            Link("4", "테스트입니다4", "url", "", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff", 0, "2024.7.31", zip),
            Link("5", "테스트입니다5", "url", "텍스트", R.drawable.btn_bottomnav_create.toString(), 1, "2024.7.28", zip),
            Link("6", "테스트입니다6", "url", "텍스트", R.drawable.btn_bottomnav_create.toString(), 1, "2024.7.29", zip),
            Link("7", "테스트입니다7", "url", "텍스트", R.drawable.btn_bottomnav_create.toString(), 1, "2024.7.30", zip),
            Link("8", "테스트입니다8", "url", "텍스트", R.drawable.btn_bottomnav_create.toString(), 1, "2024.7.31", zip)
        )
        listUnreadRVA.submitList(list)
    }

    private fun setupClickListener() {
        //한번만 클릭 허용
        binding.ivListRvDrawerbtnLineup.setOnSingleClickListener {
            setLineupOnDialog(userSelectedLineup)
            viewLifecycleOwner.lifecycleScope.launch {
                listUnreadLineDialogSharedViewModel.setSelectedData(userSelectedLineup)
            }
            val dialogFragment = ListDialogueLineupFragment()
            dialogFragment.show(parentFragmentManager, "ListDialogueLineupFragment")
        }
        binding.ivListRvDrawerbtnAll.setOnSingleClickListener {
            setListOnDialog(userSelectedListselect)
            viewLifecycleOwner.lifecycleScope.launch {
                listUnreadListDialogSharedViewModel.setSelectedData(userSelectedListselect)
            }
            val dialogFragment = ListDialogueListselectFragment()
            dialogFragment.show(parentFragmentManager, "ListDialogueListselectFragment")
        }
    }
}