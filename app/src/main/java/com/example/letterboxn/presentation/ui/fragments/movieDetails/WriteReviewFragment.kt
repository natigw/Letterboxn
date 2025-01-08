package com.example.letterboxn.presentation.ui.fragments.movieDetails

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.common.utils.nancyToastSuccess
import com.example.common.utils.nancyToastWarning
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.data.local.database.review.ReviewDao
import com.example.letterboxn.data.local.database.review.ReviewEntity
import com.example.letterboxn.data.remote.api.MovieApi
import com.example.letterboxn.data.remote.model.account.favoriteMovies.ResultFavoriteMovie
import com.example.letterboxn.data.remote.model.account.favoriteMovies.favMovie.RequestAddRemoveFavorite
import com.example.letterboxn.data.remote.model.account.ratedMovies.rateMovie.RequestAddRating
import com.example.letterboxn.databinding.FragmentWriteReviewBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class WriteReviewFragment :
    BaseFragment<FragmentWriteReviewBinding>(FragmentWriteReviewBinding::inflate) {

    @Inject
    lateinit var api: MovieApi

    @Inject
    lateinit var reviewDao: ReviewDao

    private val args: WriteReviewFragmentArgs by navArgs()

    private var isFav = false

    override fun onViewCreatedLight() {

        lifecycleScope.launch {
            try {
                if (findMovieById(args.movieId) != null) {  //found
                    isFav = true
                    binding.imageButtonFavReview.setImageResource(R.drawable.round_favorite_unselected_24)
                }
            } catch (e: Exception) {
                e.printStackTrace() // Handle exceptions (network errors, etc)
            }
        }

        lifecycleScope.launch {
            val movieItem = api.getMovieDetails(movieId = args.movieId)
            with(binding) {
                Glide.with(imagePosterReview)
                    .load("https://image.tmdb.org/t/p/w780" + movieItem.posterPath)
                    .placeholder(R.drawable.custom_poster_shape)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imagePosterReview)
                textMovienameReview.text = movieItem.title
                textMoviedateReview.text = movieItem.releaseDate.substring(0, 4)
                textMovienameReview.maxWidth = textView17.width
            }
        }

        setTodayDate()

    }

    override fun observeChanges() {
        binding.buttonDatePicker.setOnClickListener {
            showDatePicker()
        }

        binding.floatingActionButtonReviewback.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imageButtonFavReview.setOnClickListener {
            if (isFav) {
                binding.imageButtonFavReview.setImageResource(R.drawable.round_favorite_24)
                isFav = false
            } else {
                binding.imageButtonFavReview.setImageResource(R.drawable.round_favorite_unselected_24)
                isFav = true
            }
        }

        binding.buttonPublishReview.setOnClickListener {
            if (binding.ratingBarDetailsReview.rating == 0f) {
                nancyToastWarning(requireContext(), getString(R.string.please_rate_movie))
                return@setOnClickListener
            }
            if (binding.editTextReview.text.isNullOrEmpty()) {
                nancyToastWarning(requireContext(), getString(R.string.review_cannot_be_empty))
                return@setOnClickListener
            }
            lifecycleScope.launch {
                api.addOrRemoveFavoriteMovie(
                    requestFavorite = RequestAddRemoveFavorite(
                        favorite = isFav,  //if true add, else remove fav
                        mediaId = args.movieId,
                        mediaType = "movie"
                    )
                )
            }
            lifecycleScope.launch {
                api.addRateMovie(
                    movieId = args.movieId,
                    requestRateMovie = RequestAddRating(
                        value = binding.ratingBarDetailsReview.rating
                    )
                )
            }
            lifecycleScope.launch {
                reviewDao.addReview(
                    ReviewEntity(
                        movieId = args.movieId,
                        review = binding.editTextReview.text.toString(),
                        rating = binding.ratingBarDetailsReview.rating.toDouble(),
                        reviewDate = binding.textMoviedateReview.text.toString()
                    )
                )
            }
            binding.ratingBarDetailsReview.rating = 0f
            binding.editTextReview.text = null
            nancyToastSuccess(requireContext(), getString(R.string.review_published))
            findNavController().popBackStack()
        }
    }

    private suspend fun findMovieById(movieId: Int): ResultFavoriteMovie? {
        val response = api.getFavoriteMovies()
        return response.results.find { it.id == movieId }
    }

    private fun setTodayDate() {
        val todayDate = Date()
        val dateFormatted = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(todayDate)
        binding.chipDatePicker.text = dateFormatted
    }

    private fun showDatePicker() {

        val today = MaterialDatePicker.todayInUtcMilliseconds()
        // Create constraints to block future dates
        val constraintsBuilder = CalendarConstraints.Builder()
            .setEnd(today)

        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText(getString(R.string.watching_date))
            .setCalendarConstraints(constraintsBuilder.build())
            .setTheme(R.style.CustomDatePickerTheme)
            .build()

        datePicker.show(parentFragmentManager, "datePicker")

        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate =
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(selection))
            binding.chipDatePicker.text = selectedDate
        }
    }
}