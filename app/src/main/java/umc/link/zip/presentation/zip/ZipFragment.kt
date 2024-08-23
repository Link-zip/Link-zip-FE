package umc.link.zip.presentation.zip

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.Navigator
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
import umc.link.zip.presentation.zip.adapter.ZipDeleteDialogueFragment
import umc.link.zip.presentation.zip.adapter.ZipDeleteViewModel
import umc.link.zip.presentation.zip.adapter.ZipDialogueLineupFragment
import umc.link.zip.presentation.zip.adapter.ZipGetViewModel
import umc.link.zip.presentation.zip.adapter.ZipLineDialogSharedViewModel
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.extension.setOnSingleClickListener
import umc.link.zip.util.network.NetworkResult
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.collect
import umc.link.zip.presentation.zip.adapter.ZipAlertViewModel


@Suppress("DEPRECATION")
@AndroidEntryPoint
class ZipFragment : BaseFragment<FragmentZipBinding>(R.layout.fragment_zip) {
    private val viewModel: ZipGetViewModel by viewModels()
    private val zipDeleteViewModel: ZipDeleteViewModel by viewModels()
    private val zipLineDialogSharedViewModel: ZipLineDialogSharedViewModel by viewModels()
    private val zipAlertViewModel : ZipAlertViewModel by viewModels()

    private val navigator by lazy { findNavController() }
    private var adapter: ZipAdapter? = null

    private var isEditMode = false
    private var isAllSelectedMode = false
    private var userSelectedLineup = "latest"


    override fun initObserver() {
        setZipAlertExistsViewModel()

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
        viewModel.getZipList(userSelectedLineup)
        zipAlertViewModel.getAlertExists()

        binding.fragmentMakezipMakeBtn.setOnClickListener {
            if (isEditMode) {
                // 선택된 아이템이 있을 때만 삭제 다이얼로그를 띄움
                val selectedIds = adapter?.getSelectedZipIds() ?: emptyList()
                if (selectedIds.isNotEmpty()) {
                    val deleteDialog = ZipDeleteDialogueFragment.newInstance(selectedIds)
                    deleteDialog.show(childFragmentManager, "ZipDeleteDialogueFragment")
                    viewModel.getZipList(userSelectedLineup)
                } else {
                    showCustomToast3()
                }
            } else {
                // MakeZip 화면으로 이동
                findNavController().navigate(R.id.action_fragmentZip_to_fragmentMakeZip)
                Log.d("ZipFragment", "Navigated To MakeZip")
            }
        }

        // 편집 버튼 클릭 시 편집 모드로 전환
        binding.fragmentZipEditBtn.setOnClickListener {
            toggleEditMode()
            adapter?.toggleEditMode()
        }


        binding.allSelectedBtn.setOnClickListener(allSelectedListener)
        binding.allSelectedTv.setOnClickListener(allSelectedListener)

        setNormalMode()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.zipList.collect { zipList ->
                adapter?.submitList(zipList)
            }
        }
        setupRecyclerView()

        binding.ivHomeAlarmExist.setOnClickListener {
            navigator.navigate(R.id.action_zipFragment_to_alertFragment)
        }

        // Initialize in NormalMode
        setNormalMode()
        viewModel.getZipList(userSelectedLineup)

