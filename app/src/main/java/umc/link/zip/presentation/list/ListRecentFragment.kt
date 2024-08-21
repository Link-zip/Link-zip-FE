package umc.link.zip.presentation.list

import android.content.Context
import android.util.Log
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import umc.link.zip.presentation.base.BaseFragment

import umc.link.zip.R
import umc.link.zip.data.dto.list.request.UnreadRequest
import umc.link.zip.databinding.FragmentListRvBinding
import umc.link.zip.domain.model.list.Link
import umc.link.zip.domain.model.list.UnreadModel
import umc.link.zip.domain.model.list.Zip
import umc.link.zip.presentation.home.search.SearchFragmentDirections
import umc.link.zip.presentation.list.adapter.ListUnreadRVA
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.extension.setOnSingleClickListener
import umc.link.zip.util.network.UiState

@AndroidEntryPoint
class ListRecentFragment : BaseFragment<FragmentListRvBinding>(R.layout.fragment_list_rv) {
    private val navigator by lazy {
        findNavController()
    }
    private val viewModel: ListRecentViewModel by viewModels({requireParentFragment()})

    private val listUnreadLineDialogSharedViewModel: ListUnreadLineDialogSharedViewModel by viewModels()
    private val listUnreadListDialogSharedViewModel: ListUnreadListDialogSharedViewModel by viewModels()

    private val listTabViewModel : ListTabViewModel by viewModels({requireParentFragment()})

    private var userSelectedLineup = "recent"
    private var userSelectedListselect = ""

