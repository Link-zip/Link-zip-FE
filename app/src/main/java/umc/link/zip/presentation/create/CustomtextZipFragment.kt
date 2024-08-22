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
class CustomtextZipFragment : BaseFragment<FragmentCustomtextZipBinding>(R.layout.fragment_customtext_zip){
    private val viewModel: ZipGetViewModel by viewModels()
    private var adapter: CustomtextZipItemAdapter? = null

    private var isSelected = false

    private val zipLineDialogSharedViewModel: ZipLineDialogSharedViewModel by viewModels()
    private var userSelectedLineup = "latest"


    private fun navigateToCreate() {
        findNavController().navigate(R.id.action_customtextZipFragment_to_createFragment)
    }

    private fun navigateToCustom() {
        findNavController().navigate(R.id.action_customtextZipFragment_to_customtextCustomFragment)
    }

    private fun navigateToMake() {
        findNavController().navigate(R.id.action_customtextZipFragment_to_makeZipFragment)
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
                binding.btnCustomTextEarlyUnselected.setImageDrawable(ContextCompat.getDrawable(binding.btnCustomTextEarlyUnselected.context, R.drawable.drawerbtn_lineup_early_selected))
            }
            "oldest" -> {
                binding.btnCustomTextEarlyUnselected.setImageDrawable(ContextCompat.getDrawable(binding.btnCustomTextEarlyUnselected.context, R.drawable.drawerbtn_lineup_old_selected))
            }
            "ganada" -> {
                binding.btnCustomTextEarlyUnselected.setImageDrawable(ContextCompat.getDrawable(binding.btnCustomTextEarlyUnselected.context, R.drawable.drawerbtn_lineup_ganada_selected))
            }
            "visit" -> {
                binding.btnCustomTextEarlyUnselected.setImageDrawable(ContextCompat.getDrawable(binding.btnCustomTextEarlyUnselected.context, R.drawable.drawerbtn_lineup_visit_selected))
            }
        }
        viewModel.getZipList(userSelectedLineup)
    }

    private fun setLineupDismissDialog(selected: String) {
        when (selected) {
            "latest" -> {
                binding.btnCustomTextEarlyUnselected.setImageDrawable(ContextCompat.getDrawable(binding.btnCustomTextEarlyUnselected.context, R.drawable.drawerbtn_lineup_early_unselected))
            }
            "oldest" -> {
                binding.btnCustomTextEarlyUnselected.setImageDrawable(ContextCompat.getDrawable(binding.btnCustomTextEarlyUnselected.context, R.drawable.drawerbtn_lineup_old_unselected))
            }
            "ganada" -> {
                binding.btnCustomTextEarlyUnselected.setImageDrawable(ContextCompat.getDrawable(binding.btnCustomTextEarlyUnselected.context, R.drawable.drawerbtn_lineup_ganada_unselected))
            }
            "visit" -> {
                binding.btnCustomTextEarlyUnselected.setImageDrawable(ContextCompat.getDrawable(binding.btnCustomTextEarlyUnselected.context, R.drawable.drawerbtn_lineup_visit_unselected))
            }
        }
        viewModel.getZipList(userSelectedLineup)
    }

    override fun initView() {
        Log.d("CustomtextZipFragment", "initView called")
        setupClickListener()
        setLineupDismissDialog(userSelectedLineup)

        binding.ivCustomTextZipToolbarBack.setOnClickListener{
            navigateToCreate()
        }

        binding.clCustomTextZipNextBtn.setOnClickListener{
            navigateToCustom()
        }

        binding.tvCustomTextNewZip.setOnClickListener{
            navigateToMake()
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
            }else {
                resetSelectedBtn()
            }
        }

        binding.rvCustomTextZip.layoutManager = LinearLayoutManager(context)
        binding.rvCustomTextZip.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }

    private fun setSelectedBtn() {
        binding.ivCustomTextZipGrayshadow.visibility = View.GONE
        binding.ivCustomTextZipBlueshadow.visibility = View.VISIBLE
        binding.btnCustomTextZipNext.setBackgroundResource(R.drawable.shape_rect_1191ad_fill)
    }

    private fun resetSelectedBtn() {
        binding.ivCustomTextZipGrayshadow.visibility = View.VISIBLE
        binding.ivCustomTextZipBlueshadow.visibility = View.GONE
        binding.btnCustomTextZipNext.setBackgroundResource(R.drawable.shape_rect_8_666666_fill)
        adapter?.clearSelections()
    }

    private fun setupClickListener() {
        //한번만 클릭 허용
        binding.btnCustomTextEarlyUnselected.setOnSingleClickListener {
            setLineupOnDialog(userSelectedLineup)
            repeatOnStarted {
                zipLineDialogSharedViewModel.setSelectedData(userSelectedLineup)
            }
            val dialogFragment = ZipDialogueLineupFragment()
            dialogFragment.show(childFragmentManager, "ZipDialogueLineupFragment")
        }
    }

}
