package umc.link.zip.presentation.zip

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.data.dto.zip.request.ZipCreateRequest
import umc.link.zip.databinding.FragmentZipBinding
import umc.link.zip.domain.model.zip.ZipGetModel
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.zip.adapter.ZipAdapter
import umc.link.zip.presentation.zip.adapter.ZipDialogueLineupFragment
import umc.link.zip.presentation.zip.adapter.ZipGetViewModel
import umc.link.zip.presentation.zip.adapter.ZipLineDialogSharedViewModel
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.extension.setOnSingleClickListener

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ZipFragment : BaseFragment<FragmentZipBinding>(R.layout.fragment_zip) {
    private val viewModel: ZipGetViewModel by viewModels()
    private var adapter: ZipAdapter? = null
    private var isEditMode = false
    private var isAllSelectedMode = false

    private val zipLineDialogSharedViewModel: ZipLineDialogSharedViewModel by viewModels()
    private var userSelectedLineup = "latest"


    override fun initObserver() {
        // lineup
        viewLifecycleOwner.lifecycleScope.launch {
            zipLineDialogSharedViewModel.selectedData.collectLatest { data ->
                userSelectedLineup = data
                setLineupOnDialog(userSelectedLineup)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            zipLineDialogSharedViewModel.dialogDismissed.collectLatest { dismissed ->
                if (dismissed) {
                    setLineupDismissDialog(userSelectedLineup)
                    zipLineDialogSharedViewModel.resetDialogDismissed()
                }
            }
        }
    }

    private fun setLineupOnDialog(selected: String) {
        when (selected) {
            "latest" -> {
                binding.sortButton.setImageDrawable(ContextCompat.getDrawable(binding.sortButton.context, R.drawable.drawerbtn_lineup_early_selected))
            }
            "earliest" -> {
                binding.sortButton.setImageDrawable(ContextCompat.getDrawable(binding.sortButton.context, R.drawable.drawerbtn_lineup_old_selected))
            }
            "alphabet" -> {
                binding.sortButton.setImageDrawable(ContextCompat.getDrawable(binding.sortButton.context, R.drawable.drawerbtn_lineup_ganada_selected))
            }
            "visit" -> {
                binding.sortButton.setImageDrawable(ContextCompat.getDrawable(binding.sortButton.context, R.drawable.drawerbtn_lineup_visit_selected))
            }
        }
        Log.d("ZipGetFragment", "userSelectedLineup : $userSelectedLineup")
        viewModel.getZipList(userSelectedLineup)
    }

    private fun setLineupDismissDialog(selected: String) {
        when (selected) {
            "latest" -> {
                binding.sortButton.setImageDrawable(ContextCompat.getDrawable(binding.sortButton.context, R.drawable.drawerbtn_lineup_early_unselected))
            }
            "earliest" -> {
                binding.sortButton.setImageDrawable(ContextCompat.getDrawable(binding.sortButton.context, R.drawable.drawerbtn_lineup_old_unselected))
            }
            "alphabet" -> {
                binding.sortButton.setImageDrawable(ContextCompat.getDrawable(binding.sortButton.context, R.drawable.drawerbtn_lineup_ganada_unselected))
            }
            "visit" -> {
                binding.sortButton.setImageDrawable(ContextCompat.getDrawable(binding.sortButton.context, R.drawable.drawerbtn_lineup_visit_unselected))
            }
        }
        Log.d("ZipGetFragment", "userSelectedLineup : $userSelectedLineup")
        viewModel.getZipList(userSelectedLineup)
    }

    override fun initView() {
        setupClickListener()
        setLineupDismissDialog(userSelectedLineup)
    }

    private val editClickListener = View.OnClickListener {
        toggleEditMode()
    }

    private val allSelectedListener = View.OnClickListener {
        if (isEditMode) {
            setAllSelectedMode()
        } else if (isAllSelectedMode) {
            setEditMode()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.zipList.collect { zipList ->
                adapter?.submitList(zipList)
            }
        }
        setupRecyclerView()

        val alertBtn = binding.ivHomeAlarmNothing
        alertBtn.setOnClickListener {
            findNavController().navigate(R.id.action_zipFragment_to_alertFragment)
            Log.d("FragmentZip", "Navigated to FragmentAlertZip")
        }

        val MakeZipBtn = binding.fragmentMakezipMakeBtn
        MakeZipBtn.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentZip_to_fragmentMakeZip)
            Log.d("FragmentZip", "Navigated to FragmentMakeZip")
        }

        val imsiItemClick = binding.tvHomeLinkzip
        imsiItemClick.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentZip_to_fragmentOpenZip)
            Log.d("FragmentZip", "Navigated to FragmentOpenZip")
        }

        binding.fragmentZipEditBtn.setOnClickListener(editClickListener)
        binding.allSelectedBtn.setOnClickListener(allSelectedListener)
        binding.allSelectedTv.setOnClickListener(allSelectedListener)

        // Initialize in NormalMode
        setNormalMode()
    }


    override fun onResume() {
        super.onResume()
        viewModel.getZipList(userSelectedLineup)
        setLineupDismissDialog(userSelectedLineup)
    }

    private fun setupRecyclerView() {
        adapter = ZipAdapter { zipItem, isSelected ->
            if (isSelected) {
                switchToSelectedMode()
            }
        }
        binding.fragmentZipRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.fragmentZipRecyclerview.adapter = adapter
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

    private fun setNormalMode() {
        isEditMode = false
        isAllSelectedMode = false
        resetAllSelectedMode()
        resetBackgroundColorOfItems()
        binding.sortButton.visibility = View.VISIBLE
        binding.allSelectedBtn.visibility = View.GONE
        binding.allSelectedTv.visibility = View.GONE
        binding.fragmentMakezipMakeBtn.text = getString(R.string.zip_create)
        binding.fragmentMakezipMakeBtn.setBackgroundResource(R.drawable.shape_rect_1191ad_fill)
        binding.ivProfilesetBlueshadow.visibility = View.VISIBLE
        binding.ivProfilesetGrayshadow.visibility = View.GONE
        binding.fragmentZipEditBtn.text = "편집"
        binding.fragmentZipEditBtn.setTextColor(Color.parseColor("#000000"))
        binding.fragmentZipFinishBtn.visibility = View.GONE
        adapter?.clearSelections()
    }

    private fun setEditMode() {
        isEditMode = true
        isAllSelectedMode = false
        resetAllSelectedMode()
        binding.sortButton.visibility = View.GONE
        binding.allSelectedBtn.visibility = View.VISIBLE
        binding.allSelectedTv.visibility = View.VISIBLE
        binding.fragmentMakezipMakeBtn.text = getString(R.string.delete)
        binding.fragmentMakezipMakeBtn.setBackgroundResource(R.drawable.shape_rect_8_666666_fill)
        binding.ivProfilesetBlueshadow.visibility = View.GONE
        binding.ivProfilesetGrayshadow.visibility = View.VISIBLE
        binding.fragmentZipEditBtn.text = "완료"
        binding.fragmentZipEditBtn.setTextColor(Color.parseColor("#1191AD"))
        binding.fragmentZipEditBtn.visibility = View.VISIBLE
        binding.fragmentZipFinishBtn.visibility = View.GONE
        resetBackgroundColorOfItems() // Ensure the background color is reset when entering EditMode
    }

    private fun setAllSelectedMode() {
        isAllSelectedMode = true
        isEditMode = false
        binding.allSelectedBtn.setImageResource(R.drawable.ic_checkunselected_blue)
        binding.allSelectedTv.setTextColor(Color.parseColor("#1191AD"))
        binding.fragmentMakezipMakeBtn.text = getString(R.string.delete)
        binding.fragmentMakezipMakeBtn.setBackgroundResource(R.drawable.shape_rect_005773_fill)
        binding.ivProfilesetBlueshadow.visibility = View.VISIBLE
        binding.ivProfilesetGrayshadow.visibility = View.GONE
        adapter?.selectAllItems()
        updateBackgroundColorOfItems(true)
    }

    private fun resetAllSelectedMode() {
        binding.allSelectedBtn.setImageResource(R.drawable.ic_checkunselected_black)
        binding.allSelectedTv.setTextColor(Color.parseColor("#000000"))
        binding.fragmentMakezipMakeBtn.setBackgroundResource(R.drawable.shape_rect_8_666666_fill)
        binding.ivProfilesetBlueshadow.visibility = View.GONE
        binding.ivProfilesetGrayshadow.visibility = View.VISIBLE
    }

    private fun switchToSelectedMode() {
        binding.fragmentMakezipMakeBtn.setBackgroundResource(R.drawable.shape_rect_005773_fill)
        binding.ivProfilesetBlueshadow.visibility = View.VISIBLE
        binding.ivProfilesetGrayshadow.visibility = View.GONE
    }

    private fun updateBackgroundColorOfItems(isAllSelected: Boolean) {
        val color = if (isAllSelected) "#F4F5F6" else "#FBFBFB"
        adapter?.updateBackgroundColorOfItems(Color.parseColor(color))
    }

    private fun resetBackgroundColorOfItems() {
        adapter?.updateBackgroundColorOfItems(Color.parseColor("#FBFBFB"))
    }

    private fun setupClickListener() {
        //한번만 클릭 허용
        binding.sortButton.setOnSingleClickListener {
            setLineupOnDialog(userSelectedLineup)
            repeatOnStarted {
                zipLineDialogSharedViewModel.setSelectedData(userSelectedLineup)
            }
            val dialogFragment = ZipDialogueLineupFragment()
            dialogFragment.show(childFragmentManager, "ZipDialogueLineupFragment")
        }
    }

}