    private lateinit var onItemClickListener: OnItemClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is OnItemClickListener) {
            onItemClickListener = parentFragment as OnItemClickListener
        } else if (context is OnItemClickListener) {
            onItemClickListener = context
        } else {
            throw RuntimeException("$context must implement OnItemClickListener")
        }
    }

    private val listUnreadRVA by lazy {
        ListUnreadRVA(
            unreadLink = { link ->
                // 좋아요 상태 변경 시 동작
                viewModel.updateLikeStatusOnServer(link.id.toInt())
            },
            onClicked = { link ->
                onItemClickListener.onItemClicked(link)
            }
        )
    }

    override fun initObserver() {
        // lineup
        repeatOnStarted {
            listUnreadLineDialogSharedViewModel.selectedData.collectLatest { data ->
                userSelectedLineup = data
                setLineupOnDialog(userSelectedLineup)
            }
        }

        repeatOnStarted {
            listUnreadLineDialogSharedViewModel.dialogDismissed.collectLatest { dismissed ->
                if (dismissed) {
                    setLineupDismissDialog(userSelectedLineup)
                    listUnreadLineDialogSharedViewModel.resetDialogDismissed()
                }
            }
        }

        //listselect
        repeatOnStarted {
            listUnreadListDialogSharedViewModel.selectedData.collectLatest { data ->
                userSelectedListselect = data
                setListOnDialog(userSelectedListselect)
            }
        }

        repeatOnStarted {
            listUnreadListDialogSharedViewModel.dialogDismissed.collectLatest { dismissed ->
                if (dismissed) {
                    setListDismissDialog(userSelectedListselect)
                    listUnreadListDialogSharedViewModel.resetDialogDismissed()
                }
            }
        }

        //home Fragment -> List Fragment시 viewModel 설정해줘야 함.
        repeatOnStarted {
            listTabViewModel.selectedLineup.collectLatest { lineup ->
                userSelectedLineup = lineup
                setLineupDismissDialog(userSelectedLineup)
                repeatOnStarted {
                    listUnreadLineDialogSharedViewModel.resetDialogDismissed()
                }
                setLineupDismissDialog(userSelectedLineup)
                //list도 설정해줘야 함
                userSelectedListselect = ""
                repeatOnStarted {
                    setListDismissDialog(userSelectedListselect)
                    listUnreadListDialogSharedViewModel.resetDialogDismissed()
                }
            }
        }

        // StateFlow를 관찰하여 RecyclerView Adapter에 데이터를 전달
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            // 로딩 상태 처리
                            Log.d("ListLikeFragment", "Loading data")
                        }

                        is UiState.Success<*> -> {
                            val data = uiState.data as UnreadModel
                            Log.d("ListLikeFragment", "Fetched data size: ${data.links}")
                            listUnreadRVA.submitList(data.links)
                        }

                        is UiState.Error -> {
                            // 에러 상태 처리
                            Log.e("ListLikeFragment", "Error fetching data", uiState.error)
                        }

                        UiState.Empty -> Log.d("ListLikeFragment", "isEmpty")
                    }
                }
            }
        }
    }


    private fun fnUnreadRVApi(){
        val request = UnreadRequest(userSelectedLineup, userSelectedListselect) //sort, filter
        viewModel.fetchRecentList(request)
    }

    private fun setLineupOnDialog(selected: String) {
        when (selected) {
            "recent" -> {
                binding.ivListRvDrawerbtnLineup.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnLineup.context, R.drawable.drawerbtn_lineup_early_selected))
            }
            "past" -> {
                binding.ivListRvDrawerbtnLineup.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnLineup.context, R.drawable.drawerbtn_lineup_old_selected))
            }
            "dictionary" -> {
                binding.ivListRvDrawerbtnLineup.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnLineup.context, R.drawable.drawerbtn_lineup_ganada_selected))
            }
            "visit" -> {
                binding.ivListRvDrawerbtnLineup.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnLineup.context, R.drawable.drawerbtn_lineup_visit_selected))
            }
        }
        fnUnreadRVApi()
    }

    private fun setLineupDismissDialog(selected: String) {
        when (selected) {
            "recent" -> {
                binding.ivListRvDrawerbtnLineup.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnLineup.context, R.drawable.drawerbtn_lineup_early_unselected))
            }
            "past" -> {
                binding.ivListRvDrawerbtnLineup.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnLineup.context, R.drawable.drawerbtn_lineup_old_unselected))
            }
            "dictionary" -> {
                binding.ivListRvDrawerbtnLineup.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnLineup.context, R.drawable.drawerbtn_lineup_ganada_unselected))
            }
            "visit" -> {
                binding.ivListRvDrawerbtnLineup.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnLineup.context, R.drawable.drawerbtn_lineup_visit_unselected))
            }
        }
        fnUnreadRVApi()
    }

    private fun setListOnDialog(selected: String) {
        when (selected) {
            "" -> {
                binding.ivListRvDrawerbtnAll.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnAll.context, R.drawable.drawerbtn_allselect_selected))
            }
            "onlylink" -> {
                binding.ivListRvDrawerbtnAll.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnAll.context, R.drawable.drawerbtn_linkselect_selected))
            }
            "onlytext" -> {
                binding.ivListRvDrawerbtnAll.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnAll.context, R.drawable.drawerbtn_textlinkselect_selected))
            }
        }
        fnUnreadRVApi()
    }

    private fun setListDismissDialog(selected: String) {
        when (selected) {
            "" -> {
                binding.ivListRvDrawerbtnAll.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnAll.context, R.drawable.drawerbtn_allselect_unselected))
            }
            "onlylink" -> {
                binding.ivListRvDrawerbtnAll.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnAll.context, R.drawable.drawerbtn_linkselect_unselected))
            }
            "onlytext" -> {
                binding.ivListRvDrawerbtnAll.setImageDrawable(ContextCompat.getDrawable(binding.ivListRvDrawerbtnAll.context, R.drawable.drawerbtn_textlinkselect_unselected))
            }
        }
        fnUnreadRVApi()
    }

    // 다시 페이지로 돌아올 때 반영되게.
    override fun onResume() {
        super.onResume()
        setLineupDismissDialog(userSelectedLineup)
        setListDismissDialog(userSelectedListselect)
        //api 또 호출
        fnUnreadRVApi()
    }

    override fun initView() {
        initPostListRVAdapter()
        setupClickListener()
        setLineupDismissDialog(userSelectedLineup)
        setListDismissDialog(userSelectedListselect)
        fnUnreadRVApi()
    }

    private fun initPostListRVAdapter() {
        binding.rvList.adapter = listUnreadRVA
        /* 더미데이터
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
         */
    }

    private fun setupClickListener() {
        //한번만 클릭 허용
        binding.ivListRvDrawerbtnLineup.setOnSingleClickListener {
            setLineupOnDialog(userSelectedLineup)
            repeatOnStarted {
                listUnreadLineDialogSharedViewModel.setSelectedData(userSelectedLineup)
            }
            val dialogFragment = ListDialogueLineupFragment()
            dialogFragment.show(childFragmentManager, "ListDialogueLineupFragment")
        }
        binding.ivListRvDrawerbtnAll.setOnSingleClickListener {
            setListOnDialog(userSelectedListselect)
            repeatOnStarted {
                listUnreadListDialogSharedViewModel.setSelectedData(userSelectedListselect)
            }
            val dialogFragment = ListDialogueListselectFragment()
            dialogFragment.show(childFragmentManager, "ListDialogueListselectFragment")
        }
    }
}