package umc.link.zip.presentation.zip

import OpenZipViewModel
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.databinding.FragmentOpenzipBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.list.ListDialogueLineupFragment
import umc.link.zip.presentation.list.ListDialogueListselectFragment
import umc.link.zip.presentation.zip.adapter.OpenZipItemAdapter.OpenZipItemAdapter
import umc.link.zip.presentation.zip.adapter.OpenZipLineDialogSharedViewModel
import umc.link.zip.presentation.zip.adapter.OpenZipListDialogSharedViewModel
import umc.link.zip.util.extension.setOnSingleClickListener

@AndroidEntryPoint
class FragmentOpenZip : BaseFragment<FragmentOpenzipBinding>(R.layout.fragment_openzip) {
    private val navigator by lazy {
        findNavController()
    }

    private val openZipLineDialogSharedViewModel: OpenZipLineDialogSharedViewModel by activityViewModels()
    private val openZipListDialogSharedViewModel: OpenZipListDialogSharedViewModel by activityViewModels()

    private val viewModel: OpenZipViewModel by viewModels({requireParentFragment()})
    private var adapter: OpenZipItemAdapter? = null
    private var isEditMode = false
    private var isAllSelectedMode = false

    private var userSelectedLineup = "latest"
    private var userSelectedListselect = "all"

