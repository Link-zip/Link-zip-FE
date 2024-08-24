package umc.link.zip.presentation.create

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.data.dto.link.request.LinkModifyRequest
import umc.link.zip.databinding.FragmentCustomlinkCustomBinding
import umc.link.zip.databinding.FragmentCustomtextCustomBinding
import umc.link.zip.domain.model.link.LinkGetByLinkIDModel
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.create.adapter.LinkAddViewModel
import umc.link.zip.presentation.create.adapter.LinkGetByIDViewModel
import umc.link.zip.presentation.create.adapter.LinkModifyViewModel
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.extension.setImageResource
import umc.link.zip.util.network.UiState

@AndroidEntryPoint
class ModifytextCustomFragment : BaseFragment<FragmentCustomtextCustomBinding>(R.layout.fragment_customtext_custom) {

    private val linkGetByIDViewModel: LinkGetByIDViewModel by activityViewModels()
    private val linkAddViewModel: LinkAddViewModel by activityViewModels()
    private val linkModifyViewModel: LinkModifyViewModel by activityViewModels()

    private var linkId: Int? = null

    private var getTitle: String? = null
    private var getText: String? = null
    private var getMemo: String? = null
    private var getAlert: String? = null

    private var updateTitle: String? = null
    private var updateText: String? = null
    private var updateMemo: String? = null
    private var updateAlert: String? = null

    private var isSuccess: Boolean = false
    private var isUpdated: Boolean = true

    private val getLinkId: Int? by lazy {
        arguments?.getInt("linkId")
    }

