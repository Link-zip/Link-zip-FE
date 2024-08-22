package umc.link.zip.presentation.create

import android.util.Log
import android.webkit.WebViewClient
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentWebViewBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class WebViewFragment : BaseFragment<FragmentWebViewBinding>(R.layout.fragment_web_view) {

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
            Log.d("WebViewFragment", "링크 이동 성공")
        }

        Log.d("WebViewFragment", "링크 이동 성공")


    }

    override fun initView() {
    }


}
