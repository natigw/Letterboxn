package com.example.letterboxn.presentation.ui.activities

import android.view.View
import androidx.core.view.forEach
import androidx.core.view.isGone
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseActivity
import com.example.letterboxn.common.utils.NetworkConnection
import com.example.letterboxn.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private lateinit var networkConnection: NetworkConnection

    override fun onCreateLight() {
        networkConnection = NetworkConnection(applicationContext)

        networkConnection.isConnected.observe(this) { isConnected ->
            if (!isConnected) {
                showConnectionLostMessage()
            }
        }

        //disabling bottom navigation hint
        binding.bottomNavigationView.menu.forEach {
            val menuItem = binding.bottomNavigationView.findViewById<View>(it.itemId)
            menuItem.setOnLongClickListener {
                true
            }
        }

        setUpBottomNavigation()
    }

    private fun showConnectionLostMessage() {
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.connection_lost), Snackbar.LENGTH_INDEFINITE).apply {
            setAction(getString(R.string.dismiss)) {
                dismiss()
            }.show()
        }
    }

    private fun setUpBottomNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerViewMain) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.exploreFragment, R.id.profileFragment,
                R.id.popularListsHomeBottomSheetFragment, R.id.recentReviewDetailsBottomSheetFragment,
                R.id.categoryMoviesExploreBottomSheetFragment, R.id.watchlistBottomSheetFragment,
                R.id.favMoviesBottomSheetFragment -> binding.bottomNavigationView.isGone = false
                else -> binding.bottomNavigationView.isGone = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkConnection.unregister()
    }
}