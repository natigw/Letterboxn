package com.example.letterboxn.presentation.ui.fragments.home

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.Html
import android.view.Gravity
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.common.utils.nancyToastError
import com.example.letterboxn.common.utils.nancyToastInfo
import com.example.letterboxn.common.utils.nancyToastSuccess
import com.example.letterboxn.common.utils.numberFormatterSpaced
import com.example.letterboxn.common.utils.startShimmer
import com.example.letterboxn.common.utils.stopShimmer
import com.example.letterboxn.databinding.FragmentHomeBinding
import com.example.letterboxn.presentation.adapters.HomePopularListsAdapter
import com.example.letterboxn.presentation.adapters.HomePopularMoviesAdapter
import com.example.letterboxn.presentation.adapters.HomeReviewsAdapter
import com.example.letterboxn.presentation.ui.activities.OnBoardingActivity
import com.example.letterboxn.presentation.viewmodels.HomeViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewmodel by viewModels<HomeViewModel>()

    private val popularAdapter = HomePopularMoviesAdapter(
        onClick = {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailsMovieFragment(it.movieId)
            )
        }
    )
    private val popularListAdapter = HomePopularListsAdapter {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToPopularListsHomeBottomSheetFragment(it)
        )
    }
    private val reviewAdapter = HomeReviewsAdapter {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToRecentReviewDetailsBottomSheetFragment(it)
        )
    }

    private var username: String? = null
    private var email: String? = null

    override fun onViewCreatedLight() {
        setUI()
        setAdapters()
        updateAdapters()
    }

    private fun updateAdapters() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.popularMovies.collect {
                popularAdapter.updateAdapter(it)
                stopShimmer(binding.shimmerPopularMoviesHome)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.popularLists.collectLatest {
                popularListAdapter.updateAdapter(it)
                stopShimmer(binding.shimmerPopularListsHome)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.recentReviews.collectLatest {
                reviewAdapter.updateAdapter(it)
                stopShimmer(binding.shimmerRecentReviewsHome)
            }
        }
    }

    private fun setAdapters() {
        binding.rvPopularMoviesHome.adapter = popularAdapter
        binding.rvPopularListsHome.adapter = popularListAdapter
        binding.rvRecentReviewsHome.adapter = reviewAdapter
    }

    private fun setUI() {

        username = viewmodel.userName ?: getString(R.string.user)
        email = viewmodel.userEmail

        val drawerLayout = binding.myDrawerLayout
        val navigationView: NavigationView = requireActivity().findViewById(R.id.drawerNavigationHome)

        val headerView = navigationView.getHeaderView(0)
        val userProfilePicture = headerView.findViewById<ShapeableImageView>(R.id.imageUserppDrawer)
        val userNameDrawer = headerView.findViewById<TextView>(R.id.textUsernamedrawer)
        val userEmailDrawer = headerView.findViewById<TextView>(R.id.textUseremaildrawer)
        val followerCountDrawer = headerView.findViewById<Chip>(R.id.chipFollowersdrawer)
        val followingCountDrawer = headerView.findViewById<Chip>(R.id.chipFollowingsdrawer)
        userNameDrawer.text = username
        userEmailDrawer.text = email
        followerCountDrawer.text = "${numberFormatterSpaced(viewmodel.followerCount.toLong())} ${getString(R.string.followers)}"
        followingCountDrawer.text = "${numberFormatterSpaced(viewmodel.followingCount.toLong())} ${getString(R.string.followings)}"
        binding.textGreetingHome.text = Html.fromHtml("${getString(R.string.greeting_text)}, <font color=\"#E9A6A6\">$username</font>!")

        Glide.with(binding.imageUserProfilePictureHome)
            .load(getProfilePictureUri())
            .placeholder(R.drawable.placeholder_user)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageUserProfilePictureHome)

        Glide.with(userProfilePicture)
            .load(getProfilePictureUri())
            .placeholder(R.drawable.placeholder_user)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(userProfilePicture)

        if (viewmodel.status) binding.imageUserStatusHome.setImageResource(R.drawable.circle_green)
        else binding.imageUserStatusHome.setImageResource(R.drawable.circle_red)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_films -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    findNavController().navigate(R.id.exploreFragment)
                    true
                }
                R.id.nav_diary -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    findNavController().navigate(R.id.profileFragment)
                    true
                }
                R.id.nav_reviews -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    findNavController().navigate(R.id.profileFragment)
                    true
                }
                R.id.nav_watchlist -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    findNavController().navigate(R.id.watchlistBottomSheetFragment)
                    true
                }
                R.id.nav_lists -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_likes -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    findNavController().navigate(R.id.profileFragment)
                    true
                }
                R.id.nav_logout -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    viewmodel.sharedPrefLoggedIn.edit().putBoolean("status", false).apply()
                    nancyToastInfo(requireContext(), getString(R.string.logout_info_message))
                    navigateToOnBoardActivity()
                    true
                }
                else -> false
            }
        }
    }

    override fun clickListeners() {
        super.clickListeners()
        binding.toggleDrawerHome.setOnClickListener {
            binding.myDrawerLayout.openDrawer(Gravity.LEFT, true)
        }
        binding.imageUserProfilePictureHome.setOnClickListener {
            showStatusDialog()
        }
    }

    private fun navigateToOnBoardActivity() {
        val intent = Intent(requireContext(), OnBoardingActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun showStatusDialog() {
        val customTitle = TextView(requireContext()).apply {
            text = getString(R.string.__change_your_status)
            setPadding(40, 40, 40, 40)
            textSize = 24f
            setTextColor(Color.WHITE)
        }

        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setCustomTitle(customTitle)
            .setTitle(getString(R.string.__change_your_status))
            .setMessage(getString(R.string.change_status_explanation))
            .setNeutralButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
                nancyToastSuccess(requireContext(), getString(R.string.operation_cancelled))
            }
            .setNegativeButton(getString(R.string.offline)) { _, _ ->
                binding.imageUserStatusHome.setImageResource(R.drawable.circle_red)
                nancyToastError(requireContext(), getString(R.string.status_offline))
                viewmodel.sharedPrefStatus.edit().putBoolean("status", false).apply()

            }
            .setPositiveButton(getString(R.string.online)) { _, _ ->
                binding.imageUserStatusHome.setImageResource(R.drawable.circle_green)
                nancyToastSuccess(requireContext(), getString(R.string.status_online))
                viewmodel.sharedPrefStatus.edit().putBoolean("status", true).apply()
            }
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE)
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.WHITE)

            val textViewId = dialog.context.resources.getIdentifier("android:id/alertTitle", null, null)
            val textView = dialog.findViewById<TextView>(textViewId)
            textView?.setTextColor(Color.WHITE)

            val messageTextViewId = dialog.context.resources.getIdentifier("android:id/message", null, null)
            val messageTextView = dialog.findViewById<TextView>(messageTextViewId)
            messageTextView?.setTextColor(Color.WHITE)
        }

        dialog.show()
    }

    private fun getProfilePictureUri(): Uri {
        val uriImage = viewmodel.sharedPrefProfilePicture.getString("profile_image_uri", null)
        return if (uriImage != null) {
            Uri.parse(uriImage)
        } else {
            // return default drawable uri
            Uri.parse("android.resource://${requireActivity().packageName}/${R.drawable.placeholder_user}")
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewmodel.popularMovies.value.isEmpty()) startShimmer(binding.shimmerPopularMoviesHome)
        if (viewmodel.popularLists.value.isEmpty()) startShimmer(binding.shimmerPopularListsHome)
        if (viewmodel.recentReviews.value.isEmpty()) startShimmer(binding.shimmerRecentReviewsHome)
    }

    override fun onPause() {
        super.onPause()
        stopShimmer(binding.shimmerPopularMoviesHome)
        stopShimmer(binding.shimmerPopularListsHome)
        stopShimmer(binding.shimmerRecentReviewsHome)
    }
}