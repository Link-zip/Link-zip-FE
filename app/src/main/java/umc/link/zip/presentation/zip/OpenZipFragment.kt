package umc.link.zip.presentation.zip

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.databinding.FragmentOpenzipBinding
import umc.link.zip.domain.model.link.LinkGetModel
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.zip.adapter.LinkDeleteDialogueFragment
import umc.link.zip.presentation.zip.adapter.OpenZipDialogueLineupFragment
import umc.link.zip.presentation.zip.adapter.OpenZipDialogueListSelectFragment
import umc.link.zip.presentation.zip.adapter.OpenZipItemAdapter
import umc.link.zip.presentation.zip.adapter.OpenZipLineDialogSharedViewModel
import umc.link.zip.presentation.zip.adapter.OpenZipListDialogSharedViewModel
import umc.link.zip.presentation.zip.adapter.OpenZipMoveDialogFragment
import umc.link.zip.presentation.zip.adapter.OpenZipMoveDialogSharedViewModel
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
    private val sharedViewModel: OpenZipMoveDialogSharedViewModel by viewModels()
    private val viewModel: OpenZipViewModel by viewModels()

    private var isEditMode = false
    private var isAllSelectedMode = false
    private var userSelectedLineup = "newest"
    private var userSelectedListselect = ""

    private val zip_id by lazy { arguments?.getInt("zipId") }
    private val zip_title by lazy { arguments?.getString("zipTitle") }
    private val zip_color by lazy { arguments?.getString("zipColor") }
    private val zip_linkCount by lazy { arguments?.getInt("zipLinkCount") }

    private val adapter by lazy {
        OpenZipItemAdapter(
            onItemSelected = { linkItem, isSelected ->
                if (isSelected) {
                    switchToSelectedMode()
                    binding.cvMypageProfileUserInfoBoxBg.setOnSingleClickListener {
                        val dialogFragment = OpenZipMoveDialogFragment.newInstance(zip_id ?: 0, linkItem.id)
                        dialogFragment.dismissListener = object : OnDialogDismissListener {
                            override fun onDialogDismiss() {
                                getLinkListApi()
                                showCustomToast()
                                setNormalMode()
                            }
                        }
                        dialogFragment.show(childFragmentManager, "OpenZipMoveDialogFragment")
                    }
                } else {
                    resetAllSelectedMode()
                }
            },
            onSelectionCleared = {
                resetAllSelectedMode()
            },
            onLikeClicked = { linkItem ->
                viewModel.updateLikeStatusOnServer(linkItem.id)
            },
            onBackgroundChangeRequested = { highlight ->
                if (highlight) {
                    binding.fragmentOpenzipShadow.setBackgroundResource(R.drawable.shadow_zip_bg3)
                } else {
                    binding.fragmentOpenzipShadow.setBackgroundResource(R.drawable.shadow_zip_bg2)
                }
            }
        )
    }

    private fun setupRecyclerView() {
        binding.fragmentOpenzipItemLinkRv.layoutManager = LinearLayoutManager(requireContext())
        binding.fragmentOpenzipItemLinkRv.adapter = adapter
    }

    override fun initObserver() {
        // Observers for different dialog states and data updates
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

        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            Log.d("OpenZipFragment", "Loading data")
                        }
                        is UiState.Success<*> -> {
                            val data = uiState.data as LinkGetModel
                            adapter.submitList(data.link_data)
                            Log.d("OpenZipFragment", "Fetched data size: ${data.link_data}")
                        }
                        is UiState.Error -> {
                            Log.e("OpenZipFragment", "Error fetching data", uiState.error)
                        }
                        UiState.Empty -> {
                            Log.e("OpenZipFragment", "UiState.Empty")
                        }
                    }
                }
            }
        }
    }

    private fun getLinkListApi() {
        viewModel.getLinkList(zip_id!!, userSelectedListselect, userSelectedLineup)
        Log.d("OpenZipFragment", "getApi 호출됨")
    }

    private fun setLineupOnDialog(selected: String) {
        when (selected) {
            "newest" -> binding.fragmentOpenzipRecentIv.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.fragmentOpenzipRecentIv.context,
                    R.drawable.drawerbtn_lineup_early_selected
                )
            )
            "oldest" -> binding.fragmentOpenzipRecentIv.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.fragmentOpenzipRecentIv.context,
                    R.drawable.drawerbtn_lineup_old_selected
                )
            )
            "alphabetical" -> binding.fragmentOpenzipRecentIv.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.fragmentOpenzipRecentIv.context,
                    R.drawable.drawerbtn_lineup_ganada_selected
                )
            )
            "most_visited" -> binding.fragmentOpenzipRecentIv.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.fragmentOpenzipRecentIv.context,
                    R.drawable.drawerbtn_lineup_visit_selected
                )
            )
        }
        getLinkListApi()
    }

    private fun setLineupDismissDialog(selected: String) {
        when (selected) {
            "latest" -> binding.fragmentOpenzipRecentIv.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.fragmentOpenzipRecentIv.context,
                    R.drawable.drawerbtn_lineup_early_unselected
                )
            )
            "oldest" -> binding.fragmentOpenzipRecentIv.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.fragmentOpenzipRecentIv.context,
                    R.drawable.drawerbtn_lineup_old_unselected
                )
            )
            "alphabetical" -> binding.fragmentOpenzipRecentIv.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.fragmentOpenzipRecentIv.context,
                    R.drawable.drawerbtn_lineup_ganada_unselected
                )
            )
            "most_visited" -> binding.fragmentOpenzipRecentIv.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.fragmentOpenzipRecentIv.context,
                    R.drawable.drawerbtn_lineup_visit_unselected
                )
            )
        }
        getLinkListApi()
    }

    private fun setListOnDialog(selected: String) {
        when (selected) {
            "" -> binding.fragmentOpenzipAllIv.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.fragmentOpenzipAllIv.context,
                    R.drawable.drawerbtn_allselect_selected
                )
            )
            "link" -> binding.fragmentOpenzipAllIv.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.fragmentOpenzipAllIv.context,
                    R.drawable.drawerbtn_linkselect_selected
                )
            )
            "text" -> binding.fragmentOpenzipAllIv.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.fragmentOpenzipAllIv.context,
                    R.drawable.drawerbtn_textlinkselect_selected
                )
            )
        }
        getLinkListApi()
    }

    private fun setListDismissDialog(selected: String) {
        when (selected) {
            "" -> binding.fragmentOpenzipAllIv.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.fragmentOpenzipAllIv.context,
                    R.drawable.drawerbtn_allselect_unselected
                )
            )
            "link" -> binding.fragmentOpenzipAllIv.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.fragmentOpenzipAllIv.context,
                    R.drawable.drawerbtn_linkselect_unselected
                )
            )
            "text" -> binding.fragmentOpenzipAllIv.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.fragmentOpenzipAllIv.context,
                    R.drawable.drawerbtn_textlinkselect_unselected
                )
            )
        }
        getLinkListApi()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 레이아웃 파라미터를 설정하는 부분은 여기로 이동
        if (zip_color == "default") {
            binding.fragmentOpenzipImageView1.visibility = View.GONE

            val layoutParams = binding.fragmentOpenzipInsiteIv.layoutParams as ViewGroup.MarginLayoutParams
            val marginInDp = 5
            val scale = resources.displayMetrics.density
            val marginInPx = (marginInDp * scale).toInt()

            layoutParams.topMargin = marginInPx
            binding.fragmentOpenzipInsiteIv.layoutParams = layoutParams
        }

        // Set up initial view state
        binding.fragmentOpenzipZipTitle.text = zip_title?.take(5)
        binding.fragmentOpenzipInsiteTv.text = zip_title
        binding.fragmentOpenzipLinkCountTv2.text = "$zip_linkCount 개"
        binding.fragmentOpenzipInsiteIv.setBackgroundResource(setBackgroundResource(zip_color.toString()))

        if (zip_linkCount == 0) {
            binding.fragmentOpenzipItemLinkRv.visibility = View.GONE
            binding.clListNoneBackgroundCl.visibility = View.VISIBLE
        } else {
            binding.fragmentOpenzipItemLinkRv.visibility = View.VISIBLE
            binding.clListNoneBackgroundCl.visibility = View.GONE
        }

        val action = zip_linkCount?.let {
            OpenZipFragmentDirections.actionFragmentOpenZipToFragmentEditZip(
                zip_id ?: 0,
                zip_color.toString(),
                zip_title.toString(),
                it
            )
        }
        binding.fragmentOpenzipImageView1.setOnClickListener {
            action?.let { it1 -> findNavController().navigate(it1) }
        }
    }

    override fun onResume() {
        super.onResume()
        getLinkListApi()
    }

    override fun initView() {
        setupClickListener()
        setupRecyclerView()

        binding.ivOpenzipToolbarBack.setOnClickListener {
            navigator.navigate(R.id.action_fragmentOpenZip_to_fragmentZip)
        }

        binding.clProfilesetFinishBtn2.setOnClickListener {
            if (isEditMode) {
                val selectedIds = adapter.getSelectedLinkIds() ?: emptyList()
                if (selectedIds.isNotEmpty()) {
                    val deleteDialog = LinkDeleteDialogueFragment.newInstance(selectedIds)
                    deleteDialog.show(childFragmentManager, "ZipDeleteDialogueFragment")

                    deleteDialog.dismissListener = object : OnDialogDismissListener {
                        override fun onDialogDismiss() {
                            getLinkListApi()
                            showCustomToast2()
                            setNormalMode()
                        }
                    }
                } else {
                    Toast.makeText(context, "삭제할 항목을 선택하세요.", Toast.LENGTH_SHORT).show()
                }
            } else {
                findNavController().navigate(R.id.action_fragmentOpenZip_to_fragmentZip)
            }
        }

        binding.fragmentOpenzipEditIv.setOnClickListener {
            toggleEditMode()
            adapter.toggleEditMode()
        }

        binding.allSelectedBtn.setOnClickListener(allSelectedListener)
        binding.allSelectedTv.setOnClickListener(allSelectedListener)
        setNormalMode()
    }

    private fun setBackgroundResource(color: String): Int {
        return when (color.lowercase()) {
            "yellow" -> R.drawable.ic_zip_shadow_1
            "lightgreen" -> R.drawable.ic_zip_shadow_2
            "green" -> R.drawable.ic_zip_shadow_3
            "lightblue" -> R.drawable.ic_zip_shadow_4
            "blue" -> R.drawable.ic_zip_shadow_5
            "darkpurple" -> R.drawable.ic_zip_shadow_6
            "purple" -> R.drawable.ic_zip_shadow_7
            else -> R.drawable.ic_zip_clip_shadow
        }
    }

    private fun toggleEditMode() {
        isEditMode = !isEditMode
        if (isEditMode) {
            setEditMode()
        } else {
            setNormalMode()
        }
    }

    private fun setEditMode() {
        isEditMode = true
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
        binding.fragmentOpenzipAllIv.visibility = View.VISIBLE
        binding.fragmentOpenzipRecentIv.visibility = View.VISIBLE
        binding.allSelectedBtn.visibility = View.GONE
        binding.allSelectedTv.visibility = View.GONE
        binding.fragmentOpenzipEditIv.text = "편집"
        binding.fragmentOpenzipEditIv.setTextColor(Color.parseColor("#000000"))
        binding.cvMypageProfileUserInfoBoxBg.visibility = View.GONE
        binding.clProfilesetFinishBtn2.visibility = View.GONE
        binding.fragmentOpenzipShadow.setBackgroundResource(R.drawable.shadow_zip_bg2)
        adapter.clearSelections()
    }

    private val allSelectedListener = View.OnClickListener {
        if (isEditMode) {
            isAllSelectedMode = !isAllSelectedMode
            if (isAllSelectedMode) {
                setAllSelectedMode()
                binding.fragmentOpenzipShadow.setBackgroundResource(R.drawable.shadow_zip_bg3)
                adapter.logSelectedItems()
            } else {
                adapter.logdeSelectedItems()
                binding.fragmentOpenzipShadow.setBackgroundResource(R.drawable.shadow_zip_bg2)
                resetAllSelectedMode()
            }
        }
    }

    private fun setAllSelectedMode() {
        isAllSelectedMode = true
        binding.allSelectedBtn.setImageResource(R.drawable.ic_checkunselected_blue)
        binding.allSelectedTv.setTextColor(Color.parseColor("#1191AD"))
        adapter.selectAllItems()
    }

    private fun resetAllSelectedMode() {
        isAllSelectedMode = false
        binding.allSelectedBtn.setImageResource(R.drawable.ic_checkunselected_black)
        binding.allSelectedTv.setTextColor(Color.parseColor("#000000"))
        adapter.clearSelections()
    }

    private fun switchToSelectedMode() {
        binding.fragmentMakezipMakeBtn.setBackgroundResource(R.drawable.btn_openzip_movezipact_black)
        binding.fragmentMakezipMakeBtn.setTextColor(Color.parseColor("#000000"))
        binding.fragmentZipMakeBtn2.setBackgroundResource(R.drawable.shape_rect_005773_fill)
        binding.ivProfilesetBlueshadow.visibility = View.VISIBLE
        binding.ivProfilesetGrayshadow.visibility = View.GONE
    }

    private fun setupClickListener() {
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

    private fun showCustomToast() {
        val inflater = LayoutInflater.from(requireActivity())
        val layout = inflater.inflate(R.layout.custom_toast, null)
        val tv = layout.findViewById<TextView>(R.id.tvSample)
        tv.text = "링크 이동 완료"

        val toast = Toast(requireActivity()).apply {
            duration = Toast.LENGTH_SHORT
            view = layout
            setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 230)
        }
        toast.show()
    }

    private fun showCustomToast2() {
        val inflater = LayoutInflater.from(requireActivity())
        val layout = inflater.inflate(R.layout.custom_toast, null)
        val tv = layout.findViewById<TextView>(R.id.tvSample)
        tv.text = "링크 삭제 완료"

        val toast = Toast(requireActivity()).apply {
            duration = Toast.LENGTH_SHORT
            view = layout
            setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 230)
        }
        toast.show()
    }
}
