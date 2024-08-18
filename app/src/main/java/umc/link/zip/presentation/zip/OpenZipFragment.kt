package umc.link.zip.presentation.zip

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.databinding.FragmentOpenzipBinding
import umc.link.zip.domain.model.link.LinkGetItemModel
import umc.link.zip.domain.model.link.LinkGetModel
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.zip.adapter.OpenZipDialogueLineupFragment
import umc.link.zip.presentation.zip.adapter.OpenZipDialogueListSelectFragment
import umc.link.zip.presentation.zip.adapter.OpenZipItemAdapter
import umc.link.zip.presentation.zip.adapter.OpenZipLineDialogSharedViewModel
import umc.link.zip.presentation.zip.adapter.OpenZipListDialogSharedViewModel
import umc.link.zip.presentation.zip.adapter.OpenZipViewModel
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.extension.setOnSingleClickListener
import umc.link.zip.util.network.UiState

@AndroidEntryPoint
class OpenZipFragment : BaseFragment<FragmentOpenzipBinding>(R.layout.fragment_openzip) {
    private val navigator by lazy {
        findNavController()
    }

    private val openZipLineDialogSharedViewModel: OpenZipLineDialogSharedViewModel by viewModels()
    private val openZipListDialogSharedViewModel: OpenZipListDialogSharedViewModel by viewModels()

    private val viewModel: OpenZipViewModel by viewModels()
    private var isEditMode = false
    private var isAllSelectedMode = false

    private var userSelectedLineup = "newest"
    private var userSelectedListselect = ""

    val args : OpenZipFragmentArgs by navArgs()
    private val zip_id by lazy {
        args.zipId
    }

    private val adapter by lazy {
        OpenZipItemAdapter{ link ->
            /* 링크 페이지 연결
            linkId ->
            val action =
                ListUnreadFragmentDirections.actionListUnreadFragmentToLinkFragment(linkId)
            navigator.navigate(action)
             */
            // 좋아요 상태 변경 시 동작
        }
    }

