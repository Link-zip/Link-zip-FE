package umc.link.zip.presentation.create

import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.databinding.FragmentOpenTextBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.util.extension.repeatOnStarted
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class OpenTextFragment : BaseFragment<FragmentOpenTextBinding>(R.layout.fragment_open_text){
    private val createViewModel: CreateViewModel by activityViewModels()
    private val openViewModel: OpenViewModel by activityViewModels()

    override fun initObserver() {
        repeatOnStarted {
            createViewModel.link.collectLatest { link ->
                // 제목
                binding.tvOpenTextTitle.text = link.title ?: "No Title"

                // 메모
                binding.tvOpenTextMemo.text = link.memo.ifEmpty { "설정된 메모가 없습니다." }

                // 알림
                val alertDate = link.alertDate ?: ""
                binding.tvOpenTextAlarm.text = if (alertDate.isNotEmpty()) {
                    formatAlarm(alertDate)
                } else {
                    "설정된 알림이 없습니다."
                }

                // 텍스트 요약
                binding.tvOpenTextSummary.text = link.text.ifEmpty { "텍스트 요약이 없습니다." }

            }
        }
    }

    override fun initView() {
        binding.ivOpenTextToolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnOpenTextEdit.setOnClickListener {
            navigateToCustomTextCustom()
        }

        // 원본 링크 이동
        binding.btnOpenTextMove.setOnClickListener {
            createViewModel.link.value?.url?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }

        // Toast 표시
        showCustomToast()
    }

    private fun navigateToCustomTextCustom() {
        findNavController().navigate(R.id.action_openTextFragment_to_customTextCustomFragment)
    }

    private fun formatAlarm(alertDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy.MM.dd a hh:mm", Locale.getDefault())
            val parsedDate = inputFormat.parse(alertDate)
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
}