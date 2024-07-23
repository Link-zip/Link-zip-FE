package umc.link.zip.presentation.mypage

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import umc.link.zip.R
import umc.link.zip.databinding.FragmentMypageProfileBinding
import umc.link.zip.presentation.base.BaseFragment

class MypageProfileFragment : BaseFragment<FragmentMypageProfileBinding>(R.layout.fragment_mypage_profile){
    override fun initObserver() {

    }

    override fun initView() {

        val iv = binding.ivMypageProfileUserInfoBox
        iv.setImageResource(R.drawable.blur_mypage_profile)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            applyBlurEffect(iv)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun applyBlurEffect(imageView: ImageView) {
        val blurEffect = RenderEffect.createBlurEffect(10f, 10f, Shader.TileMode.CLAMP)
        imageView.setRenderEffect(blurEffect)
    }
}