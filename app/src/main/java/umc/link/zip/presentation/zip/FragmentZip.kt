package umc.link.zip.presentation.zip

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentZipBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class FragmentZip : BaseFragment<FragmentZipBinding>(R.layout.fragment_zip) {

    private val zipViewModel: ZipViewModel by viewModels()
    private lateinit var zipAdapter: ZipAdapter

    override fun initObserver() {}
    override fun initView() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView setup
        val recyclerView: RecyclerView = binding.fragmentZipRecyclerview
        zipAdapter = ZipAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = zipAdapter

        // Observe ViewModel's zipItems to update RecyclerView
        zipViewModel.zipItems.observe(viewLifecycleOwner) { zipItems ->
            zipAdapter.submitList(zipItems)
            Log.d("FragmentZip", "zipItems updated: $zipItems")
        }

        // Zip creation button setup
        val makeZipButton: View = binding.fragmentZipMakeBtn
        makeZipButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentZip_to_fragmentMakeZip)
            Log.d("FragmentZip", "Navigated to FragmentMakeZip")
        }

        setupInteractionListeners()
    }

    override fun onResume() {
        super.onResume()
        zipAdapter.notifyDataSetChanged()
    }

    private fun setupInteractionListeners() {
        val sortButton = binding.sortButton  // Make sure the ID matches your layout

        sortButton.setOnClickListener {
            // Toggle the image resource
            val isUnselected = sortButton.tag == null || sortButton.tag == true
            val newDrawable = if (isUnselected) R.drawable.drawerbtn_lineup_early_selected else R.drawable.drawerbtn_lineup_early_unselected
            sortButton.setImageResource(newDrawable)
            sortButton.tag = !isUnselected
        }

        binding.fragmentZipEditBtn.setOnClickListener {
            toggleEditingMode(true)
        }

        binding.fragmentZipFinishBtn.setOnClickListener {
            toggleEditingMode(false)
        }

        // Listener for allSelectedBtnBlack and allSelectedTv
        val toggleSelectionListener = View.OnClickListener {
            binding.allSelectedTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.color1191ad))  // Assuming #1191AD is defined as colorPrimary in your colors.xml
            binding.allSelectedBtn.visibility = View.GONE
        }

        binding.allSelectedBtn.setOnClickListener(toggleSelectionListener)
        binding.allSelectedTv.setOnClickListener(toggleSelectionListener)
    }

    private fun toggleEditingMode(isEditing: Boolean) {
        if (isEditing) {
            // Edit mode
            binding.sortButton.visibility = View.GONE
            binding.fragmentZipEditBtn.visibility = View.GONE
            binding.allSelectedBtn.visibility = View.VISIBLE
            binding.allSelectedTv.visibility = View.VISIBLE
            binding.fragmentZipFinishBtn.visibility = View.VISIBLE
            binding.fragmentZipMakeBtn.setBackgroundResource(R.drawable.shape_rect_8_666666_fill)
            binding.fragmentZipMakeBtn.text = "삭제"
            binding.ivProfilesetBlueshadow.visibility = View.GONE
            binding.ivProfilesetGrayshadow.visibility = View.VISIBLE
        } else {
            // Normal mode
            binding.sortButton.visibility = View.VISIBLE
            binding.fragmentZipEditBtn.visibility = View.VISIBLE
            binding.allSelectedBtn.visibility = View.GONE
            binding.allSelectedTv.visibility = View.GONE
            binding.fragmentZipFinishBtn.visibility = View.GONE
            binding.fragmentZipMakeBtn.setBackgroundResource(R.drawable.shape_rect_1191ad_fill)
            binding.fragmentZipMakeBtn.text = "ZIP 만들기"
            binding.ivProfilesetBlueshadow.visibility = View.VISIBLE
            binding.ivProfilesetGrayshadow.visibility = View.GONE
            // Reset text color and visibility states to default
            binding.allSelectedTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.black)) // Reset to default color
        }
    }

}