    override fun initObserver() {
        linkId = getLinkId ?: return
        linkGetByIDViewModel.getLinkByLinkID(linkId!!)

        // linkGeyByID API 응답
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                linkGetByIDViewModel.linkGetByLinkIDResponse.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> {
                            // 로딩 상태 처리
                            Log.d("ModifytextCustomFragment", "Loading get Link by ID data")
                        }

                        is UiState.Success<*> -> {
                            val data = state.data as LinkGetByLinkIDModel
                            // 제목
                            getTitle = data.title
                            // 텍스트 요약
                            getText = data.text
                            // 메모
                            getMemo = data.memo
                            // 알림
                            getAlert = data.alert_date
                            Log.d("ModifytextCustomFragment", "getLinkByID 성공 getTitle: $getTitle getText: $getText getMemo: $getMemo getAlert: $getAlert")
                        }

                        is UiState.Error -> {
                            Log.d("ModifytextCustomFragment", "getLinkByID 가져오기 실패")
                        }

                        UiState.Empty -> Log.d("ModifytextCustomFragment", "getLinkByID isEmpty")
                    }
                }
            }
        }
        // 제목 설정
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                linkAddViewModel.link.collectLatest { link ->
                    if (isUpdated == true) {

                        updateTitle = link.title
                        updateMemo = link.memo
                        updateAlert = link.alertDate

                        Log.d(
                            "ModifytextCustomFragment",
                            "updateTitle: $updateTitle \nupdateMemo: $updateMemo \nupdateAlert: $updateAlert"
                        )

                        if (updateTitle == "default") {
                            binding.etCustomTextCustomLinkTitle.setText(getTitle)
                            Log.d("ModifytextCustomFragment", "제목 설정: $getTitle")
                        } else {
                            binding.etCustomTextCustomLinkTitle.setText(updateTitle)
                            Log.d("ModifytextCustomFragment", "제목 설정: $updateTitle")
                        }
                        isUpdated = false
                    }
                }
            }
        }
        // 텍스트 요약 설정
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                linkAddViewModel.link.collectLatest { link ->
                    updateText = link.text
                    Log.d("ModifytextCustomFragment", "updateText: $updateText")
                    if (updateText == "default"){
                        binding.etCustomTextSummaryText.setText(getText)
                        Log.d("ModifytextCustomFragment", "텍스트 요약 설정: $getText")
                    } else {
                        binding.etCustomTextSummaryText.setText(updateText)
                        Log.d("ModifytextCustomFragment", "텍스트 요약 설정: $updateText")
                    }
                }
            }
        }
    }

    override fun initView() {
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.ivCustomTextCustomToolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // 메모 커스텀 버튼
        binding.btnCustomTextCustomMemo.setOnClickListener {
            handleSaveAndNavigate(::navigateToMemo)
        }

        // 알림 커스텀 버튼
        binding.btnCustomTextCustomAlarm.setOnClickListener {
            handleSaveAndNavigate(::navigateToAlarm)
        }

        // 저장 버튼
        binding.btnCustomTextCustomSave.setOnClickListener {
            handleSaveAndNavigate(::navigateToOpenLink)
        }

        // Delete 버튼
        binding.ivCustomTextCustomDelete.setOnClickListener {
            linkAddViewModel.clearLinkInput()
            binding.etCustomTextCustomLinkTitle.text.clear() // EditText의 내용을 직접 초기화
        }
    }

    private fun handleSaveAndNavigate(navigateAction: () -> Unit) {
        isSuccess = false

        updateTitle = binding.etCustomTextCustomLinkTitle.text.toString()
        updateText = binding.etCustomTextSummaryText.text.toString()
        updateMemo = getMemo
        updateAlert = getAlert

        linkAddViewModel.updateText(text = updateText!!)
        linkAddViewModel.updateTitle(title = updateTitle!!)
        linkAddViewModel.updateMemo(memo = updateMemo!!)
        linkAddViewModel.updateAlertDateAll(getAlert)
        Log.d("ModifytextCustomFragment", "updateTitle: $updateTitle\nupdateSummary: $updateText")

        if (!isSuccess) {
            isSuccess = true
            if (updateTitle!!.isEmpty()) {
                // 제목이 비어있으면 토스트 메시지 표시
                Toast.makeText(requireContext(), "제목을 설정해주세요", Toast.LENGTH_SHORT).show()
            } else {
                // 제목이 비어있지 않으면 이동
                navigateAction()
            }
        }
    }

    private fun navigateToMemo() {
        linkId?.let { id ->
            val action =
                ModifytextCustomFragmentDirections.actionModifyTextCustomFragmentToModifyTextMemoFragment(
                    id
                )
            Log.d("ModifytextCustomFragment", "linkId: $id 메모 페이지로 이동")
            findNavController().navigate(action)
        } ?: run {
            Log.d("ModifytextCustomFragment", "메모 페이지 이동 실패")
        }    }

    private fun navigateToAlarm() {
        linkId?.let { id ->
            val action =
                ModifytextCustomFragmentDirections.actionModifyTextCustomFragmentToModifyTextAlarmFragment(
                    id
                )
            Log.d("ModifytextCustomFragment", "linkId: $id 알림 페이지로 이동")
            findNavController().navigate(action)
        } ?: run {
            Log.d("ModifytextCustomFragment", "알림 페이지 이동 실패")
        }    }

    private fun navigateToOpenLink() {
        updateTitle = linkAddViewModel.link.value.title
        updateMemo = linkAddViewModel.link.value.memo
        updateText = linkAddViewModel.link.value.text
        updateAlert = linkAddViewModel.link.value.alertDate.toString()

        if(updateAlert=="null"){
            updateAlert=null
        }

        // Modify API 호출
        val linkModifyRequest = LinkModifyRequest(
            title = updateTitle!!,
            memo = updateMemo,
            text = updateText,
            alert_date = updateAlert
        )
        linkId?.let { linkModifyViewModel.modifyLink(it, linkModifyRequest) }
        Log.d(
            "ModifylinkCustomFragment",
            "Modify API 호출\ntitle=${updateTitle}\nmemo=${updateMemo}\ntext=${updateText}\nalert_date=${updateAlert}"
        )

        // Modify API 응답 후 이동 (linkId 설정)
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                linkModifyViewModel.linkModifyResponse.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> {
                            // 로딩 상태 처리
                            Log.d("ModifytextCustomFragment", "Loading Modify data")
                        }
                        is UiState.Success<*> -> {
                            Log.d("ModifytextCustomFragment", "링크 수정 성공 linkId: $linkId")

                            // 화면 이동
                            linkId?.let { id ->
                                val action =
                                    ModifytextCustomFragmentDirections.actionModifyTextCustomFragmentToOpenTextFragment(
                                        id, true
                                    )
                                Log.d("ModifytextCustomFragment", "linkId: $id 로 이동")
                                linkAddViewModel.resetState()
                                findNavController().navigate(action)
                            } ?: run {
                                Log.d("ModifytextCustomFragment", "이동 실패")
                            }
                        }

                        is UiState.Error -> {
                            Toast.makeText(requireContext(), "링크 수정 실패", Toast.LENGTH_SHORT)
                                .show()
                            Log.d("ModifytextCustomFragment", "링크 수정 실패")
                        }

                        UiState.Empty -> Log.d("ModifytextCustomFragment", "isEmpty")
                    }
                }
            }
        }
    }

}
