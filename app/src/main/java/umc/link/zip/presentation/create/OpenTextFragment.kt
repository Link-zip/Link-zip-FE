package umc.link.zip.presentation.create

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.databinding.FragmentOpenTextBinding
import umc.link.zip.domain.model.link.LinkGetByLinkIDModel
import umc.link.zip.domain.model.link.LinkVisitModel
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.create.adapter.LinkGetByIDViewModel
import umc.link.zip.presentation.create.adapter.LinkUpdateLikeViewModel
import umc.link.zip.presentation.create.adapter.LinkVisitViewModel
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.network.UiState
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class OpenTextFragment : BaseFragment<FragmentOpenTextBinding>(R.layout.fragment_open_text) {

    private val linkGetByIDViewModel: LinkGetByIDViewModel by activityViewModels()
    private val linkUpdateLikeViewModel: LinkUpdateLikeViewModel by viewModels()
    private val linkVisitViewModel: LinkVisitViewModel by viewModels()

    private var isSuccess: Boolean = false
    private var popUp: Boolean = false

    private val linkId: Int? by lazy {
        arguments?.getInt("linkId")
    }

    private val add by lazy {
        arguments?.getBoolean("add")
    }

    private val edit by lazy {
        arguments?.getBoolean("edit")
    }
    private var bool : Boolean = true

    private var url: String? = null
    private var isLike: Int? = null

    override fun initObserver() {
        val linkId = linkId ?: return
        popUp = false

        // GetLink API 호출
        linkGetByIDViewModel.getLinkByLinkID(linkId)
        Log.d("OpenTextFragment", "OpenLinkFragment linkId: $linkId")


        // GetLink API 응답
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                linkGetByIDViewModel.linkGetByLinkIDResponse.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> {
                            // 로딩 상태 처리
                            Log.d("OpenTextFragment", "Loading data")
                        }

                        is UiState.Success<*> -> {
                            val data = state.data as LinkGetByLinkIDModel
                            // url
                            url = data.url

                            // Zip name
                            binding.tvOpenTextZipname.text = data.zip_title.ifEmpty { "설정된 제목이 없습니다." }
                            // Zip img
                            val zipColor = data.zip_color
                            setBackgroundBasedOnColor(binding.ivOpenTextZipimg, zipColor)
                            // Zip name
                            binding.tvOpenTextCountingNumber.text = data.visit.toString()
                            // 제목
                            binding.tvOpenTextTitle.text = data.title.ifEmpty { "설정된 제목이 없습니다." }
                            // 썸네일
                            val thumbnailUrl = data.thumb
                            if (thumbnailUrl == null) {
                                binding.ivOpenTextTopImg.setImageResource(R.drawable.iv_link_thumbnail_default)
                            } else {
                                Glide.with(binding.ivOpenTextTopImg.context)
                                    .load(thumbnailUrl)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.ivOpenTextTopImg)
                            }
                            // 메모
                            binding.tvOpenTextMemo.text = data.memo?.ifEmpty { "설정된 메모가 없습니다." }
                            // 알림
                            val alertDate = data.alert_date ?: ""
                            binding.tvOpenTextAlarm.text = if (alertDate.isNotEmpty()) {
                                formatAlarm(alertDate)
                            } else {
                                "설정된 알림이 없습니다."
                            }
                            // 좋아요
                            isLike = data.like
                            if (data.like == 1) {
                                binding.ivOpenTextLike.setImageResource(R.drawable.ic_heart_selected)
                            } else {
                                binding.ivOpenTextLike.setImageResource(R.drawable.ic_heart_unselected)
                            }
                            // 텍스트 요약
                            binding.tvOpenTextSummary.text = data.text?.ifEmpty { "텍스트 요약이 없습니다." }

                            Log.d("OpenTextFragment", "링크 정보 가져오기 성공")

                            // Toast 표시
                            if (bool) {
                                if (add == true) {
                                    showCustomToast("링크 저장 완료")
                                }
                                if (edit == true) {
                                    showCustomToast("링크 수정 완료")
                                }
                                bool = false
                            }
                        }

                        is UiState.Error -> {
                            Toast.makeText(requireContext(), "링크 정보 가져오기 실패", Toast.LENGTH_SHORT)
                                .show()
                            Log.d("OpenTextFragment", "링크 정보 가져오기 실패")
                        }

                        UiState.Empty -> Log.d("OpenTextFragment", "링크 정보 isEmpty")
                    }
                }
            }
        }
        // 원본 링크 이동
        binding.btnOpenTextMove.setOnClickListener {
            isSuccess = false
            bool = false
            // VisitLink API 호출
            linkVisitViewModel.visitLink(linkId)
            Log.d("OpenTextFragment", "OpenLinkFragment VisitLink API 호출")

            // GetLink API 응답
            repeatOnStarted {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    linkVisitViewModel.visitLinkResponse.collectLatest { state ->
                        when (state) {
                            is UiState.Loading -> {
                                // 로딩 상태 처리
                                Log.d("OpenTextFragment", "Loading visit data")
                            }

                            is UiState.Success<*> -> {
                                val data = state.data as LinkVisitModel
                                // 방문 횟수
                                binding.tvOpenTextCountingNumber.text = data.visit.toString()
                                Log.d("OpenLinkFragment", "방문 횟수 가져오기 성공")

                                if (!isSuccess && !url.isNullOrEmpty()) {
                                    isSuccess = true
                                    Log.d("OpenLinkFragment", "isSuccess: $isSuccess")
                                    // 링크 이동
                                    moveLink()
                                    Log.d("OpenLinkFragment", "MoveLink 호출")
                                }

                                Log.d("OpenLinkFragment", "방문 횟수 가져오기 성공")

                            }

                            is UiState.Error -> {
                                Log.d("OpenTextFragment", "방문 횟수 가져오기 실패")
                            }

                            UiState.Empty -> Log.d("OpenTextFragment", "방문 횟수 isEmpty")
                        }
                    }
                }
            }
        }
    }

    override fun initView() {
        linkGetByIDViewModel.resetState()

        binding.ivOpenTextToolbarBack.setOnClickListener {
            if (add == true || edit == true) {
                navigateToHome()
            } else {
                findNavController().navigateUp()
            }
        }

        binding.btnOpenTextEdit.setOnClickListener {
            navigateToCustomTextCustom()
        }

        binding.ivOpenTextLike.setOnClickListener {
            isLike = if (isLike == 1) 0 else 1
            if (isLike == 1) {
                binding.ivOpenTextLike.setImageResource(R.drawable.ic_heart_selected)
                // UpdateLike API 호출
                linkId?.let { it1 -> linkUpdateLikeViewModel.updateLikeStatusOnServer(linkId = it1) }
            } else {
                binding.ivOpenTextLike.setImageResource(R.drawable.ic_heart_unselected)
                // UpdateLike API 호출
                linkId?.let { it1 -> linkUpdateLikeViewModel.updateLikeStatusOnServer(linkId = it1) }
            }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (add == true || edit == true) {
                    navigateToHome()
                } else {
                    findNavController().navigateUp()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.openTextFragment_to_homeFragmentTab)
    }

    private fun navigateToCustomTextCustom() {
        linkId?.let { id ->
            val action =
                OpenTextFragmentDirections.actionOpenTextFragmentToModifyTextCustomFragment(
                    id
                )
            Log.d("OpenTextFragment", "linkId: $id 수정하러 이동")
            findNavController().navigate(action)
        } ?: run {
            Log.d("OpenTextFragment", "linkId 가져오기 실패")
        }
    }

    private fun moveLink() {
        var finalUrl = url ?: return

        // URL이 http:// 또는 https://로 시작하지 않는다면 추가
        if (!finalUrl.startsWith("http://") && !finalUrl.startsWith("https://")) {
            finalUrl = "http://$finalUrl"
        }

        // 명시적으로 Chrome으로 URL 열기
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl))
        intent.setPackage("com.android.chrome")  // Chrome 패키지 명시

        // Chrome 앱이 설치되어 있는지 확인
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
            Log.d("OpenLinkFragment", "Chrome에서 웹 페이지 열기: $finalUrl")
        } else {
            Log.e("OpenLinkFragment", "Chrome이 설치되지 않음, 기본 브라우저로 시도")

            // Chrome이 없으면 일반 브라우저로 열기 시도
            val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl))
            if (fallbackIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(fallbackIntent)
                Log.d("OpenLinkFragment", "외부 브라우저로 이동: $finalUrl")
            } else {
                Log.e("OpenLinkFragment", "No activity found to handle the intent")
                Toast.makeText(requireContext(), "No application can handle this link", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun formatAlarm(alertDate: String): String {
        return try {
            // 입력 형식: 밀리초와 'Z'를 포함
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
             // 'Z'는 UTC를 나타냅니다

            // 출력 형식
            val outputFormat = SimpleDateFormat("yyyy.MM.dd a hh:mm", Locale.getDefault())

            // 문자열을 Date 객체로 파싱
            val parsedDate = inputFormat.parse(alertDate)

            // Date 객체를 문자열로 포맷팅
            parsedDate?.let { outputFormat.format(it) } ?: "Invalid Date"
        } catch (e: Exception) {
            "Invalid Date"
        }
    }

    private fun showCustomToast(text:String) {
        val inflater = LayoutInflater.from(requireActivity())
        val layout = inflater.inflate(R.layout.custom_toast, null)
        val tv = layout.findViewById<TextView>(R.id.tvSample)
        tv.text = text
        val toast = Toast(requireActivity()).apply {
            duration = Toast.LENGTH_SHORT
            view = layout
            setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 230)
        }

        toast.show()

    }

    private fun setBackgroundBasedOnColor(imageView: ImageView, color: String) {
        when (color.lowercase()) {
            "yellow" -> imageView.setBackgroundResource(R.drawable.ic_zip_shadow_1)
            "lightgreen" -> imageView.setBackgroundResource(R.drawable.ic_zip_shadow_2)
            "green" -> imageView.setBackgroundResource(R.drawable.ic_zip_shadow_3)
            "lightblue" -> imageView.setBackgroundResource(R.drawable.ic_zip_shadow_4)
            "blue" -> imageView.setBackgroundResource(R.drawable.ic_zip_shadow_5)
            "darkpurple" -> imageView.setBackgroundResource(R.drawable.ic_zip_shadow_6)
            "purple" -> imageView.setBackgroundResource(R.drawable.ic_zip_shadow_7)
            else -> imageView.setBackgroundResource(R.drawable.ic_zip_clip_shadow) // default
        }
    }
}