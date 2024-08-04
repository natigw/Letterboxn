package com.example.letterboxn.presentation.ui.fragments.home

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.text.Html
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.databinding.FragmentHomeBinding
import com.example.letterboxn.presentation.adapters.HomePopularMoviesAdapter
import com.example.letterboxn.presentation.ui.activities.OnBoardingActivity
import com.example.letterboxn.presentation.viewmodels.HomeViewModel
import com.example.letterboxn.common.utils.NancyToast
import com.example.letterboxn.common.utils.numberFormatterSpaced
import com.example.letterboxn.data.remote.api.TmdbApi
import com.example.letterboxn.data.remote.model.popularList.ResultPopularList
import com.example.letterboxn.presentation.adapters.HomePopularListsAdapter
import com.example.letterboxn.presentation.adapters.HomeReviewsAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    @Inject
    lateinit var api : TmdbApi

    @Inject
    lateinit var firestore: FirebaseFirestore

    @Inject
    @Named("UserLoggedIn")
    lateinit var shprefLoggedin: SharedPreferences

    @Inject
    @Named("UserStatusInApp")
    lateinit var shprefStatus: SharedPreferences

    @Inject
    @Named("UserProfileImage")
    lateinit var shprefProfilePicture : SharedPreferences

    val viewmodel: HomeViewModel by viewModels()

    private val popularAdapter = HomePopularMoviesAdapter(
        onClick = {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailsMovieFragment(it.movieId)
            )
        }
    )
    private val listAdapter = HomePopularListsAdapter {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToPopularListsHomeBottomSheetFragment(it)
        )
    }
    private val reviewAdapter = HomeReviewsAdapter {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToRecentReviewDetailsBottomSheetFragment(it)
        )
    }

    private lateinit var shimmerPopularMovies : ShimmerFrameLayout
    private lateinit var shimmerPopularLists : ShimmerFrameLayout
    private lateinit var shimmerHomeReviews : ShimmerFrameLayout

    override fun onViewCreatedLight() {
        startShimmers()
        setUI()
        setAdapters()
        updateAdapters()
    }

    private fun updateAdapters() {
        lifecycleScope.launch {
            viewmodel.movies.collect {
                popularAdapter.updateAdapter(it)
            }
        }
//        lifecycleScope.launch {
//            viewmodel.lists.collect {
//                listAdapter.updateAdapter(it)
//            }
//        }
//        lifecycleScope.launch {
//            viewmodel.
//                .collect {
//                    reviewAdapter.updateAdapter(it)
//                }
//        }
        lifecycleScope.launch {
            val responseList1 = api.getLists(1).results
            val responseList2 = api.getLists(2).results
            val responseList3 = api.getLists(3).results
            val responseList4 = api.getLists(4).results
            val responseHuman = api.getPeople(1).resultPeople

            val responseList = mutableListOf<ResultPopularList>()

            repeat(10) {
                responseList.add(responseList1[it])
                responseList.add(responseList2[it])
                responseList.add(responseList3[it])
                responseList.add(responseList4[it])
            }

            listAdapter.updateAdapter(responseList, responseHuman)
            reviewAdapter.updateAdapter(responseList1, responseHuman)

            stopShimmers()
        }
    }

    private fun setAdapters() {
        binding.rvpopularmovies.adapter = popularAdapter
        binding.rvpopularlists.adapter = listAdapter
        binding.rvrecentreviews.adapter = reviewAdapter
    }

    private fun setUI() {
        val drawerLayout = binding.myDrawerLayout
        val navigationView = requireActivity().findViewById(R.id.drawernavigationhome) as NavigationView

        val username = shprefLoggedin.getString("username", null)
        val useremail = shprefLoggedin.getString("email", null)
        val status = shprefStatus.getBoolean("status", false)
        val followerCount = 22   //from api
        val followingCount = 39
        val headerView = navigationView.getHeaderView(0)
        val userProfilePicture = headerView.findViewById<ShapeableImageView>(R.id.imageUserppDrawer)
        val usernameDrawer = headerView.findViewById<TextView>(R.id.textUsernamedrawer)
        val useremailDrawer = headerView.findViewById<TextView>(R.id.textUseremaildrawer)
        val followercountDrawer = headerView.findViewById<Chip>(R.id.chipFollowersdrawer)
        val followingcountDrawer = headerView.findViewById<Chip>(R.id.chipFollowingsdrawer)
        usernameDrawer.text = username
        useremailDrawer.text = useremail
        followercountDrawer.text = "${numberFormatterSpaced(followerCount.toLong())} Followers"
        followingcountDrawer.text = "${numberFormatterSpaced(followingCount.toLong())} Followings"
        binding.textgreeting1.text = Html.fromHtml("Hello, <font color=\"#E9A6A6\">$username</font>!")

        Glide.with(binding.imageuserpphome)
            .load(getProfilePictureUri())
            .placeholder(R.drawable.usersample)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageuserpphome)

        Glide.with(userProfilePicture)
            .load(getProfilePictureUri())
            .placeholder(R.drawable.usersample)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(userProfilePicture)

        if (status) binding.imageUserstatusHome.setImageResource(R.drawable.circle_green)
        else binding.imageUserstatusHome.setImageResource(R.drawable.circle_red)

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
                    shprefLoggedin.edit().putBoolean("status", false).apply()
                    NancyToast.makeText(requireContext(), "You have been logged out!", NancyToast.LENGTH_SHORT, NancyToast.INFO, false).show()
                    navigateToOnBoardActivity()
                    true
                }
                else -> false
            }
        }
    }

    override fun observeChanges() {
        binding.morehome.setOnClickListener {
            binding.myDrawerLayout.openDrawer(Gravity.LEFT, true)
        }

        binding.imageuserpphome.setOnClickListener {
            showStatusDialog()
        }
    }

    private suspend fun checkIfUserExists(email: String): Boolean {
        return try {
            val documentSnapshot = firestore.collection("users").document(email).get().await()
            documentSnapshot.exists()
        } catch (e: Exception) {
            false
        }
    }

    private fun navigateToOnBoardActivity() {
        val intent = Intent(requireContext(), OnBoardingActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun startShimmers() {
        shimmerPopularMovies = binding.rvpopularmoviesShimmera
        shimmerPopularLists = binding.rvpopularlistsShimmer
        shimmerHomeReviews = binding.rvrecentreviewsShimmer
        shimmerPopularMovies.startShimmer()
        shimmerPopularMovies.visibility = View.VISIBLE
        shimmerPopularLists.startShimmer()
        shimmerPopularLists.visibility = View.VISIBLE
        shimmerHomeReviews.startShimmer()
        shimmerHomeReviews.visibility = View.VISIBLE
    }

    private fun stopShimmers() {
        shimmerPopularMovies.stopShimmer()
        shimmerPopularMovies.visibility = View.GONE
        shimmerPopularLists.stopShimmer()
        shimmerPopularLists.visibility = View.GONE
        shimmerHomeReviews.stopShimmer()
        shimmerHomeReviews.visibility = View.GONE
    }

    private fun showStatusDialog() {

        val customTitle = TextView(requireContext()).apply {
            text = "  Change your status"
            setPadding(40, 40, 40, 40) // Adjust padding as needed
            textSize = 24f
            setTextColor(Color.WHITE)
        }

        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setCustomTitle(customTitle)
            .setTitle("Change your status")   //.setTitle(resources.getString(R.string.title))
            .setMessage("If you set the status offline, you won't be able to see your friends status either!")
            .setNeutralButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                NancyToast.makeText(requireContext(), "Operation cancelled", NancyToast.LENGTH_SHORT, NancyToast.DEFAULT, false).show()
            }
            .setNegativeButton("Offline") { _, _ ->
                binding.imageUserstatusHome.setImageResource(R.drawable.circle_red)
                NancyToast.makeText(requireContext(), "Status Offline", NancyToast.LENGTH_SHORT, NancyToast.ERROR, false).show()
                shprefStatus.edit().putBoolean("status", false).apply()

            }
            .setPositiveButton("Online") { _, _ ->
                binding.imageUserstatusHome.setImageResource(R.drawable.circle_green)
                NancyToast.makeText(requireContext(), "Status Online", NancyToast.LENGTH_SHORT, NancyToast.SUCCESS, false).show()
                shprefStatus.edit().putBoolean("status", true).apply()
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
        val uriImage = shprefProfilePicture.getString("profile_image_uri", null)
        return if (uriImage != null) {
            Uri.parse(uriImage)
        } else {
            // Return a default drawable URI as fallback
            Uri.parse("android.resource://${requireActivity().packageName}/${R.drawable.usersample}")
        }
    }

}