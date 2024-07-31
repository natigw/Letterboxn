package com.example.letterboxn.presentation.ui.fragments.profile

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.letterboxn.data.remote.api.TmdbApi
import com.example.letterboxn.databinding.FragmentProfileBinding
import com.example.letterboxn.presentation.adapters.ProfileRatedAdapter
import com.example.letterboxn.presentation.ui.activities.OnBoardingActivity
import com.example.letterboxn.presentation.viewmodels.ProfileViewModel
import com.example.letterboxn.common.utils.NancyToast
import com.example.letterboxn.common.utils.numberFormatter
import com.example.letterboxn.common.utils.numberFormatterSpaced
import com.example.letterboxn.data.local.dao.ReviewDao
import com.example.letterboxn.data.local.model.ReviewEntity
import com.example.letterboxn.data.remote.model.account.favoriteMovies.favMovie.RequestAddRemoveFavorite
import com.example.letterboxn.domain.model.MovieItem
import com.example.letterboxn.domain.model.ReviewWithMovieItem
import com.example.letterboxn.presentation.adapters.ProfileFavMoviesAdapter
import com.example.letterboxn.presentation.adapters.ProfileReviewsAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    @Inject
    lateinit var api: TmdbApi

    @Inject
    lateinit var reviewDao: ReviewDao

    @Inject
    @Named("UserLoggedIn")
    lateinit var shprefLogon : SharedPreferences

    @Inject
    @Named("UserProfileImage")
    lateinit var shprefProfilePicture : SharedPreferences

    @Inject
    @Named("UserBackPosterImage")
    lateinit var shprefBackPoster : SharedPreferences

    @Inject
    @Named("UserBackPosterIsDefault")
    lateinit var shprefBackPosterIsDefault : SharedPreferences

    val viewmodel : ProfileViewModel by viewModels()

