package umc.link.zip.presentation.create

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.webkit.WebViewClient
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
import umc.link.zip.databinding.FragmentWebViewBinding
import umc.link.zip.domain.model.create.Link
import umc.link.zip.domain.model.link.LinkGetByLinkIDModel
import umc.link.zip.domain.model.link.LinkVisitModel
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.create.adapter.CreateViewModel
import umc.link.zip.presentation.create.adapter.LinkGetByIDViewModel
import umc.link.zip.presentation.create.adapter.LinkUpdateLikeViewModel
import umc.link.zip.presentation.create.adapter.LinkVisitViewModel
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.extension.setImageResource
import umc.link.zip.util.network.UiState
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class WebViewFragment : BaseFragment<FragmentWebViewBinding>(R.layout.fragment_web_view) {

    private val linkGetByIDViewModel: LinkGetByIDViewModel by activityViewModels()


    private val url: String? by lazy {
        arguments?.getString("url")
    }

    override fun initObserver() {
        val url = url ?: return

        // WebView 설정
        binding.webView.apply {
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true

            webViewClient = WebViewClient()  // 외부 브라우저가 아닌 WebView에서 링크 열기
        }

        // 링크 이동
        url.let {
            binding.webView.loadUrl(it)
            Log.d("OpenLinkFragment", "링크 이동 성공")
        }

        Log.d("OpenLinkFragment", "링크 이동 성공")


    }

    override fun initView() {
    }


}
