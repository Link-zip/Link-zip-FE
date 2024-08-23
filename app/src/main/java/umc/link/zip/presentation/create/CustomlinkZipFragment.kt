package umc.link.zip.presentation.create

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.data.dto.link.request.LinkAddRequest
import umc.link.zip.databinding.FragmentCustomlinkZipBinding
import umc.link.zip.databinding.FragmentCustomtextZipBinding
import umc.link.zip.databinding.FragmentZipBinding
import umc.link.zip.domain.model.link.LinkAddModel
import umc.link.zip.domain.model.link.LinkExtractModel
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.create.adapter.LinkAddViewModel
import umc.link.zip.presentation.create.adapter.LinkExtractViewModel
import umc.link.zip.presentation.zip.adapter.CustomtextZipItemAdapter
import umc.link.zip.presentation.zip.adapter.ZipAdapter
import umc.link.zip.presentation.zip.adapter.ZipDialogueLineupFragment
import umc.link.zip.presentation.zip.adapter.ZipGetViewModel
import umc.link.zip.presentation.zip.adapter.ZipLineDialogSharedViewModel
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.extension.setOnSingleClickListener
import umc.link.zip.util.network.UiState

@AndroidEntryPoint
class CustomlinkZipFragment : BaseFragment<FragmentCustomlinkZipBinding>(R.layout.fragment_customlink_zip){
    private val viewModel: ZipGetViewModel by viewModels()
    private val linkAddViewModel: LinkAddViewModel by viewModels()

    private var adapter: CustomtextZipItemAdapter? = null

    private var isSelected = false

    private val zipLineDialogSharedViewModel: ZipLineDialogSharedViewModel by viewModels()
    private var userSelectedLineup = "latest"

    private var easySaveZipId : Int? = null
    private var selectedZipID: Int? = null

    private val linkTitle: String? by lazy {
        arguments?.getString("linkTitle")
    }
    private val linkUrl: String? by lazy {
        arguments?.getString("linkUrl")
    }

    private var setTitle: String? = null
    private var setUrl: String? = null

    private var linkId: Int? = null


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
        val zipId = easySaveZipId
        val title = setTitle
        val memoText = ""
        val text: String? = null // text를 null로 지정
        val url = setUrl
        val alertDate = null

        val linkAddRequest = LinkAddRequest(
            zip_id = zipId!!,
            title = title!!,
            memo = memoText,
            text = text,
            url = url!!,
            alert_date = alertDate
        )
        linkAddViewModel.addLink(linkAddRequest)

        Log.d(
            "CustomlinkZipFragment",
            "ADD API 호출\nzip_id=${zipId}\ntitle=${title}\nmemo=${memoText}\ntext=${text}\nurl=${url}\nalert_date=${alertDate}"
        )

        // ADD API 응답 후 이동 (linkId 설정)
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                linkAddViewModel.addResponse.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> {
                            // 로딩 상태 처리
                            Log.d("CustomlinkZipFragment", "Loading ADD data")
                        }
                        is UiState.Success<*> -> {
                            val data = state.data as LinkAddModel
                            linkId = data.link_id  // 응답으로 받은 링크 ID 설정
                            Log.d("CustomlinkZipFragment", "링크 추가 성공 linkId: $linkId")

                            // 성공적으로 linkId를 받았을 때 화면 이동
                            linkId?.let { id ->
                                val action =
                                    CustomlinkZipFragmentDirections.actionCustomlinkZipFragmentToOpenLinkFragment(
                                        id, true
                                    )
                                Log.d("CustomlinkZipFragment", "linkId: $id")
                                findNavController().navigate(action)
                            } ?: run {
                                Log.d("CustomlinkZipFragment", "linkId 가져오기 실패")
                            }
                        }

                        is UiState.Error -> {
                            Toast.makeText(requireContext(), "링크 추가 실패", Toast.LENGTH_SHORT)
                                .show()
                            Log.d("CustomlinkZipFragment", "링크 추가 실패")
                        }

                        UiState.Empty -> Log.d("CustomlinkZipFragment", "isEmpty")
                    }
                }
            }
        }
    }



    override fun initObserver() {
        setTitle = linkTitle ?: return
        setUrl = linkUrl ?: return

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

    //selected Mode / empty Mode

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