//    val favAdapter = ProfileFavMoviesAdapter(
//        bindinqa = binding,
//        onClick = {
//            findNavController().navigate(
//                ProfileFragmentDirections.actionProfileFragmentToDetailsMovieFragment(it.movieId)
//            )
//        }
//    )

    val recentRatedAdapter = ProfileRatedAdapter (
        bindinqa = binding,
        onClick = {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToDetailsMovieFragment(it)
            )
        }
    )

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            saveImageUri(uri)
            refreshProfilePicture()
            NancyToast.makeText(requireContext(), "Profile picture changed!", NancyToast.LENGTH_SHORT, NancyToast.SUCCESS, false).show()
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    private val cameraPermissionRequestCode = 100
    private val maxDeniedCount = 3

    private lateinit var takePictureLauncher: ActivityResultLauncher<Void?>

    private lateinit var favAdapter: ProfileFavMoviesAdapter

    val reviewsToDelete = mutableListOf<ReviewEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        takePictureLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
                bitmap?.let {
                    binding.imageUserppprofile.setImageBitmap(it)
                }
            }
    }

    override fun onViewCreatedLight() {

//        findNavController().currentBackStackEntry?.savedStateHandle
//            ?.getLiveData<Boolean>("changed")
//            ?.observe(viewLifecycleOwner) {
//                if (it) refreshProfileBackPoster()
//            }

//        val shp = requireContext().getSharedPreferences("account_id", Context.MODE_PRIVATE)
//        val accId = shp.getInt("id", 0)
//        val advApiKey = shp.getString("api+session", null)
//        NancyToast.makeText(requireContext(), "accountId is $accId", NancyToast.LENGTH_SHORT, NancyToast.INFO, false).show()
//        NancyToast.makeText(requireContext(), "api+sess is $advApiKey", NancyToast.LENGTH_SHORT, NancyToast.INFO, false).show()


        val shimmerFrameLayout = binding.rvUsersFavMoviesShimmer
        shimmerFrameLayout.startShimmer()
        shimmerFrameLayout.visibility = View.VISIBLE
        val shimmerWatched = binding.rvUsersRecWatchedShimmer
        shimmerWatched.startShimmer()
        shimmerWatched.visibility = View.VISIBLE
        val shimmerReviewed = binding.rvRecentReviewsProfileShimmer
        shimmerReviewed.stopShimmer()
        shimmerReviewed.visibility = View.GONE

        val username = shprefLogon.getString("username", null)
        binding.textUsernameprofile.text = username
        binding.textUsersFavFilms.text = "$username's Favorite Films"
        binding.textUsersrecentwatched.text = "$username's Recent Watched"
        binding.textUsersrecentreviewed.text = "$username's Recent Reviewed"
        val userFollowers = 22  //from api
        val userFollowings = 39
        binding.textCountnFollowersprofile.text = "${numberFormatterSpaced(userFollowers.toLong())} Followers"
        binding.textCountnFollowingsprofile.text = "${numberFormatterSpaced(userFollowings.toLong())} Followings"
        val lists = 4
        binding.textListcountProfile.text = numberFormatter(lists.toLong())


        lifecycleScope.launch {
            val response = api.getFavoriteMovies()
            binding.textFavcountProfile.text = numberFormatter(response.totalResults.toLong())
            favAdapter = ProfileFavMoviesAdapter(
                bindinqa = binding,
                movies = response.results.map {
                    MovieItem(
                        moviePoster = it.posterPath,
                        movieId = it.id,
                        movieTitle = it.title,
                        movieDescription = it.overview
                    )
                }.toMutableList(),
                onClick = {
                    findNavController().navigate(
                        ProfileFragmentDirections.actionProfileFragmentToDetailsMovieFragment(it.movieId)
                    )
                },
                onLongClick = { position, movieId ->
                    showDeleteConfirmationDialog(position, movieId)
                }
            )
            binding.rvUsersFavMovies.adapter = favAdapter
        }

//        lifecycleScope.launch {
//            val responseRecWatched = api.getRatedMoviesAccount()
//            binding.textRatedcountProfile.text = numberFormatter(responseRecWatched.totalResults.toLong())
//            binding.rvUsersRecWatched.adapter = ProfileRatedAdapter(
//                bindinqa = binding,
//                results = responseRecWatched.results,
//                onClick = {
//                    findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToDetailsMovieFragment(movieId = it))
//                }
//            )
//            shimmerWatched.stopShimmer()
//            shimmerWatched.visibility = View.GONE
//        }


        val deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.trash_bin)!!
        val background = ColorDrawable(Color.RED)

        lifecycleScope.launch {
            try {
                //reviewDao.getAllReviewsDynamically().collectLatest {}
                val reviews = reviewDao.getAllReviews().reversed()
                val reviewsWithMovieDetails = reviews.map { review ->
                    val movieDetails = api.getMovieDetails(movieId = review.movieId)
                    ReviewWithMovieItem(
                        authorName = username ?: "salam",
                        authorImage = shprefProfilePicture.getString("profile_image_uri", null)
                            ?: "android.resource://${requireActivity().packageName}/${R.drawable.usersample}",
                        review = review.review,
                        reviewRating = review.rating,
                        commentCount = 0,
                        movieId = movieDetails.id,
                        movieTitle = movieDetails.title,
                        moviePoster = movieDetails.posterPath,
                        movieRating = movieDetails.voteAverage.toFloat()/2,
                        movieReleaseDate = movieDetails.releaseDate
                    )
                }
                binding.textReviewcountProfile.text = numberFormatter(reviews.size.toLong())
                val reviewAdapter = ProfileReviewsAdapter(
                    bindinqa = binding,
                    reviews = reviewsWithMovieDetails.toMutableList(),
                    onClick = {
                        findNavController().navigate(
                            ProfileFragmentDirections.actionProfileFragmentToRecentReviewDetailsBottomSheetFragment(it)
                        )
                    }
                )
                binding.rvUsersRecReviewed.adapter = reviewAdapter

                val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val position = viewHolder.adapterPosition
                        val deletedReview = reviewAdapter.deleteItem(position)
                        binding.textReviewcountProfile.text = binding.textReviewcountProfile.text.toString().toInt().minus(1).toString()
                        runBlocking {
                            reviewsToDelete.add(ReviewEntity(
                                movieId = deletedReview.movieId,
                                review = deletedReview.review,
                                rating = deletedReview.reviewRating,
                                reviewDate = deletedReview.movieReleaseDate,
                                reviewId = reviewDao.getReviewId(deletedReview.movieId, review = deletedReview.review)!!))
                        }
                        Snackbar.make(binding.rvUsersRecReviewed, "Review deleted", Snackbar.LENGTH_LONG).apply {
                            setAction("UNDO") {
                                reviewAdapter.restoreItem(deletedReview, position)
                                binding.rvUsersRecReviewed.scrollToPosition(position)
                                reviewsToDelete.removeLast()
                                binding.textReviewcountProfile.text = binding.textReviewcountProfile.text.toString().toInt().plus(1).toString()
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
                itemTouchHelper.attachToRecyclerView(binding.rvUsersRecReviewed)

                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
            } catch (e: Exception) {
                Log.e("dao", e.toString())
            }
        }

        refreshProfilePicture()
        refreshProfileBackPoster()

//        setRv()
//        observe()
    }

    override fun onPause() {
        super.onPause()
        reviewsToDelete.forEach {
            lifecycleScope.launch {
                reviewDao.deleteReview(it)
            }
        }
    }

    private fun showDeleteConfirmationDialog(position: Int, movieId : Int) {
        val deleteText = SpannableString("Delete").apply {
            setSpan(ForegroundColorSpan(Color.RED), 0, length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        MaterialAlertDialogBuilder(requireContext()).apply {
            setMessage("Do you want to remove this movie?")
            setPositiveButton(deleteText) { _, _ ->
                favAdapter.removeItem(position)
                binding.textFavcountProfile.text = binding.textFavcountProfile.text.toString().toInt().minus(1).toString()
                lifecycleScope.launch {
                    api.addOrRemoveFavoriteMovie(requestFavorite = RequestAddRemoveFavorite(
                        favorite = false,
                        mediaId = movieId,
                        mediaType = "movie"
                    ))
                }
            }
            setNegativeButton("Cancel", null)
        }.show()
    }

    override fun observeChanges() {

        binding.pickMeButton.setOnClickListener {
            showChangeProfileDialog()
        }
        binding.buttonChangeBackground.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToFavMoviesBottomSheetFragment())
        }

        binding.buttonSeeallRecents.setOnClickListener {
            NancyToast.makeText(requireContext(), "[navigating all rated movies page]", NancyToast.LENGTH_SHORT, NancyToast.INFO, false).show()
        }
        binding.buttonSeeallReviews.setOnClickListener {
            NancyToast.makeText(requireContext(), "[navigating all reviews page]", NancyToast.LENGTH_SHORT, NancyToast.INFO, false).show()
        }
        binding.buttonlogout.setOnClickListener {
            shprefLogon.edit().putBoolean("status", false).apply()
            NancyToast.makeText(requireContext(), "You have been logged out!", NancyToast.LENGTH_SHORT, NancyToast.INFO, false).show()
            navigateToOnBoardActivity()
        }

    }

    private fun navigateToOnBoardActivity() {
        val intent = Intent(requireContext(), OnBoardingActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    fun observe() {
        lifecycleScope.launch {
            viewmodel.movies
                .collect {
                    recentRatedAdapter.updateAdapter(it)
                }
        }
    }

    fun setRv() {
        binding.rvUsersRecentRated.adapter = recentRatedAdapter
    }

    private fun showChangeProfileDialog() {

        val currentUri = getProfilePictureUri()
        // Check if the current picture is the default one
        val isDefaultPicture = currentUri.toString() == "android.resource://${requireActivity().packageName}/${R.drawable.usersample}"


        val dialogTitle = if (isDefaultPicture) "  Set a profile picture" else "  Change or Delete picture"
        val dialogMessage = if (isDefaultPicture) "Choose a new profile picture from the gallery." else "You can choose a new profile picture from the gallery, or delete to set default!"

        val customTitle = TextView(requireContext()).apply {
            text = dialogTitle
            setPadding(40, 40, 40, 40) // Adjust padding as needed
            textSize = 24f
            setTextColor(Color.WHITE)
        }

        val dialogBuilder = MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setCustomTitle(customTitle)
            .setMessage(dialogMessage)
            .setNeutralButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                NancyToast.makeText(requireContext(), "Operation cancelled", NancyToast.LENGTH_SHORT, NancyToast.DEFAULT, false).show()
            }
            .setPositiveButton("Change") { _, _ ->
                takePhotoOrChooseGalleryDialog()
            }

        if (!isDefaultPicture) {
            dialogBuilder.setNegativeButton("Delete") { _, _ ->
                // Set default image and clear the URI in SharedPreferences
                val defaultUri = Uri.parse("android.resource://${requireActivity().packageName}/${R.drawable.usersample}")
                Glide.with(binding.imageUserppprofile)
                    .load(defaultUri)
                    .placeholder(R.drawable.usersample)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.imageUserppprofile)

                // Clear the profile image URI in SharedPreferences
                shprefProfilePicture.edit().remove("profile_image_uri").apply()

                NancyToast.makeText(requireContext(), "Profile picture deleted!", NancyToast.LENGTH_SHORT, NancyToast.ERROR, false).show()
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
            text = "  Take or Choose a picture"
            setPadding(40, 40, 40, 40) // Adjust padding as needed
            textSize = 24f
            setTextColor(Color.WHITE)
        }

        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setCustomTitle(customTitle)
            .setMessage("You can take a photo right now, or choose one from your gallery.")
            .setPositiveButton("Choose") { _, _ ->
                try {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                } catch (e: Exception) {
                    Log.e("PhotoPicker", "PHOTOPICKER FAILED!")
                    NancyToast.makeText(requireContext(), "Something went wrong!", NancyToast.LENGTH_SHORT, NancyToast.ERROR, false).show()
                }
            }.setNegativeButton("Take a picture") { _, _ ->
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
        shprefProfilePicture.edit()
            .putString("profile_image_uri", uri.toString())
            .apply()
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

    private fun refreshProfilePicture() {
        val uri = getProfilePictureUri()
        Glide.with(binding.imageUserppprofile)
            .load(uri)
            .placeholder(R.drawable.usersample)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageUserppprofile)
    }

    private fun refreshProfileBackPoster() {
        val isPosterDefault = shprefBackPosterIsDefault.getBoolean("isDefault", false)
        val backPoster = shprefBackPoster.getString("poster", "android.resource://${requireActivity().packageName}/${R.drawable.profile_default_back3}")

        if (isPosterDefault ) {
            Glide.with(binding.imageBackProfile)
                .load(backPoster?.toInt())
                .placeholder(R.drawable.custom_poster_shape)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageBackProfile)
        }
        else {
            Glide.with(binding.imageBackProfile)
                .load(backPoster)
                .placeholder(R.drawable.custom_poster_shape)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageBackProfile)
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
            .setTitle("Permission Required")
            .setMessage("You have denied camera permission several times. Please allow permission in the app settings to use this feature.")
            .setPositiveButton("Go to Settings") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        startActivity(intent)
    }

}