package umc.link.zip.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.ActivityMainBinding
import umc.link.zip.presentation.base.BaseActivity

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var navController: NavController
    override fun initView() {
        initNavigator()
    }

    override fun initObserver() {

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