package umc.link.zip.presentation.list

import android.content.Context
import android.util.Log
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
class ListLikeFragment : BaseFragment<FragmentListRvBinding>(R.layout.fragment_list_rv) {
    private val navigator by lazy {
        findNavController()
    }
    private val viewModel: ListLikeViewModel by viewModels({requireParentFragment()})

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
        viewModel.fetchLikeList(request)
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