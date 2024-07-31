package com.example.letterboxn.presentation.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.core.view.isGone
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.letterboxn.R
import com.example.letterboxn.common.utils.NetworkConnection
import com.example.letterboxn.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var networkConnection: NetworkConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        networkConnection = NetworkConnection(applicationContext)

        networkConnection.isConnected.observe(this) { isConnected ->
            if (!isConnected) {
                showConnectionLostMessage()
            }
        }

//        binding.bottomNavigationView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                // Remove the listener to avoid multiple calls
//                binding.bottomNavigationView.viewTreeObserver.removeOnGlobalLayoutListener(this)
//
//                // Set the marginBottom of fragmentContainerView to the height of bottomNavigationView
//                binding.fragmentContainerView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
//                    bottomMargin = binding.bottomNavigationView.height
//                }
//            }
//        })

//        binding.fragmentContainerView.marginBottom = binding.bottomNavigationView.height

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
        Snackbar.make(findViewById(android.R.id.content), "Connection Lost", Snackbar.LENGTH_INDEFINITE).apply {
                setAction("Dismiss") {
                    dismiss()
                }.show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkConnection.unregister()
    }

    private fun setUpBottomNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
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

}