package umc.link.zip.presentation.create

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
import umc.link.zip.databinding.FragmentCustomlinkZipBinding
import umc.link.zip.databinding.FragmentCustomtextZipBinding
import umc.link.zip.databinding.FragmentZipBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.zip.adapter.CustomtextZipItemAdapter
import umc.link.zip.presentation.zip.adapter.ZipAdapter
import umc.link.zip.presentation.zip.adapter.ZipDialogueLineupFragment
import umc.link.zip.presentation.zip.adapter.ZipGetViewModel
import umc.link.zip.presentation.zip.adapter.ZipLineDialogSharedViewModel
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.extension.setOnSingleClickListener

@AndroidEntryPoint
class CustomlinkZipFragment : BaseFragment<FragmentCustomlinkZipBinding>(R.layout.fragment_customlink_zip){
    private val viewModel: ZipGetViewModel by viewModels()
    private var adapter: CustomtextZipItemAdapter? = null

    private var isSelected = false

    private val zipLineDialogSharedViewModel: ZipLineDialogSharedViewModel by viewModels()
    private var userSelectedLineup = "latest"

    private var easySaveZipId : Int? = null
    private var selectedZipID: Int? = null


    private fun navigateToCustom() {
        selectedZipID?.let { id ->
            val action =
                CustomlinkZipFragmentDirections.actionCustomlinkZipFragmentToCustomlinkCustomFragment(
                    id
                )
            Log.d("CustomlinkZipFragment", "선택된 zipId: $id 전달 navigate")
            findNavController().navigate(action)
        } ?: run {
            Log.d("CustomlinkZipFragment", "선택된 zipId 가져오기 실패")
        }
    }

    private fun navigateToOpenLink(){
        selectedZipID?.let { id ->
            val action =
                CustomlinkZipFragmentDirections.actionCustomlinkZipFragmentToOpenLinkFragment(
                    id
                )
            Log.d("OpenLinkFragment", "빠른저장 zipId; $id 전달 navigate")
            findNavController().navigate(action)
        } ?: run {
            Log.d("OpenLinkFragment", "빠른저장 zipId 가져오기 실패")
        }
    }



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
                binding.btnCustomLinkEarlyUnselected.setImageDrawable(ContextCompat.getDrawable(binding.btnCustomLinkEarlyUnselected.context, R.drawable.drawerbtn_lineup_early_selected))
            }
            "earliest" -> {
                binding.btnCustomLinkEarlyUnselected.setImageDrawable(ContextCompat.getDrawable(binding.btnCustomLinkEarlyUnselected.context, R.drawable.drawerbtn_lineup_old_selected))
            }
            "alphabet" -> {
                binding.btnCustomLinkEarlyUnselected.setImageDrawable(ContextCompat.getDrawable(binding.btnCustomLinkEarlyUnselected.context, R.drawable.drawerbtn_lineup_ganada_selected))
            }
            "visit" -> {
                binding.btnCustomLinkEarlyUnselected.setImageDrawable(ContextCompat.getDrawable(binding.btnCustomLinkEarlyUnselected.context, R.drawable.drawerbtn_lineup_visit_selected))
            }
        }
        viewModel.getZipList(userSelectedLineup)
    }

    private fun setLineupDismissDialog(selected: String) {
        when (selected) {
            "latest" -> {
                binding.btnCustomLinkEarlyUnselected.setImageDrawable(ContextCompat.getDrawable(binding.btnCustomLinkEarlyUnselected.context, R.drawable.drawerbtn_lineup_early_unselected))
            }
            "earliest" -> {
                binding.btnCustomLinkEarlyUnselected.setImageDrawable(ContextCompat.getDrawable(binding.btnCustomLinkEarlyUnselected.context, R.drawable.drawerbtn_lineup_old_unselected))
            }
            "alphabet" -> {
                binding.btnCustomLinkEarlyUnselected.setImageDrawable(ContextCompat.getDrawable(binding.btnCustomLinkEarlyUnselected.context, R.drawable.drawerbtn_lineup_ganada_unselected))
            }
            "visit" -> {
                binding.btnCustomLinkEarlyUnselected.setImageDrawable(ContextCompat.getDrawable(binding.btnCustomLinkEarlyUnselected.context, R.drawable.drawerbtn_lineup_visit_unselected))
            }
        }
        viewModel.getZipList(userSelectedLineup)
    }

    override fun initView() {
        Log.d("CustomtextZipFragment", "initView called")
        setupClickListener()
        setLineupDismissDialog(userSelectedLineup)

        binding.ivCustomLinkZipToolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnCustomLinkZipNext.setOnClickListener {
            navigateToCustom()
        }

        binding.clCustomLinkZipEasySaveBtn.setOnClickListener {
            navigateToOpenLink()
        }


        if(isSelected){
            setSelectedBtn()
        }else{
            resetSelectedBtn()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터 로딩을 onViewCreated에서 수행
        viewModel.getZipList(userSelectedLineup)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.zipList.collect { zipList ->
                adapter?.submitList(zipList)

                // 빠른저장 zipId 가져오기
                val easySaveZip = zipList.find { it.title == "빠른 저장" }
                easySaveZipId = easySaveZip?.zip_id

                Log.d("CustomlinkZipFragment", "빠른 저장 zipId: $easySaveZipId")
            }
        }
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        setLineupDismissDialog(userSelectedLineup)
        viewModel.getZipList(userSelectedLineup)
    }

    private fun setupRecyclerView() {
        adapter = CustomtextZipItemAdapter { zipItem, isSelected ->
            if (isSelected) {
                setSelectedBtn()
                selectedZipID = zipItem.zip_id
                Log.d("CustomlinkZipFragment", "선택된 zipId: $selectedZipID")
            }else {
                resetSelectedBtn()
            }
        }

        binding.rvCustomLinkZip.layoutManager = LinearLayoutManager(context)
        binding.rvCustomLinkZip.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }

    private fun setSelectedBtn() {
        binding.ivCustomLinkZipGrayshadow.visibility = View.GONE
        binding.ivCustomLinkZipBlueshadow.visibility = View.VISIBLE
        binding.btnCustomLinkZipNext.setBackgroundResource(R.drawable.shape_rect_1191ad_fill)

        binding.btnCustomLinkZipNext.isClickable = true
    }

    private fun resetSelectedBtn() {
        binding.ivCustomLinkZipGrayshadow.visibility = View.VISIBLE
        binding.ivCustomLinkZipBlueshadow.visibility = View.GONE
        binding.btnCustomLinkZipNext.setBackgroundResource(R.drawable.shape_rect_8_666666_fill)
        adapter?.clearSelections()

        binding.btnCustomLinkZipNext.isClickable = false
    }

    private fun setupClickListener() {
        //한번만 클릭 허용
        binding.btnCustomLinkEarlyUnselected.setOnSingleClickListener {
            setLineupOnDialog(userSelectedLineup)
            repeatOnStarted {
                zipLineDialogSharedViewModel.setSelectedData(userSelectedLineup)
            }
            val dialogFragment = ZipDialogueLineupFragment()
            dialogFragment.show(childFragmentManager, "ZipDialogueLineupFragment")
        }
    }

}