        viewLifecycleOwner.lifecycleScope.launch {
            zipDeleteViewModel.deleteResponse.collectLatest { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        Log.d("ZipFragment", "개성공")
                        showCustomToast()
                        viewModel.getZipList(userSelectedLineup)
                    }

                    is NetworkResult.Error -> {
                        Log.d("ZipFragment", "하나씩 삭제되어서 생긴 문제")
                        viewModel.getZipList(userSelectedLineup)
                    }

                    is NetworkResult.Fail -> {
                        Toast.makeText(context, "삭제 실패: ${response.message}", Toast.LENGTH_LONG)
                            .show()
                    }

                    null -> {
                        Log.d("ZipFragment", "개망")
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getZipList(userSelectedLineup)
        setLineupDismissDialog(userSelectedLineup)
    }

    private fun setupRecyclerView() {
        adapter = ZipAdapter({ zipItem, isSelected ->
            if (isSelected) {
                switchToSelectedMode()
            }
        }, {
            resetAllSelectedMode() // 선택된 아이템이 없을 때 모드 초기화
        })

        binding.fragmentZipRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.fragmentZipRecyclerview.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }

    private fun setZipAlertExistsViewModel() {
        zipAlertViewModel.alertExists.observe(this) { result ->
            when(result) {
                is NetworkResult.Error -> { Log.d("home", "alert exists : ${result.exception}") }
                is NetworkResult.Fail -> {}
                is NetworkResult.Success -> {
                    if(result.data.uncomfirmedAlert) {
                        binding.ivHomeAlarmExist.visibility = View.VISIBLE
                        binding.ivHomeAlarmNothing.visibility = View.INVISIBLE
                    } else {
                        binding.ivHomeAlarmExist.visibility = View.INVISIBLE
                        binding.ivHomeAlarmNothing.visibility = View.VISIBLE
                    }
                }
            }
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

    private fun setNormalMode() {
        isEditMode = false
        isAllSelectedMode = false
        resetAllSelectedMode()
        binding.sortButton.visibility = View.VISIBLE
        binding.allSelectedBtn.visibility = View.GONE
        binding.allSelectedTv.visibility = View.GONE
        binding.fragmentMakezipMakeBtn.text = getString(R.string.zip_create)
        binding.fragmentZipShadow.setBackgroundResource(R.drawable.shadow_zip_bg)
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
        resetAllSelectedMode()
        binding.sortButton.visibility = View.GONE
        binding.allSelectedBtn.visibility = View.VISIBLE
        binding.allSelectedTv.visibility = View.VISIBLE
        binding.fragmentMakezipMakeBtn.text = getString(R.string.delete)
        binding.fragmentMakezipMakeBtn.setBackgroundResource(R.drawable.shape_rect_8_666666_fill)
        binding.fragmentZipShadow.setBackgroundResource(R.drawable.shadow_zip_bg4)
        binding.ivProfilesetBlueshadow.visibility = View.GONE
        binding.ivProfilesetGrayshadow.visibility = View.VISIBLE
        binding.fragmentZipEditBtn.text = "완료"
        binding.fragmentZipEditBtn.setTextColor(Color.parseColor("#1191AD"))
        binding.fragmentZipEditBtn.visibility = View.VISIBLE
        binding.fragmentZipFinishBtn.visibility = View.GONE
    }

    private val allSelectedListener = View.OnClickListener {
        if (isEditMode) {
            if (isAllSelectedMode) {
                // 전체 선택 해제
                resetAllSelectedMode()
            } else {
                // 전체 선택 활성화
                setAllSelectedMode()
            }
        }
    }

    private fun setAllSelectedMode() {
        isAllSelectedMode = true
        binding.allSelectedBtn.setImageResource(R.drawable.ic_checkunselected_blue)
        binding.allSelectedTv.setTextColor(Color.parseColor("#1191AD"))
        binding.fragmentMakezipMakeBtn.text = getString(R.string.delete)
        binding.fragmentMakezipMakeBtn.setBackgroundResource(R.drawable.shape_rect_005773_fill)
        binding.ivProfilesetBlueshadow.visibility = View.VISIBLE
        binding.ivProfilesetGrayshadow.visibility = View.GONE
        adapter?.selectAllItems()
    }

    private fun resetAllSelectedMode() {
        isAllSelectedMode = false
        binding.allSelectedBtn.setImageResource(R.drawable.ic_checkunselected_black)
        binding.allSelectedTv.setTextColor(Color.parseColor("#000000"))
        binding.fragmentMakezipMakeBtn.setBackgroundResource(R.drawable.shape_rect_8_666666_fill)
        binding.ivProfilesetBlueshadow.visibility = View.GONE
        binding.ivProfilesetGrayshadow.visibility = View.VISIBLE
        adapter?.clearSelections()
    }

    private fun switchToSelectedMode() {
        binding.fragmentMakezipMakeBtn.setBackgroundResource(R.drawable.shape_rect_005773_fill)
        binding.ivProfilesetBlueshadow.visibility = View.VISIBLE
        binding.ivProfilesetGrayshadow.visibility = View.GONE
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

    //토스트
    private fun showCustomToast() {
        Log.d("Toast", "Toast 뜸")
        val inflater = LayoutInflater.from(requireActivity())
        val layout = inflater.inflate(R.layout.custom_toast, null)
        val tv = layout.findViewById<TextView>(R.id.tvSample)
        tv.text = "Zip 삭제 완료"

        val toast = Toast(requireActivity()).apply {
            duration = Toast.LENGTH_SHORT
            view = layout
            setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 230)
        }
        toast.show()
    }

    private fun showCustomToast3() {
        val inflater = LayoutInflater.from(requireActivity())
        val layout = inflater.inflate(R.layout.custom_toast, null)
        val tv = layout.findViewById<TextView>(R.id.tvSample)
        tv.text = "삭제할 항목을 선택하세요."

        val toast = Toast(requireActivity()).apply {
            duration = Toast.LENGTH_SHORT
            view = layout
            setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 230)
        }
        toast.show()
    }
}