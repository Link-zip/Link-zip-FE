package umc.link.zip.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.ActivityMainBinding
import umc.link.zip.presentation.base.BaseActivity
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
        binding.mainBnv.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment, null, NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setPopUpTo(R.id.main_graph, false)
                        .build())
                    true
                }

                R.id.zipFragment -> {
                    navController.navigate(R.id.zipFragment, null, NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setPopUpTo(R.id.main_graph, false)
                        .build())
                    true
                }

                R.id.listFragment -> {
                    navController.navigate(R.id.listFragment, null, NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setPopUpTo(R.id.main_graph, false)
                        .build())
                    true
                }

                R.id.mypageFragment -> {
                    navController.navigate(R.id.mypageFragment, null, NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setPopUpTo(R.id.main_graph, false)
                        .build())
                    true
                }

                else -> false
            }
        }
        binding.mainBnv.setupWithNavController(navController)
        binding.mainBnv.itemIconTintList = null;
    }
}