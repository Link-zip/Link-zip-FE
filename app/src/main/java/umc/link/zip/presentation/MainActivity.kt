package umc.link.zip.presentation

import android.os.Build
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.ActivityMainBinding
import umc.link.zip.presentation.base.BaseActivity
import umc.link.zip.presentation.home.HomeFragment
import umc.link.zip.presentation.home.HomeViewModel
import umc.link.zip.util.extension.repeatOnStarted

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var navController: NavController
    private val viewModel: HomeViewModel by viewModels()
    override fun initView() {
        initNavigator()
    }

    override fun initObserver() {
        repeatOnStarted {
            viewModel.navigateEvent.collect { id ->
                id?.let {
                    binding.mainBnv.selectedItemId = it
                }
            }
        }
    }

    private fun initNavigator() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.mainBnv.setupWithNavController(navController)
        binding.mainBnv.itemIconTintList = null

        // Fragment 초기화 설정
        binding.mainBnv.setOnItemSelectedListener { item ->
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.main_graph, true) // homeFragmentTab을 남기고 나머지 스택 제거
                .setLaunchSingleTop(true) // 동일한 프래그먼트가 중복으로 스택에 쌓이지 않도록 설정
                .build()

            when (item.itemId) {
                R.id.homeFragmentTab -> {
                    navController.navigate(R.id.homeFragmentTab, null, navOptions)
                    true
                }
                R.id.zipFragmentTab -> {
                    navController.navigate(R.id.zipFragmentTab, null, navOptions)
                    true
                }
                R.id.createFragmentTab -> {
                    navController.navigate(R.id.createFragmentTab, null, navOptions)
                    true
                }
                R.id.listFragmentTab -> {
                    navController.navigate(R.id.listFragmentTab, null, navOptions)
                    true
                }
                R.id.mypageFragmentTab -> {
                    navController.navigate(R.id.mypageFragmentTab, null, navOptions)
                    true
                }
                else -> false
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            @RequiresApi(Build.VERSION_CODES.P)
            override fun handleOnBackPressed() {
                if (!navController.popBackStack()) {
                    val fragment = navHostFragment.childFragmentManager.fragments[0]
                    if (fragment !is HomeFragment) {
                        binding.mainBnv.selectedItemId = R.id.homeFragmentTab
                        navController.navigate(R.id.homeFragmentTab)
                    } else {
                        finish()
                    }
                }
            }
        })
    }
}