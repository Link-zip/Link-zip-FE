package umc.link.zip.presentation.create

import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.databinding.FragmentOpenLinkBinding
import umc.link.zip.domain.model.create.Link
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.util.extension.repeatOnStarted
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class OpenLinkFragment : BaseFragment<FragmentOpenLinkBinding>(R.layout.fragment_open_link) {

    private val createViewModel: CreateViewModel by activityViewModels()
    private val openViewModel: OpenViewModel by activityViewModels()

    override fun initObserver() {
        repeatOnStarted {
            createViewModel.link.collectLatest { link ->
                // 제목
                binding.tvOpenLinkTitle.text = link.title ?: "설정된 제목이 없습니다."

                // 메모
                binding.tvOpenLinkMemo.text = link.memo ?: "설정된 메모가 없습니다."

                // 알림
                val alertDate = link.alertDate ?: ""
                if (alertDate.isNotEmpty()) {
                    val alert = alertDate.removeSuffix("Z")
                    val formattedAlarm = formatAlarm(alert)
                    binding.tvOpenLinkAlarm.text = formattedAlarm
                } else {
                    binding.tvOpenLinkAlarm.text = "설정된 알림이 없습니다."
                }
            }
        }
    }

    override fun initView() {
        binding.ivOpenLinkToolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnOpenLinkEdit.setOnClickListener {
            navigateToCustomLinkCustom()
        }

        // 원본 링크 이동
        binding.btnOpenLinkMove.setOnClickListener {
            createViewModel.link.value?.url?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        }

        // Toast 표시
        showCustomToast()
    }

    private fun navigateToCustomLinkCustom() {
        findNavController().navigate(R.id.action_openLinkFragment_to_customLinkCustomFragment)
    }

    private fun formatAlarm(alert: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy.MM.dd a hh:mm", Locale.ENGLISH)
            val parsedAlert = inputFormat.parse(alert)
            outputFormat.format(parsedAlert)
        } catch (e: Exception) {
            "Invalid Date"
        }
    }

    private fun showCustomToast() {
        val inflater = LayoutInflater.from(requireActivity()) // requireActivity()를 사용
        val layout = inflater.inflate(R.layout.custom_toast, null)

        val toast = Toast(requireActivity()).apply {
            duration = Toast.LENGTH_SHORT
            view = layout
            setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 230)
        }

        toast.show()
    }

}