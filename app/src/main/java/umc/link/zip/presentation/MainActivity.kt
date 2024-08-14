package umc.link.zip.presentation

import androidx.activity.viewModels
import androidx.navigation.NavController
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
        binding.mainBnv.setupWithNavController(navController)
        binding.mainBnv.itemIconTintList = null;
    }
}