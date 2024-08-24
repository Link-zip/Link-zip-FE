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
import umc.link.zip.domain.model.link.LinkGetByLinkIDModel
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.create.adapter.LinkAddViewModel
import umc.link.zip.presentation.create.adapter.LinkGetByIDViewModel
import umc.link.zip.presentation.create.adapter.LinkModifyViewModel
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.network.UiState

@AndroidEntryPoint
class ModifylinkCustomFragment : BaseFragment<FragmentCustomlinkCustomBinding>(R.layout.fragment_customlink_custom) {

    private val linkGetByIDViewModel: LinkGetByIDViewModel by activityViewModels()
    private val linkAddViewModel: LinkAddViewModel by activityViewModels()
    private val linkModifyViewModel: LinkModifyViewModel by activityViewModels()

    private var linkId: Int? = null

    private var getTitle: String? = ""
    private var getMemo: String? = ""
    private var getAlert: String? = ""

    private var updateTitle: String? = ""
    private var updateMemo: String? = ""
    private var updateAlert: String? = ""

    private var isSuccess: Boolean = false

    private val getLinkId: Int? by lazy {
        arguments?.getInt("linkId")
    }
    private var bool : Boolean = true

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
                            Log.d("ModifylinkCustomFragment", "Loading get Link by ID data")
                        }

                        is UiState.Success<*> -> {
                            val data = state.data as LinkGetByLinkIDModel
                            // 제목
                            getTitle = data.title
                            // 메모
                            getMemo = data.memo
                            // 알림
                            getAlert = data.alert_date
                            Log.d("ModifylinkCustomFragment", "getTitle: $getTitle getMemo: $getMemo getAlert: $getAlert")
                            // 썸네일
                            val thumbnailUrl = data.thumb
                            if (thumbnailUrl == null) {
                                binding.ivCustomLinkCustomTopImg.setImageResource(R.drawable.iv_link_thumbnail_default)
                            } else {
                                Glide.with(binding.ivCustomLinkCustomTopImg.context)
                                    .load(thumbnailUrl)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.ivCustomLinkCustomTopImg)
                            }

                            Log.d("ModifylinkCustomFragment", "getLinkByID 가져오기 성공")

                        }

                        is UiState.Error -> {
                            Log.d("ModifylinkCustomFragment", "getLinkByID 가져오기 실패")
                        }

                        UiState.Empty -> Log.d("ModifylinkCustomFragment", "getLinkByID isEmpty")
                    }
                }
            }
        }
        // add API 응답
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                linkAddViewModel.link.collectLatest { link ->
                    if(bool == true) {
                        updateTitle = link.title
                        updateMemo = link.memo
                        updateAlert = link.alertDate
                        Log.d(
                            "ModifylinkCustomFragment",
                            "updateTitle: $updateTitle updateMemo: $updateMemo updateAlert: $updateAlert"
                        )
                        if (updateTitle == "default" || updateTitle == "") {
                            binding.etCustomLinkCustomLinkTitle.setText(getTitle)
                            Log.d("ModifylinkCustomFragment", "제목 설정: $getTitle")
                        } else {
                            binding.etCustomLinkCustomLinkTitle.setText(updateTitle)
                            Log.d("ModifylinkCustomFragment", "제목 설정: $updateTitle")
                        }
                        bool = false
                    }
                }
            }
        }
    }

    override fun initView() {
        setOnClickListener()

        binding.etCustomLinkCustomLinkTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                linkAddViewModel.updateLinkInput(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setOnClickListener() {
        binding.ivCustomLinkCustomToolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // 메모 커스텀 버튼
        binding.btnCustomLinkCustomMemo.setOnClickListener {
            handleSaveAndNavigate(::navigateToMemo)
        }

        // 알림 커스텀 버튼
        binding.btnCustomLinkCustomAlarm.setOnClickListener {
            handleSaveAndNavigate(::navigateToAlarm)
        }

        // 저장 버튼
        binding.btnCustomLinkCustomSave.setOnClickListener {
            handleSaveAndNavigate(::navigateToOpenLink)
        }

        // Delete 버튼
        binding.ivCustomLinkCustomDelete.setOnClickListener {
            linkAddViewModel.clearLinkInput()
            binding.etCustomLinkCustomLinkTitle.text.clear() // EditText의 내용을 직접 초기화
        }
    }

    private fun handleSaveAndNavigate(navigateAction: () -> Unit) {
        isSuccess = false

        updateTitle = binding.etCustomLinkCustomLinkTitle.text.toString()
        updateMemo = getMemo
        updateAlert = getAlert

        linkAddViewModel.updateTitle(title = updateTitle!!)
        linkAddViewModel.updateMemo(memo = updateMemo!!)
        linkAddViewModel.updateAlertDateAll(updateAlert)
        Log.d("handleSave", "$getTitle , $updateMemo, $updateAlert")
        if (!isSuccess) {
            isSuccess = true
            if (updateTitle!!.isEmpty()) {
                // 제목이 비어있으면 토스트 메시지 표시
                Toast.makeText(requireContext(), "제목을 설정해주세요", Toast.LENGTH_SHORT).show()
            } else {
                // 제목이 비어있지 않으면 ViewModel에 제목 저장하고 이동
                linkAddViewModel.updateTitle(updateTitle!!)
                navigateAction()
            }
        }
    }

    private fun navigateToMemo() {
        linkId?.let { id ->
            val action =
                ModifylinkCustomFragmentDirections.actionModifyLinkCustomFragmentToModifyLinkMemoFragment(
                    id
                )
            Log.d("ModifylinkCustomFragment", "linkId: $id 메모 페이지로 이동")
            findNavController().navigate(action)
        } ?: run {
            Log.d("ModifylinkCustomFragment", "메모 페이지 이동 실패")
        }    }

    private fun navigateToAlarm() {
        linkId?.let { id ->
            val action =
                ModifylinkCustomFragmentDirections.actionModifyLinkCustomFragmentToModifyLinkAlarmFragment(
                    id
                )
            Log.d("ModifylinkCustomFragment", "linkId: $id 알림 페이지로 이동")
            findNavController().navigate(action)
        } ?: run {
            Log.d("ModifylinkCustomFragment", "알림 페이지 이동 실패")
        }    }

    private fun navigateToOpenLink() {
        updateTitle = linkAddViewModel.link.value.title
        updateMemo = linkAddViewModel.link.value.memo
        val text: String? = null // text를 null로 지정
        updateAlert = linkAddViewModel.link.value.alertDate.toString()

        if(updateAlert=="null"){
            updateAlert=null
        }

        // Modify API 호출
        val linkModifyRequest = LinkModifyRequest(
            title = updateTitle!!,
            memo = updateMemo,
            text = text, // 여기서 null 값 전달
            alert_date = updateAlert
        )
        
        linkId?.let { linkModifyViewModel.modifyLink(it, linkModifyRequest) }
        Log.d(
            "ModifylinkCustomFragment",
            "Modify API 호출\ntitle=${updateTitle}\nmemo=${updateMemo}\ntext=${text}\nalert_date=${updateAlert}"
        )

        // Modify API 응답 후 이동 (linkId 설정)
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                linkModifyViewModel.linkModifyResponse.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> {
                            // 로딩 상태 처리
                            Log.d("ModifylinkCustomFragment", "Loading Modify data")
                        }
                        is UiState.Success<*> -> {
                            Log.d("ModifylinkCustomFragment", "링크 수정 성공 linkId: $linkId")

                            // 화면 이동
                            linkId?.let { id ->
                                val action =
                                    ModifylinkCustomFragmentDirections.actionModifyLinkCustomFragmentToOpenLinkFragment(
                                        id, true
                                    )
                                Log.d("ModifylinkCustomFragment", "linkId: $id 로 이동")
                                findNavController().navigate(action)
                            } ?: run {
                                Log.d("ModifylinkCustomFragment", "이동 실패")
                            }
                        }

                        is UiState.Error -> {
                            Toast.makeText(requireContext(), "링크 수정 실패", Toast.LENGTH_SHORT)
                                .show()
                            Log.d("ModifylinkCustomFragment", "링크 수정 실패")
                        }

                        UiState.Empty -> Log.d("ModifylinkCustomFragment", "isEmpty")
                    }
                }
            }
        }
    }

}
