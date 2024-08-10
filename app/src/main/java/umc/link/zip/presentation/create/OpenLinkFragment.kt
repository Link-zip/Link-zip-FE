package umc.link.zip.presentation.create

import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.databinding.FragmentOpenLinkBinding
import umc.link.zip.presentation.base.BaseFragment
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class OpenLinkFragment : BaseFragment<FragmentOpenLinkBinding>(R.layout.fragment_open_link) {

    private val viewModel: CreateViewModel by activityViewModels()

    override fun initObserver() {
        lifecycleScope.launch {
            viewModel.link.collectLatest { link ->
                // 제목
                binding.tvOpenLinkTitle.text = link.title ?: "No Title"

                // 메모
                binding.tvOpenLinkMemo.text = link.memo ?: "No Memo"

                // 알림
                val alertDate = link.alertDate ?: ""
                if (alertDate.isNotEmpty()) {
                    val alert = alertDate.removeSuffix("Z")
                    val formattedAlarm = formatAlarm(alert)
                    binding.tvOpenLinkAlarm.text = formattedAlarm
                } else {
                    binding.tvOpenLinkAlarm.text = "No Alarm"
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
            duration = Toast.LENGTH_LONG
            view = layout
            setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 250)
        }

        toast.show()
    }



    /*
private fun setLike(link: Link, view: ImageView, onLikeChanged: (Link) -> Unit) {
    // 초기 상태 설정
    if (link.likes == 1) {
        view.setImageResource(R.drawable.ic_heart_selected)
    } else {
        view.setImageResource(R.drawable.ic_heart_unselected)
    }

    // 클릭 리스너 설정
    view.setOnClickListener {
        link.likes = if (link.likes == 1) 0 else 1
        if (link.likes == 1) {
            view.setImageResource(R.drawable.ic_heart_selected)
        } else {
            view.setImageResource(R.drawable.ic_heart_unselected)
        }
        // 좋아요 상태가 변경되었음을 외부에 알림
        onLikeChanged(link)
    }
}*/

}