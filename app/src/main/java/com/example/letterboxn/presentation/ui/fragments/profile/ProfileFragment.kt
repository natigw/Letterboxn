package com.example.letterboxn.presentation.ui.fragments.profile

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.common.utils.nancyToast
import com.example.letterboxn.common.utils.nancyToastError
import com.example.letterboxn.common.utils.nancyToastInfo
import com.example.letterboxn.common.utils.nancyToastSuccess
import com.example.letterboxn.common.utils.numberFormatter
import com.example.letterboxn.common.utils.numberFormatterSpaced
import com.example.letterboxn.common.utils.startShimmer
import com.example.letterboxn.common.utils.stopShimmer
import com.example.letterboxn.data.local.database.review.ReviewEntity
import com.example.letterboxn.data.remote.model.account.favoriteMovies.favMovie.RequestAddRemoveFavorite
import com.example.letterboxn.databinding.FragmentProfileBinding
import com.example.letterboxn.domain.model.MovieItem
import com.example.letterboxn.domain.model.RatedMovieItem
import com.example.letterboxn.domain.model.home.recentReviews.ReviewWithMovieItem
import com.example.letterboxn.presentation.adapters.ProfileFavMoviesAdapter
import com.example.letterboxn.presentation.adapters.ProfileRatedAdapter
import com.example.letterboxn.presentation.adapters.ProfileReviewsAdapter
import com.example.letterboxn.presentation.ui.activities.OnBoardingActivity
import com.example.letterboxn.presentation.viewmodels.ProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewmodel by viewModels<ProfileViewModel>()

    private val favAdapter = ProfileFavMoviesAdapter(
        onClick = {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToDetailsMovieFragment(it.movieId)
            )
        },
        onLongClick = { position, movieId ->
            showDeleteConfirmationDialog(position, movieId)
        }
    )
    private val recentRatedAdapter = ProfileRatedAdapter (
        onClick = {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToDetailsMovieFragment(it)
            )
        }
    )
    private val reviewAdapter = ProfileReviewsAdapter(
        onClick = {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToRecentReviewDetailsBottomSheetFragment(it)
            )
        }
    )

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            saveImageUri(uri)
            refreshProfilePicture()
            nancyToastSuccess(requireContext(), getString(R.string.profile_picture_changed))
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    private val cameraPermissionRequestCode = 100
    private val maxDeniedCount = 3

    private lateinit var takePictureLauncher: ActivityResultLauncher<Void?>

    private lateinit var username: String

    val reviewsToDelete = mutableListOf<ReviewEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        takePictureLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
                bitmap?.let {
                    binding.imageUserPictureProfile.setImageBitmap(it)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        startShimmer(binding.shimmerFavMoviesProfile)
        startShimmer(binding.shimmerRecentWatchedProfile)
        startShimmer(binding.shimmerRecentReviewsProfile)
    }

    override fun onViewCreatedLight() {
        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<Boolean>("changed")
            ?.observe(viewLifecycleOwner) {
                if (it == true) refreshProfileBackPoster()
            }

//        val shp = requireContext().getSharedPreferences("account_id", Context.MODE_PRIVATE)
//        val accId = shp.getInt("id", 0)
//        val advApiKey = shp.getString("api+session", null)
//        nancyToastInfo(requireContext(), "accountId is $accId")
//        nancyToastInfo(requireContext(), "api+sess is $advApiKey")

        username = viewmodel.sharedPrefLogon.getString("username", null) ?: getString(R.string.user)
        binding.textUsernameProfile.text = username
        binding.textUsersFavMoviesTEXT.text = buildString {
        append(username)
        append(" ")
        append(getString(R.string.somebody_s_fav_films))
    }
        binding.textUsersRecentWatchedProfileTEXT.text = "$username's Recent Watched"
        binding.textUsersRecentReviewedProfileTEXT.text = "$username's Recent Reviewed"
        val userFollowers = 22  //from api
        val userFollowings = 39 //from api
        binding.textFollowersCountProfile.text = "${numberFormatterSpaced(userFollowers.toLong())} Followers"
        binding.textFollowingsCountProfile.text = "${numberFormatterSpaced(userFollowings.toLong())} Followings"
        val lists = 4  //from api
        binding.textListsCountProfile.text = numberFormatter(lists.toLong())


//        val deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.trash_bin)!!
//        val background = ColorDrawable(Color.RED)
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            try {
//                val reviews = reviewDao.getAllReviews().reversed()
//                binding.textReviewcountProfile.text = numberFormatter(reviews.size.toLong())
//                val reviewsWithMovieDetails = reviews.map { review ->
//                    val movieDetails = api.getMovieDetails(movieId = review.movieId)
//                    ReviewWithMovieItem(
//                        authorName = username ?: "salam",
//                        authorImage = shprefProfilePicture.getString("profile_image_uri", null)
//                            ?: "android.resource://${requireActivity().packageName}/${R.drawable.usersample}",
//                        review = review.review,
//                        reviewRating = review.rating,
//                        commentCount = 0,
//                        movieId = movieDetails.id,
//                        movieTitle = movieDetails.title,
//                        moviePoster = movieDetails.posterPath,
//                        movieRating = movieDetails.voteAverage.toFloat()/2,
//                        movieReleaseDate = movieDetails.releaseDate
//                    )
//                }.toMutableList()
//                binding.textNoReviewedProfile.visibility = if (reviews.isEmpty()) View.VISIBLE else View.GONE
//                reviewAdapter.updateAdapter(reviewsWithMovieDetails)
//
//                val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
//                    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
//                        return false
//                    }
//
//                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                        val position = viewHolder.adapterPosition
//                        val deletedReview = reviewAdapter.deleteItem(position)
//                        binding.textReviewcountProfile.text = binding.textReviewcountProfile.text.toString().toInt().minus(1).toString()
//                        runBlocking {
//                            reviewsToDelete.add(ReviewEntity(
//                                movieId = deletedReview.movieId,
//                                review = deletedReview.review,
//                                rating = deletedReview.reviewRating,
//                                reviewDate = deletedReview.movieReleaseDate,
//                                reviewId = reviewDao.getReviewId(deletedReview.movieId, review = deletedReview.review)!!))
//                        }
//                        Snackbar.make(binding.rvUsersRecReviewed, "Review deleted", Snackbar.LENGTH_LONG).apply {
//                            setAction("UNDO") {
//                                reviewAdapter.restoreItem(deletedReview, position)
//                                binding.rvUsersRecReviewed.scrollToPosition(position)
//                                reviewsToDelete.removeLast()
//                                binding.textReviewcountProfile.text = binding.textReviewcountProfile.text.toString().toInt().plus(1).toString()
//                            }.show()
//                        }
//                    }
//
//                    override fun onChildDraw(
//                        c: Canvas,
//                        recyclerView: RecyclerView,
//                        viewHolder: RecyclerView.ViewHolder,
//                        dX: Float,
//                        dY: Float,
//                        actionState: Int,
//                        isCurrentlyActive: Boolean
//                    ) {
//                        val itemView = viewHolder.itemView
//                        val backgroundCornerOffset = 20 // Offset for the background
//
//                        val iconSize = deleteIcon.intrinsicHeight / 4 // Adjust icon size here
//                        val iconMargin = (itemView.height - iconSize) / 2
//                        val iconTop = itemView.top + (itemView.height - iconSize) / 2
//                        val iconBottom = iconTop + iconSize
//
//                        when {
//                            dX > 0 -> { // Swiping to the right
//                                val iconLeft = itemView.left + iconMargin
//                                val iconRight = iconLeft + iconSize
//                                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
//
//                                background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom)
//                            }
//                            dX < 0 -> { // Swiping to the left
//                                val iconRight = itemView.right - iconMargin
//                                val iconLeft = iconRight - iconSize
//                                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
//
//                                background.setBounds(itemView.right + dX.toInt() - backgroundCornerOffset, itemView.top, itemView.right, itemView.bottom)
//                            }
//                            else -> { // View is unSwiped
//                                background.setBounds(0, 0, 0, 0)
//                            }
//                        }
//
//                        background.draw(c)
//                        deleteIcon.draw(c)
//
//                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//                    }
//                }
//
//                val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
//                itemTouchHelper.attachToRecyclerView(binding.rvUsersRecReviewed)
//
//            } catch (e: Exception) {
//                Log.e("dao", e.toString())
//            }
//        }

        refreshProfilePicture()
        refreshProfileBackPoster()

        setAdapters()
        updateAdapters()
    }

    override fun onPause() {
        super.onPause()
        reviewsToDelete.forEach {
            viewLifecycleOwner.lifecycleScope.launch {
                viewmodel.reviewDao.deleteReview(it)
            }
        }
    }

    override fun clickListeners() {
        super.clickListeners()

        binding.buttonChangePictureProfile.setOnClickListener {
            showChangeProfileDialog()
        }
        binding.buttonChangeBackgroundProfile.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToFavMoviesBottomSheetFragment())
        }
        binding.buttonSeeAllRecentWatchedProfile.setOnClickListener {
            nancyToastInfo(requireContext(), getString(R.string.navigating_rated_movies_screen))
        }
        binding.buttonSeeAllReviewsProfile.setOnClickListener {
            nancyToastInfo(requireContext(), getString(R.string.navigating_reviews_screen))
        }
        binding.buttonLogoutProfile.setOnClickListener {
            viewmodel.sharedPrefLogon.edit().putBoolean("status", false).apply()
            nancyToastInfo(requireContext(), getString(R.string.logout_info_message))
            navigateToOnBoardActivity()
        }
    }

    private fun updateAdapters() {
        viewLifecycleOwner.lifecycleScope.launch {
            val it = viewmodel.api.getFavoriteMovies().results.map {
                MovieItem(
                    movieId = it.id,
                    movieTitle = it.title,
                    moviePoster = it.posterPath,
                    movieDescription = it.overview
                )
            }.toMutableList()
            binding.textFavMoviesCountProfile.text = numberFormatter(it.size.toLong())
            binding.textNoFavMoviesProfile.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            favAdapter.updateAdapter(it)
            stopShimmer(binding.shimmerFavMoviesProfile)
//            viewmodel.favMovies.collectLatest {
//                binding.textFavcountProfile.text = numberFormatter(it.size.toLong())
//                binding.textNoFavMoviesProfile.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
//                favAdapter.updateAdapter(it)
//                shimmerFav.stopShimmer()
//                shimmerFav.visibility = View.GONE
//            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            val it = viewmodel.api.getRatedMoviesAccount().results.map {
                RatedMovieItem(
                    movieId = it.id,
                    moviePoster = it.posterPath,
                    rating = it.rating
                )
            }
            binding.textNoRecentWatchedProfile.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE
            binding.textRatedMoviesCountProfile.text = numberFormatter(it.size.toLong())
            recentRatedAdapter.updateAdapter(it)
            stopShimmer(binding.shimmerRecentWatchedProfile)
//            viewmodel.movies.collectLatest {
//                    binding.textNoRecentWatchedProfile.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE
//                    binding.textRatedcountProfile.text = numberFormatter(it.size.toLong())
//                    recentRatedAdapter.updateAdapter(it)
//                    shimmerWatched.stopShimmer()
//                    shimmerWatched.visibility = View.GONE
//            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            val reviews = viewmodel.reviewDao.getAllReviews()
            val reviewsWithMovieDetails = reviews.map { review ->
                val movieDetails = viewmodel.api.getMovieDetails(movieId = review.movieId) //viewmodel
                ReviewWithMovieItem(
                    authorName = username,
                    authorImage = viewmodel.sharedPrefProfilePicture.getString("profile_image_uri", null)
                        ?: "android.resource://${requireActivity().packageName}/${R.drawable.placeholder_user}",
                    review = review.review,
                    reviewRating = review.rating.toDouble(),
                    commentCount = 0,
                    movieId = movieDetails.id,
                    movieTitle = movieDetails.title,
                    moviePoster = movieDetails.posterPath,
                    movieRating = movieDetails.voteAverage / 2,
                    movieReleaseDate = movieDetails.releaseDate
                )
            }.reversed().toMutableList()
            binding.textReviewsCountProfile.text = numberFormatter(reviews.size.toLong())
            binding.textNoReviewedProfile.visibility = if (reviews.isEmpty()) View.VISIBLE else View.GONE
            reviewAdapter.updateAdapter(reviewsWithMovieDetails)
            stopShimmer(binding.shimmerRecentReviewsProfile)
            swipeToDelete()

//            viewmodel.reviews.collectLatest {
//                val reviewsWithMovieDetails = it.map { review ->
//                    val movieDetails = api.getMovieDetails(movieId = review.movieId) //viewmodel
//                    ReviewWithMovieItem(
//                        authorName = username,
//                        authorImage = shprefProfilePicture.getString("profile_image_uri", null)
//                            ?: "android.resource://${requireActivity().packageName}/${R.drawable.usersample}",
//                        review = review.review,
//                        reviewRating = review.rating,
//                        commentCount = 0,
//                        movieId = movieDetails.id,
//                        movieTitle = movieDetails.title,
//                        moviePoster = movieDetails.posterPath,
//                        movieRating = movieDetails.voteAverage.toFloat() / 2,
//                        movieReleaseDate = movieDetails.releaseDate
//                    )
//                }.toMutableList()
//                binding.textReviewcountProfile.text = numberFormatter(it.size.toLong())
//                binding.textNoReviewedProfile.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
//                reviewAdapter.updateAdapter(reviewsWithMovieDetails)
//                shimmerReviewed.stopShimmer()
//                shimmerReviewed.visibility = View.GONE
//                swipeToDelete()
//            }
        }
    }

    private fun setAdapters() {
        binding.rvUsersFavMoviesProfile.adapter = favAdapter
        binding.rvUsersRecentWatchedProfile.adapter = recentRatedAdapter
        binding.rvUsersRecReviewedProfile.adapter = reviewAdapter
    }

    private fun swipeToDelete() {
        val deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.trash_bin)!!
        val background = ColorDrawable(Color.RED)

        try {
            val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val deletedReview = reviewAdapter.deleteItem(position)
                    binding.textReviewsCountProfile.text = binding.textReviewsCountProfile.text.toString().toInt().minus(1).toString()
                    runBlocking {
                        reviewsToDelete.add(
                            ReviewEntity(
                            movieId = deletedReview.movieId,
                            review = deletedReview.review ?: "Just perfect!",
                            rating = deletedReview.reviewRating,
                            reviewDate = deletedReview.movieReleaseDate,
                            reviewId = viewmodel.reviewDao.getReviewId(deletedReview.movieId, review = deletedReview.review ?: "Just perfect!")!!)
                        )
                    }
                    Snackbar.make(binding.rvUsersRecReviewedProfile, getString(R.string.review_deleted), Snackbar.LENGTH_LONG).apply {
                        setAction(getString(R.string.undo)) {
                            reviewAdapter.restoreItem(deletedReview, position)
                            binding.rvUsersRecReviewedProfile.scrollToPosition(position)
                            reviewsToDelete.removeLast()
                            binding.textReviewsCountProfile.text = binding.textReviewsCountProfile.text.toString().toInt().plus(1).toString()
                        }.show()
                    }
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    val itemView = viewHolder.itemView
                    val backgroundCornerOffset = 20 // Offset for the background

                    val iconSize = deleteIcon.intrinsicHeight / 4 // Adjust icon size here
                    val iconMargin = (itemView.height - iconSize) / 2
                    val iconTop = itemView.top + (itemView.height - iconSize) / 2
                    val iconBottom = iconTop + iconSize

                    when {
                        dX > 0 -> { // Swiping to the right
                            val iconLeft = itemView.left + iconMargin
                            val iconRight = iconLeft + iconSize
                            deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                            background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom)
                        }
                        dX < 0 -> { // Swiping to the left
                            val iconRight = itemView.right - iconMargin
                            val iconLeft = iconRight - iconSize
                            deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                            background.setBounds(itemView.right + dX.toInt() - backgroundCornerOffset, itemView.top, itemView.right, itemView.bottom)
                        }
                        else -> { // View is unSwiped
                            background.setBounds(0, 0, 0, 0)
                        }
                    }

                    background.draw(c)
                    deleteIcon.draw(c)

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }

            val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
            itemTouchHelper.attachToRecyclerView(binding.rvUsersRecReviewedProfile)

        } catch (e: Exception) {
            Log.e("dao", e.toString())
        }
    }

    private fun navigateToOnBoardActivity() {
        val intent = Intent(requireContext(), OnBoardingActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun showDeleteConfirmationDialog(position: Int, movieId : Int) {
        val deleteText = SpannableString(getString(R.string.delete)).apply {
            setSpan(ForegroundColorSpan(Color.RED), 0, length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        MaterialAlertDialogBuilder(requireContext()).apply {
            setMessage(getString(R.string.wanna_delete_this_movie))
            setPositiveButton(deleteText) { _, _ ->
                favAdapter.removeItem(position)
                binding.textFavMoviesCountProfile.text = binding.textFavMoviesCountProfile.text.toString().toInt().minus(1).toString()
                viewLifecycleOwner.lifecycleScope.launch {
                    viewmodel.api.addOrRemoveFavoriteMovie(requestFavorite = RequestAddRemoveFavorite(
                        favorite = false,
                        mediaId = movieId,
                        mediaType = "movie"
                    ))
                }
            }
            setNegativeButton(getString(R.string.cancel), null)
        }.show()
    }

    private fun showChangeProfileDialog() {

        val currentUri = getProfilePictureUri()
        // Check if the current picture is the default one
        val isDefaultPicture = currentUri.toString() == "android.resource://${requireActivity().packageName}/${R.drawable.placeholder_user}"


        val dialogTitle = if (isDefaultPicture) getString(R.string.set_a_profile_picture) else getString(R.string.change_or_delete_picture)
        val dialogMessage = if (isDefaultPicture) getString(R.string.new_picture_from_gallery) else getString(R.string.new_profile_picture_options)

        val customTitle = TextView(requireContext()).apply {
            text = dialogTitle
            setPadding(40, 40, 40, 40)
            textSize = 24f
            setTextColor(Color.WHITE)
        }

        val dialogBuilder = MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setCustomTitle(customTitle)
            .setMessage(dialogMessage)
            .setNeutralButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
                nancyToast(requireContext(), getString(R.string.operation_cancelled))
            }
            .setPositiveButton(getString(R.string.change)) { _, _ ->
                takePhotoOrChooseGalleryDialog()
            }

        if (!isDefaultPicture) {
            dialogBuilder.setNegativeButton(getString(R.string.cancel)) { _, _ ->
                val defaultUri = Uri.parse("android.resource://${requireActivity().packageName}/${R.drawable.placeholder_user}")
                Glide.with(binding.imageUserPictureProfile)
                    .load(defaultUri)
                    .placeholder(R.drawable.placeholder_user)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.imageUserPictureProfile)
                // Clear the profile image URI in SharedPreferences
                viewmodel.sharedPrefProfilePicture.edit().remove("profile_image_uri").apply()

                nancyToastInfo(requireContext(), getString(R.string.profile_picture_deleted))
            }
        }

        val dialog = dialogBuilder.create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(Color.WHITE)
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

    private fun takePhotoOrChooseGalleryDialog() {

        val customTitle = TextView(requireContext()).apply {
            text = getString(R.string.take_or_choose_picture)
            setPadding(40, 40, 40, 40)
            textSize = 24f
            setTextColor(Color.WHITE)
        }

        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setCustomTitle(customTitle)
            .setMessage(getString(R.string.take_or_choose_explanation))
            .setPositiveButton(getString(R.string.choose)) { _, _ ->
                try {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                } catch (e: Exception) {
                    Log.e("PhotoPicker", "PHOTOPICKER FAILED!")
                    nancyToastError(requireContext(), getString(R.string.something_went_wrong))
                }
            }.setNegativeButton(getString(R.string.take_picture)) { _, _ ->
                handleCameraButtonClick()
            }.create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(Color.WHITE)
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


    private fun saveImageUri(uri: Uri) {
        viewmodel.sharedPrefProfilePicture.edit()
            .putString("profile_image_uri", uri.toString())
            .apply()
    }

    private fun getProfilePictureUri(): Uri {
        val uriImage = viewmodel.sharedPrefProfilePicture.getString("profile_image_uri", null)
        return if (uriImage != null) {
            Uri.parse(uriImage)
        } else {
            // Return a default drawable URI as fallback
            Uri.parse("android.resource://${requireActivity().packageName}/${R.drawable.placeholder_user}")
        }
    }

    private fun refreshProfilePicture() {
        val uri = getProfilePictureUri()
        Glide.with(binding.imageUserPictureProfile)
            .load(uri)
            .placeholder(R.drawable.placeholder_user)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageUserPictureProfile)
    }

    private fun refreshProfileBackPoster() {
        val isPosterDefault = viewmodel.sharedPrefBackPosterIsDefault.getBoolean("isDefault", false)
        val backPoster = viewmodel.sharedPrefBackPoster.getString("poster", "android.resource://${requireActivity().packageName}/${R.drawable.profile_default_back3}")

        if (isPosterDefault ) {
            Glide.with(binding.imageProfileBackProfile)
                .load(backPoster?.toInt())
                .placeholder(R.drawable.custom_poster_shape)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageProfileBackProfile)
        }
        else {
            Glide.with(binding.imageProfileBackProfile)
                .load(backPoster)
                .placeholder(R.drawable.custom_poster_shape)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageProfileBackProfile)
        }
    }


    private fun handleCameraButtonClick() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            launchCamera()
        } else {
            requestCameraPermission()
        }
    }

    private fun launchCamera() {
        takePictureLauncher.launch(null)
    }

    private fun requestCameraPermission() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), cameraPermissionRequestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCamera()
                resetDeniedCount()
            } else {
                incrementDeniedCount()
                if (getDeniedCount() >= maxDeniedCount) {
                    showPermissionSettingsDialog()
                }
            }
        }
    }

    private fun incrementDeniedCount() {
        val prefs = requireContext().getSharedPreferences("AppPrefs", 0)
        val deniedCount = prefs.getInt("denied_count", 0) + 1
        prefs.edit().putInt("denied_count", deniedCount).apply()
    }

    private fun getDeniedCount(): Int {
        val prefs = requireContext().getSharedPreferences("AppPrefs", 0)
        return prefs.getInt("denied_count", 0)
    }

    private fun resetDeniedCount() {
        requireContext().getSharedPreferences("AppPrefs", 0).edit().putInt("denied_count", 0)
            .apply()
    }

    private fun showPermissionSettingsDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.permission_required))
            .setMessage(getString(R.string.permission_required_explanation))
            .setPositiveButton(getString(R.string.goto_settings)) { _, _ ->
                openAppSettings()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        stopShimmer(binding.shimmerFavMoviesProfile)
        stopShimmer(binding.shimmerRecentWatchedProfile)
        stopShimmer(binding.shimmerRecentReviewsProfile)
    }
}