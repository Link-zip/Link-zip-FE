package umc.link.zip.presentation.zip

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentZipBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.zip.adapter.ZipAdapter
import umc.link.zip.presentation.zip.adapter.ZipViewModel

@AndroidEntryPoint
class FragmentZip : BaseFragment<FragmentZipBinding>(R.layout.fragment_zip) {

    override fun initObserver() {}
    override fun initView() {}

    private val viewModel: ZipViewModel by viewModels()
    private var adapter: ZipAdapter? = null
    private var isEditMode = false
    private var isAllSelectedMode = false

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

        setupRecyclerView()

        val toolbarBackBtn = binding.ivHomeAlarmNothing
        toolbarBackBtn.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentZip_to_fragmentOpenZip)
            Log.d("FragmentZip", "Navigated to FragmentOpenZip")
        }

        val MakeZipBtn = binding.fragmentZipMakeBtn
        MakeZipBtn.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentZip_to_fragmentMakeZip)
            Log.d("FragmentZip", "Navigated to FragmentMakeZip")
        }

        binding.fragmentZipEditBtn.setOnClickListener(editClickListener)
        binding.allSelectedBtn.setOnClickListener(allSelectedListener)
        binding.allSelectedTv.setOnClickListener(allSelectedListener)

        // Initialize in NormalMode
        setNormalMode()
    }

    override fun onResume() {
        super.onResume()
        adapter?.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        adapter = ZipAdapter { zipItem, isSelected ->
            if (isSelected) {
                switchToSelectedMode()
            }
        }
        binding.fragmentZipRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.fragmentZipRecyclerview.adapter = adapter

        viewModel.zipItems.observe(viewLifecycleOwner) { zipItems ->
            adapter?.submitList(zipItems)
        }
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
        binding.fragmentZipMakeBtn.text = getString(R.string.zip_create)
        binding.fragmentZipMakeBtn.setBackgroundResource(R.drawable.shape_rect_1191ad_fill)
        binding.ivProfilesetBlueshadow.visibility = View.GONE
        binding.ivProfilesetGrayshadow.visibility = View.VISIBLE
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
        binding.fragmentZipMakeBtn.text = getString(R.string.delete)
        binding.fragmentZipMakeBtn.setBackgroundResource(R.drawable.shape_rect_8_666666_fill)
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
        binding.fragmentZipMakeBtn.text = getString(R.string.delete)
        binding.fragmentZipMakeBtn.setBackgroundResource(R.drawable.shape_rect_005773_fill)
        binding.ivProfilesetBlueshadow.visibility = View.VISIBLE
        binding.ivProfilesetGrayshadow.visibility = View.GONE
        adapter?.selectAllItems()
        updateBackgroundColorOfItems(true)
    }

    private fun resetAllSelectedMode() {
        binding.allSelectedBtn.setImageResource(R.drawable.ic_checkunselected_black)
        binding.allSelectedTv.setTextColor(Color.parseColor("#000000"))
        binding.fragmentZipMakeBtn.setBackgroundResource(R.drawable.shape_rect_8_666666_fill)
        binding.ivProfilesetBlueshadow.visibility = View.GONE
        binding.ivProfilesetGrayshadow.visibility = View.VISIBLE
    }

    private fun switchToSelectedMode() {
        binding.fragmentZipMakeBtn.text = getString(R.string.delete)
        binding.fragmentZipMakeBtn.setBackgroundResource(R.drawable.shape_rect_005773_fill)
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
}