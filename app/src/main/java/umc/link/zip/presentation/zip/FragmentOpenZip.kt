package umc.link.zip.presentation.zip.FragmentOpenZip

import OpenZipViewModel
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentOpenzipBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.zip.adapter.OpenZipItemAdapter.OpenZipItemAdapter

@AndroidEntryPoint
class FragmentOpenZip : BaseFragment<FragmentOpenzipBinding>(R.layout.fragment_openzip) {

    private val viewModel: OpenZipViewModel by viewModels()
    private var adapter: OpenZipItemAdapter? = null
    private var isEditMode = false
    private var isAllSelectedMode = false

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

    override fun initObserver() {
        viewModel.zipLinks.observe(viewLifecycleOwner, Observer { links ->
            adapter?.updateData(links)
        })
    }

    override fun initView() {
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
        binding.ivProfilesetBlueshadow.visibility = View.VISIBLE
        binding.ivProfilesetGrayshadow.visibility = View.GONE
    }

    private fun updateBackgroundColorOfItems() {
        val recyclerView = binding.fragmentOpenzipItemLinkRv
        for (i in 0 until recyclerView.childCount) {
            val itemView = recyclerView.getChildAt(i)
            val itemMainLayout: ConstraintLayout = itemView.findViewById(R.id.item_link_main_cl)
            itemMainLayout.setBackgroundColor(Color.parseColor("#F5F4FD"))
        }
    }

    private fun resetBackgroundColorOfItems() {
        val recyclerView = binding.fragmentOpenzipItemLinkRv
        for (i in 0 until recyclerView.childCount) {
            val itemView = recyclerView.getChildAt(i)
            val itemMainLayout: ConstraintLayout = itemView.findViewById(R.id.item_link_main_cl)
            itemMainLayout.setBackgroundColor(Color.parseColor("#FBFBFB"))
        }
    }
}
