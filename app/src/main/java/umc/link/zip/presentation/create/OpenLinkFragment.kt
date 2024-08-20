package umc.link.zip.presentation.create

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
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
import umc.link.zip.databinding.FragmentOpenLinkBinding
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
class OpenLinkFragment : BaseFragment<FragmentOpenLinkBinding>(R.layout.fragment_open_link) {

    private val linkGetByIDViewModel: LinkGetByIDViewModel by activityViewModels()
    private val linkUpdateLikeViewModel: LinkUpdateLikeViewModel by activityViewModels()
    private val linkVisitViewModel: LinkVisitViewModel by activityViewModels()

    private var isSuccess: Boolean = false

    private val linkId: Int? by lazy {
        arguments?.getInt("linkId")
    }

    private var url: String? = null
    private var isLike: Int? = null

    override fun initObserver() {
        val linkId = linkId ?: return

        // GetLink API 호출
        linkGetByIDViewModel.getLinkByLinkID(linkId)
        Log.d("OpenLinkFragment", "OpenLinkFragment linkId: $linkId")


        // GetLink API 응답
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                linkGetByIDViewModel.linkGetByLinkIDResponse.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> {
                            // 로딩 상태 처리
                            Log.d("OpenLinkFragment", "Loading data")
                        }

                        is UiState.Success<*> -> {
                            val data = state.data as LinkGetByLinkIDModel
                            // url
                            url = data.url

                            // Zip name
                            binding.tvOpenLinkZipname.text = data.zip_title.ifEmpty { "none" }
                            // Zip img
                            val zipColor = data.zip_color
                            setBackgroundBasedOnColor(binding.ivOpenLinkZipimg, zipColor)
                            // 방문 횟수
                            binding.tvOpenLinkCountingNumber.text = data.visit.toString()
                            // 제목
                            binding.tvOpenLinkTitle.text = data.title.ifEmpty { "설정된 제목이 없습니다." }
                            // 썸네일
                            val thumbnailUrl = data.thumb
                            if (thumbnailUrl == null) {
                                binding.ivOpenLinkTopImg.setImageResource(R.drawable.iv_link_thumbnail_default)
                            } else {
                                Glide.with(binding.ivOpenLinkTopImg.context)
                                    .load(thumbnailUrl)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.ivOpenLinkTopImg)
                            }
                            // 메모
                            binding.tvOpenLinkMemo.text = data.memo?.ifEmpty { "설정된 메모가 없습니다." }
                            // 알림
                            val alertDate = data.alert_date ?: ""
                            binding.tvOpenLinkAlarm.text = if (alertDate.isNotEmpty()) {
                                formatAlarm(alertDate)
                            } else {
                                "설정된 알림이 없습니다."
                            }
                            // 좋아요
                            isLike = data.like
                            if (data.like == 1) {
                                binding.ivOpenLinkLike.setImageResource(R.drawable.ic_heart_selected)
                            } else {
                                binding.ivOpenLinkLike.setImageResource(R.drawable.ic_heart_unselected)
                            }
                            Log.d("OpenLinkFragment", "링크 정보 가져오기 성공")

                        }

                        is UiState.Error -> {
                            Toast.makeText(requireContext(), "링크 정보 가져오기 실패", Toast.LENGTH_SHORT)
                                .show()
                            Log.d("OpenLinkFragment", "링크 정보 가져오기 실패")
                        }

                        UiState.Empty -> Log.d("OpenLinkFragment", "링크 정보 isEmpty")
                    }
                }
            }
        }
        // 원본 링크 이동
        binding.btnOpenLinkMove.setOnClickListener {
            isSuccess = false
            // VisitLink API 호출
            linkVisitViewModel.visitLink(linkId)
            Log.d("OpenLinkFragment", "OpenLinkFragment VisitLink API 호출")

            // GetLink API 응답
            repeatOnStarted {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    linkVisitViewModel.visitLinkResponse.collectLatest { state ->
                        when (state) {
                            is UiState.Loading -> {
                                // 로딩 상태 처리
                                Log.d("OpenLinkFragment", "Loading visit data")
                            }

                            is UiState.Success<*> -> {
                                val data = state.data as LinkVisitModel
                                // 방문 횟수
                                binding.tvOpenLinkCountingNumber.text = data.visit.toString()
                                Log.d("OpenLinkFragment", "방문 횟수 가져오기 성공")

                                if (!isSuccess) {
                                    isSuccess = true
                                    // 링크 이동
                                    navigateToWebView()
                                    Log.d("OpenLinkFragment", "MoveLink 호출")
                                }

                                Log.d("OpenLinkFragment", "방문 횟수 가져오기 성공")

                            }

                            is UiState.Error -> {
                                Log.d("OpenLinkFragment", "방문 횟수 가져오기 실패")
                            }

                            UiState.Empty -> Log.d("OpenLinkFragment", "방문 횟수 isEmpty")
                        }
                    }
                }
            }
        }

    }


    override fun initView() {

        binding.ivOpenLinkToolbarBack.setOnClickListener {
            navigateToHome()
        }

        binding.btnOpenLinkEdit.setOnClickListener {
            navigateToCustomLinkCustom()
        }

        binding.ivOpenLinkLike.setOnClickListener {
            isLike = if (isLike == 1) 0 else 1
            if (isLike == 1) {
                binding.ivOpenLinkLike.setImageResource(R.drawable.ic_heart_selected)
                // UpdateLike API 호출
                linkId?.let { it1 -> linkUpdateLikeViewModel.updateLikeStatusOnServer(linkId = it1) }
            } else {
                binding.ivOpenLinkLike.setImageResource(R.drawable.ic_heart_unselected)
                // UpdateLike API 호출
                linkId?.let { it1 -> linkUpdateLikeViewModel.updateLikeStatusOnServer(linkId = it1) }
            }
        }

        // Toast 표시
        showCustomToast()
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.openLinkFragment_to_homeFragment)
    }

    private fun navigateToCustomLinkCustom() {
        findNavController().navigate(R.id.action_openLinkFragment_to_customLinkCustomFragment)
    }

    private fun navigateToWebView() {
        url?.let { url ->
            val action = OpenLinkFragmentDirections.actionOpenLinkFragmentToWebViewFragment(url)
            Log.d("OpenLinkFragment", "url: $url")
            findNavController().navigate(action)
        } ?: run {
            Log.d("OpenLinkFragment", "url 가져오기 실패")
        }
    }

    private fun formatAlarm(alertDate: String): String {
        return try {
            // 입력 형식: 밀리초와 'Z'를 포함
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC") // 'Z'는 UTC를 나타냅니다

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

    private fun showCustomToast() {
        val inflater = LayoutInflater.from(requireActivity())
        val layout = inflater.inflate(R.layout.custom_toast, null)

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