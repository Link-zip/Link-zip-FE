package umc.link.zip.presentation

import android.content.Intent
import android.os.Handler
import android.os.Looper
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.ActivitySplashBinding
import umc.link.zip.presentation.base.BaseActivity

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash){
    override fun initView() {
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 1000)
    }

    override fun initObserver() {
    }

}