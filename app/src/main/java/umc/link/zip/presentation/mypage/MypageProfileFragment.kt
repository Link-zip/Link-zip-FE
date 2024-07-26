package umc.link.zip.presentation.mypage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import jp.wasabeef.blurry.Blurry
import umc.link.zip.R
import umc.link.zip.databinding.FragmentMypageProfileBinding
import umc.link.zip.presentation.base.BaseFragment

class MypageProfileFragment : BaseFragment<FragmentMypageProfileBinding>(R.layout.fragment_mypage_profile) {
    override fun initObserver() {

    }

    override fun initView() {
        // 블러 처리
        applyBlurToImageView(binding.ivMypageProfileUserInfoBoxBg)
        applyBlurToImageView(binding.ivMypageProfileUserNicknmChangeBoxBg)
    }


    private fun applyBlurToImageView(view: BlurView) {
        val window = requireActivity().window
        val radius = 16f

        val decorView = window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowBackground = decorView.background
        view.outlineProvider = ViewOutlineProvider.BOUNDS;
        view.setClipToOutline(true);

        view.setupWith(rootView, context?.let { RenderScriptBlur(it) }) // or RenderEffectBlur
            .setFrameClearDrawable(windowBackground) // Optional
            .setBlurRadius(radius)

    }
}