    override fun initObserver() {
        // lineup
        viewLifecycleOwner.lifecycleScope.launch {
            openZipLineDialogSharedViewModel.selectedData.collectLatest { data ->
                userSelectedLineup = data
                setLineupOnDialog(userSelectedLineup)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            openZipLineDialogSharedViewModel.dialogDismissed.collectLatest { dismissed ->
                if (dismissed) {
                    setLineupDismissDialog(userSelectedLineup)
                    openZipLineDialogSharedViewModel.resetDialogDismissed()
                }
            }
        }

        // listselect
        viewLifecycleOwner.lifecycleScope.launch {
            openZipListDialogSharedViewModel.selectedData.collectLatest { data ->
                userSelectedListselect = data
                setListOnDialog(userSelectedListselect)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            openZipListDialogSharedViewModel.dialogDismissed.collectLatest { dismissed ->
                if (dismissed) {
                    setListDismissDialog(userSelectedListselect)
                    openZipListDialogSharedViewModel.resetDialogDismissed()
                }
            }
        }

        viewModel.zipLinks.observe(viewLifecycleOwner, Observer { links ->
            adapter?.updateData(links)
        })
    }

    private fun setLineupOnDialog(selected: String) {
        Log.d("FragmentOpenZip", "setLineupOnDialog called with selected: $selected")
        when (selected) {
            "latest" -> {
                binding.fragmentOpenzipRecentIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipRecentIv.context, R.drawable.drawerbtn_lineup_early_selected))
                Log.d("FragmentOpenZip", "Set latest lineup selected")
            }
            "oldest" -> {
                binding.fragmentOpenzipRecentIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipRecentIv.context, R.drawable.drawerbtn_lineup_old_selected))
                Log.d("FragmentOpenZip", "Set oldest lineup selected")
            }
            "ganada" -> {
                binding.fragmentOpenzipRecentIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipRecentIv.context, R.drawable.drawerbtn_lineup_ganada_selected))
                Log.d("FragmentOpenZip", "Set ganada lineup selected")
            }
            "visit" -> {
                binding.fragmentOpenzipRecentIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipRecentIv.context, R.drawable.drawerbtn_lineup_visit_selected))
                Log.d("FragmentOpenZip", "Set visit lineup selected")
            }
            else -> {
                Log.d("FragmentOpenZip", "Unknown selection in setLineupOnDialog: $selected")
            }
        }
    }

    private fun setLineupDismissDialog(selected: String) {
        Log.d("FragmentOpenZip", "setLineupDismissDialog called with selected: $selected")
        when (selected) {
            "latest" -> {
                binding.fragmentOpenzipRecentIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipRecentIv.context, R.drawable.drawerbtn_lineup_early_unselected))
                Log.d("FragmentOpenZip", "Set latest lineup unselected")
            }
            "oldest" -> {
                binding.fragmentOpenzipRecentIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipRecentIv.context, R.drawable.drawerbtn_lineup_old_unselected))
                Log.d("FragmentOpenZip", "Set oldest lineup unselected")
            }
            "ganada" -> {
                binding.fragmentOpenzipRecentIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipRecentIv.context, R.drawable.drawerbtn_lineup_ganada_unselected))
                Log.d("FragmentOpenZip", "Set ganada lineup unselected")
            }
            "visit" -> {
                binding.fragmentOpenzipRecentIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipRecentIv.context, R.drawable.drawerbtn_lineup_visit_unselected))
                Log.d("FragmentOpenZip", "Set visit lineup unselected")
            }
            else -> {
                Log.d("FragmentOpenZip", "Unknown selection in setLineupDismissDialog: $selected")
            }
        }
    }

    private fun setListOnDialog(selected: String) {
        Log.d("FragmentOpenZip", "setListOnDialog called with selected: $selected")
        when (selected) {
            "all" -> {
                binding.fragmentOpenzipAllIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipAllIv.context, R.drawable.drawerbtn_allselect_selected))
                Log.d("FragmentOpenZip", "Set all list selected")
            }
            "link" -> {
                binding.fragmentOpenzipAllIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipAllIv.context, R.drawable.drawerbtn_linkselect_selected))
                Log.d("FragmentOpenZip", "Set link list selected")
            }
            "text" -> {
                binding.fragmentOpenzipAllIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipAllIv.context, R.drawable.drawerbtn_textlinkselect_selected))
                Log.d("FragmentOpenZip", "Set text list selected")
            }
            else -> {
                Log.d("FragmentOpenZip", "Unknown selection in setListOnDialog: $selected")
            }
        }
    }

    private fun setListDismissDialog(selected: String) {
        Log.d("FragmentOpenZip", "setListDismissDialog called with selected: $selected")
        when (selected) {
            "all" -> {
                binding.fragmentOpenzipAllIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipAllIv.context, R.drawable.drawerbtn_allselect_unselected))
                Log.d("FragmentOpenZip", "Set all list unselected")
            }
            "link" -> {
                binding.fragmentOpenzipAllIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipAllIv.context, R.drawable.drawerbtn_linkselect_unselected))
                Log.d("FragmentOpenZip", "Set link list unselected")
            }
            "text" -> {
                binding.fragmentOpenzipAllIv.setImageDrawable(ContextCompat.getDrawable(binding.fragmentOpenzipAllIv.context, R.drawable.drawerbtn_textlinkselect_unselected))
                Log.d("FragmentOpenZip", "Set text list unselected")
            }
            else -> {
                Log.d("FragmentOpenZip", "Unknown selection in setListDismissDialog: $selected")
            }
        }
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

    override fun initView() {
        setupClickListener()
        setLineupDismissDialog(userSelectedLineup)
        setListDismissDialog(userSelectedListselect)
        setupRecyclerView()

        val toolbarBackBtn = binding.ivOpenzipToolbarBack
        toolbarBackBtn.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentOpenZip_to_fragmentZip)
            Log.d("FragmentOpenZip", "Navigated to FragmentZip")
        }

        binding.fragmentOpenzipEditIv.setOnClickListener(editClickListener)
        binding.allSelectedBtn.setOnClickListener(allSelectedListener)
        binding.allSelectedTv.setOnClickListener(allSelectedListener)

        // Initialize in NormalMode
        setNormalMode()
    }

    private fun setupRecyclerView() {
        adapter = OpenZipItemAdapter(emptyList())
        binding.fragmentOpenzipItemLinkRv.layoutManager = LinearLayoutManager(context)
        binding.fragmentOpenzipItemLinkRv.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
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
        binding.fragmentZipMakeBtn.setBackgroundResource(R.drawable.btn_openzip_movezipact_black)
        binding.fragmentZipMakeBtn.setTextColor(Color.parseColor("#000000"))
        binding.fragmentZipMakeBtn2.setBackgroundResource(R.drawable.shape_rect_005773_fill)
        binding.ivProfilesetBlueshadow.visibility = View.VISIBLE
        binding.ivProfilesetGrayshadow.visibility = View.GONE

        // Add click listener to fragmentZipMakeBtn2
        /*binding.fragmentZipMakeBtn2.setOnClickListener {
            updateBackgroundColorOfItems()
        }
*/
        // Update click listeners to go back to EditMode from AllSelectedMode
        binding.fragmentOpenzipEditIv.setOnClickListener(editClickListener)
        binding.allSelectedBtn.setOnClickListener(editClickListener)
    }

    private fun resetAllSelectedMode() {
        // Reset UI elements to default states
        binding.allSelectedBtn.setImageResource(R.drawable.ic_checkunselected_black)
        binding.allSelectedTv.setTextColor(Color.parseColor("#000000"))
        binding.fragmentZipMakeBtn.setTextColor(Color.parseColor("#999999"))
        binding.fragmentZipMakeBtn.setBackgroundResource(R.drawable.btn_openzip_movezipinact)
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
            viewLifecycleOwner.lifecycleScope.launch {
                openZipLineDialogSharedViewModel.setSelectedData(userSelectedLineup)
            }
            val dialogFragment = ListDialogueLineupFragment()
            dialogFragment.show(parentFragmentManager, "ListDialogueLineupFragment")
        }
        binding.fragmentOpenzipAllIv.setOnSingleClickListener {
            setListOnDialog(userSelectedListselect)
            viewLifecycleOwner.lifecycleScope.launch {
                openZipListDialogSharedViewModel.setSelectedData(userSelectedListselect)
            }
            val dialogFragment = ListDialogueListselectFragment()
            dialogFragment.show(parentFragmentManager, "ListDialogueListselectFragment")
        }
    }
}