    override fun initObserver() {
        // lineup
        repeatOnStarted {
            openZipLineDialogSharedViewModel.selectedData.collectLatest { data ->
                userSelectedLineup = data
                setLineupOnDialog(userSelectedLineup)
            }
        }

        repeatOnStarted {
            openZipLineDialogSharedViewModel.dialogDismissed.collectLatest { dismissed ->
                if (dismissed) {
                    setLineupDismissDialog(userSelectedLineup)
                    openZipLineDialogSharedViewModel.resetDialogDismissed()
                }
            }
        }

        //listselect
        repeatOnStarted {
            openZipListDialogSharedViewModel.selectedData.collectLatest { data ->
                userSelectedListselect = data
                setListOnDialog(userSelectedListselect)
            }
        }

        repeatOnStarted {
            openZipListDialogSharedViewModel.dialogDismissed.collectLatest { dismissed ->
                if (dismissed) {
                    setListDismissDialog(userSelectedListselect)
                    openZipListDialogSharedViewModel.resetDialogDismissed()
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
                            Log.d("OpenZipFragment", "Loading data")
                        }

                        is UiState.Success<*> -> {
                            val data = uiState.data as LinkGetModel
                            Log.d("OpenZipFragment", "Fetched data size: ${data.link_data}")
                            adapter.submitList(data.link_data)
                        }

                        is UiState.Error -> {
                            // 에러 상태 처리
                            Log.e("OpenZipFragment", "Error fetching data", uiState.error)
                        }

                        UiState.Empty -> Log.d("OpenZipFragment", "isEmpty")
                    }
                }
            }
        }
    }

    private fun getLinkListApi(){
        //zip_id, tag, sortOrder
        viewModel.getLinkList(zip_id,  userSelectedListselect, userSelectedLineup)
        Log.d("OpenZipFragment", "getApi 호출됨")
    }

    private fun setLineupOnDialog(selected: String) {
        when (selected) {
            "newest" -> {
                binding.fragmentOpenzipRecentIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipRecentIv.context, R.drawable.drawerbtn_lineup_early_selected))
            }
            "oldest" -> {
                binding.fragmentOpenzipRecentIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipRecentIv.context, R.drawable.drawerbtn_lineup_old_selected))
            }
            "alphabetical" -> {
                binding.fragmentOpenzipRecentIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipRecentIv.context, R.drawable.drawerbtn_lineup_ganada_selected))
            }
            "most_visited" -> {
                binding.fragmentOpenzipRecentIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipRecentIv.context, R.drawable.drawerbtn_lineup_visit_selected))
            }
        }
        getLinkListApi()
    }

    private fun setLineupDismissDialog(selected: String) {
        when (selected) {
            "latest" -> {
                binding.fragmentOpenzipRecentIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipRecentIv.context, R.drawable.drawerbtn_lineup_early_unselected))
            }
            "oldest" -> {
                binding.fragmentOpenzipRecentIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipRecentIv.context, R.drawable.drawerbtn_lineup_old_unselected))
            }
            "alphabetical" -> {
                binding.fragmentOpenzipRecentIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipRecentIv.context, R.drawable.drawerbtn_lineup_ganada_unselected))
            }
            "most_visited" -> {
                binding.fragmentOpenzipRecentIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipRecentIv.context, R.drawable.drawerbtn_lineup_visit_unselected))
            }
        }
        getLinkListApi()
    }

    private fun setListOnDialog(selected: String) {
        when (selected) {
            "" -> {
                binding.fragmentOpenzipAllIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipAllIv.context, R.drawable.drawerbtn_allselect_selected))
            }
            "link" -> {
                binding.fragmentOpenzipAllIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipAllIv.context, R.drawable.drawerbtn_linkselect_selected))
            }
            "text" -> {
                binding.fragmentOpenzipAllIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipAllIv.context, R.drawable.drawerbtn_textlinkselect_selected))
            }
        }
        getLinkListApi()
    }

    private fun setListDismissDialog(selected: String) {
        when (selected) {
            "" -> {
                binding.fragmentOpenzipAllIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipAllIv.context, R.drawable.drawerbtn_allselect_unselected))
            }
            "link" -> {
                binding.fragmentOpenzipAllIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipAllIv.context, R.drawable.drawerbtn_linkselect_unselected))
            }
            "text" -> {
                binding.fragmentOpenzipAllIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipAllIv.context, R.drawable.drawerbtn_textlinkselect_unselected))
            }
        }
        getLinkListApi()
    }

    private val editClickListener = View.OnClickListener {
        if (isAllSelectedMode) {
            setEditMode()
        } else {
            toggleEditMode()
        }
    }

    private val allSelectedListener = View.OnClickListener {
        if (isEditMode) {
            setAllSelectedMode()
            updateBackgroundColorOfItems()
        } else if (isAllSelectedMode) {
            setEditMode()
            resetBackgroundColorOfItems()
        }
    }



    override fun onResume() {
        super.onResume()
        setLineupDismissDialog(userSelectedLineup)
        setListDismissDialog(userSelectedListselect)
        viewModel.getLinkList(zip_id, userSelectedListselect, userSelectedLineup)
        Log.d("OpenZipFragment","$zip_id")
    }

    override fun initView() {
        setupClickListener()
        setLineupDismissDialog(userSelectedLineup)
        setListDismissDialog(userSelectedListselect)
        viewModel.getLinkList(zip_id, userSelectedListselect, userSelectedLineup)

        setupRecyclerView()

        val toolbarBackBtn = binding.ivOpenzipToolbarBack
        toolbarBackBtn.setOnClickListener {
            navigator.navigate(R.id.action_fragmentOpenZip_to_fragmentZip)
            Log.d("FragmentOpenZip", "Navigated to FragmentZip")
        }

        val zipEditBtn = binding.fragmentOpenzipImageView1
        zipEditBtn.setOnClickListener {
            navigator.navigate(R.id.action_fragmentOpenZip_to_fragmentEditZip)
            Log.d("FragmentOpenZip", "Navigated to FragmentEditZip")
        }

        binding.allSelectedBtn.setOnClickListener(allSelectedListener)
        binding.allSelectedTv.setOnClickListener(allSelectedListener)

        // Initialize in NormalMode
        setNormalMode()
    }

    private fun setupRecyclerView() {
        binding.fragmentOpenzipItemLinkRv.layoutManager = LinearLayoutManager(context)
        binding.fragmentOpenzipItemLinkRv.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun toggleEditMode() {
        if (isEditMode) {
            setNormalMode()
        } else {
            setEditMode()
        }
    }

    // Functions to handle different modes
    private fun setEditMode() {
        isEditMode = true
        isAllSelectedMode = false
        resetAllSelectedMode()
        binding.fragmentOpenzipAllIv.visibility = View.GONE
        binding.fragmentOpenzipRecentIv.visibility = View.GONE
        binding.allSelectedBtn.visibility = View.VISIBLE
        binding.allSelectedTv.visibility = View.VISIBLE
        binding.fragmentOpenzipEditIv.text = "완료"
        binding.fragmentOpenzipEditIv.setTextColor(Color.parseColor("#1191AD"))
        binding.cvMypageProfileUserInfoBoxBg.visibility = View.VISIBLE
        binding.clProfilesetFinishBtn2.visibility = View.VISIBLE
    }

    private fun setNormalMode() {
        isEditMode = false
        isAllSelectedMode = false
        resetAllSelectedMode()
        resetBackgroundColorOfItems()
        binding.fragmentOpenzipAllIv.visibility = View.VISIBLE
        binding.fragmentOpenzipRecentIv.visibility = View.VISIBLE
        binding.allSelectedBtn.visibility = View.GONE
        binding.allSelectedTv.visibility = View.GONE
        binding.fragmentOpenzipEditIv.text = "편집"
        binding.fragmentOpenzipEditIv.setTextColor(Color.parseColor("#000000"))
        binding.cvMypageProfileUserInfoBoxBg.visibility = View.GONE
        binding.clProfilesetFinishBtn2.visibility = View.GONE
    }

    private fun setAllSelectedMode() {
        isAllSelectedMode = true
        isEditMode = false
        binding.allSelectedBtn.setImageResource(R.drawable.ic_checkunselected_blue)
        binding.allSelectedTv.setTextColor(Color.parseColor("#1191AD"))
        binding.fragmentMakezipMakeBtn.setBackgroundResource(R.drawable.btn_openzip_movezipact_black)
        binding.fragmentMakezipMakeBtn.setTextColor(Color.parseColor("#000000"))
        binding.fragmentZipMakeBtn2.setBackgroundResource(R.drawable.shape_rect_005773_fill)
        binding.ivProfilesetBlueshadow.visibility = View.VISIBLE
        binding.ivProfilesetGrayshadow.visibility = View.GONE


        // Update click listeners to go back to EditMode from AllSelectedMode
        binding.fragmentOpenzipEditIv.setOnClickListener(editClickListener)
        binding.allSelectedBtn.setOnClickListener(editClickListener)
    }

    private fun resetAllSelectedMode() {
        // Reset UI elements to default states
        binding.allSelectedBtn.setImageResource(R.drawable.ic_checkunselected_black)
        binding.allSelectedTv.setTextColor(Color.parseColor("#000000"))
        binding.fragmentMakezipMakeBtn.setTextColor(Color.parseColor("#999999"))
        binding.fragmentMakezipMakeBtn.setBackgroundResource(R.drawable.btn_openzip_movezipinact)
        binding.fragmentZipMakeBtn2.setBackgroundResource(R.drawable.shape_rect_8_666666_fill)
        binding.ivProfilesetBlueshadow.visibility = View.GONE
        binding.ivProfilesetGrayshadow.visibility = View.VISIBLE
    }

    private fun updateBackgroundColorOfItems() {
        val recyclerView = binding.fragmentOpenzipItemLinkRv
        for (i in 0 until recyclerView.childCount) {
            val itemView = recyclerView.getChildAt(i)
            val itemMainLayout: ConstraintLayout = itemView.findViewById(R.id.item_link_main_cl)
            itemMainLayout.setBackgroundColor(Color.parseColor("#F1F0FF"))
        }
        binding.fragmentOpenzipShadow.setBackgroundResource(R.drawable.shadow_zip_bg3)
    }

    private fun resetBackgroundColorOfItems() {
        val recyclerView = binding.fragmentOpenzipItemLinkRv
        for (i in 0 until recyclerView.childCount) {
            val itemView = recyclerView.getChildAt(i)
            val itemMainLayout: ConstraintLayout = itemView.findViewById(R.id.item_link_main_cl)
            itemMainLayout.setBackgroundColor(Color.parseColor("#FBFBFB"))
        }
        binding.fragmentOpenzipShadow.setBackgroundResource(R.drawable.shadow_zip_bg2)
    }

    private fun setupClickListener() {
        //한번만 클릭 허용
        binding.fragmentOpenzipRecentIv.setOnSingleClickListener {
            setLineupOnDialog(userSelectedLineup)
            repeatOnStarted {
                openZipLineDialogSharedViewModel.setSelectedData(userSelectedLineup)
            }
            val dialogFragment = OpenZipDialogueLineupFragment()
            dialogFragment.show(childFragmentManager, "OpenZipDialogueLineupFragment")
        }
        binding.fragmentOpenzipAllIv.setOnSingleClickListener {
            setListOnDialog(userSelectedListselect)
            repeatOnStarted {
                openZipListDialogSharedViewModel.setSelectedData(userSelectedListselect)
            }
            val dialogFragment = OpenZipDialogueListSelectFragment()
            dialogFragment.show(childFragmentManager, "OpenZipDialogueListSelectFragment")
        }
    }
}
