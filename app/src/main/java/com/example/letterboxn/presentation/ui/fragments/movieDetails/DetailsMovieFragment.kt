package com.example.letterboxn.presentation.ui.fragments.movieDetails

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.Resources
import android.graphics.Color
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.common.utils.DoubleClickListener
import com.example.letterboxn.data.remote.api.TmdbApi
import com.example.letterboxn.data.remote.model.movieCredit.Cast
import com.example.letterboxn.data.remote.model.movieCredit.Crew
import com.example.letterboxn.databinding.FragmentDetailsMovieBinding
import com.example.letterboxn.presentation.adapters.MovieDetailsCastAdapter
import com.example.letterboxn.presentation.adapters.MovieDetailsCrewAdapter
import com.example.letterboxn.presentation.adapters.MovieDetailsReviewsAdapter
import com.example.letterboxn.common.utils.NancyToast
import com.example.letterboxn.common.utils.numberFormatter
import com.example.letterboxn.common.utils.randomInteger
import com.example.letterboxn.data.remote.model.account.favoriteMovies.ResultFavoriteMovie
import com.example.letterboxn.data.remote.model.account.favoriteMovies.favMovie.RequestAddRemoveFavorite
import com.example.letterboxn.data.remote.model.account.ratedMovies.ResultRatedMovie
import com.example.letterboxn.data.remote.model.account.watchlist.ResultWatchlist
import com.example.letterboxn.data.remote.model.account.watchlist.addWatchlist.RequestAddRemoveWatchlist
import com.example.letterboxn.domain.model.ReviewWithoutMovieItem
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class DetailsMovieFragment :
    BaseFragment<FragmentDetailsMovieBinding>(FragmentDetailsMovieBinding::inflate) {

    @Inject
    lateinit var api: TmdbApi

    private val args: DetailsMovieFragmentArgs by navArgs()

    val castAdapter = MovieDetailsCastAdapter(
        onClick = {
            findNavController().navigate(DetailsMovieFragmentDirections.actionDetailsMovieFragmentToPersonDetailsBottomSheetFragment(it))
        }
    )
    val crewAdapter = MovieDetailsCrewAdapter(
        onClick = {
            findNavController().navigate(DetailsMovieFragmentDirections.actionDetailsMovieFragmentToPersonDetailsBottomSheetFragment(it))
        }
    )
//    val reviewAdapter = MovieDetailsReviewsAdapter(
//        onClick = {
//            findNavController().navigate(DetailsMovieFragmentDirections.actionDetailsMovieFragmentToRecentReviewDetailsBottomSheetFragment(it))
//        }
//    )

    var movieTitle : String = ""
    var movieId : Int = 0
    var rating = 0f
    var isAlreadyAddedToList = false
    var isFav = false
    var isAddedToWatchList = false

//    private lateinit var shimmer : ShimmerFrameLayout

    override fun onViewCreatedLight() {

//        shimmer = binding.detailsShimmer
//        shimmer.startShimmer()

        val savedStateHandled = findNavController().currentBackStackEntry?.savedStateHandle
        savedStateHandled?.getLiveData<Float>("result")
            ?.observe(viewLifecycleOwner) {
                rating = it
            }
        savedStateHandled?.getLiveData<Boolean>("requestStatus")
            ?.observe(viewLifecycleOwner) {
                isAlreadyAddedToList = !it
                Snackbar.make(requireView(), "Movie added to recent watched list.", Snackbar.LENGTH_SHORT)
                    .setAction("See all") {
                        findNavController().navigate(DetailsMovieFragmentDirections.actionDetailsMovieFragmentToProfileFragment())
                    }.show()
                binding.buttonAddtoMyList.text = "Added to Lists"
                binding.buttonAddtoMyList.setIconResource(R.drawable.round_check_circle_24)
                binding.textMyRatingDetails.visibility = View.VISIBLE
                binding.textRatingValueDetails.visibility = View.VISIBLE
                binding.imageStarMyRatingDetails.visibility = View.VISIBLE
                binding.textRatingValueDetails.text = rating.toString()
            }


        var castResponse = emptyList<Cast>()
        var crewResponse = emptyList<Crew>()
        lifecycleScope.launch {
            val movieItem = api.getMovieDetails(movieId = args.movieId)
            castResponse = api.getMovieCredits(movieItem.id).cast
            crewResponse = api.getMovieCredits(movieItem.id).crew

            setAdapters()

            binding.rvCast.visibility = View.VISIBLE
            binding.rvCrew.visibility = View.INVISIBLE
            castAdapter.updateAdapter(castResponse)
        }


        binding.tablayoutgroup.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    binding.rvCast.visibility = View.VISIBLE
                    binding.rvCrew.visibility = View.INVISIBLE
                    binding.LinearGroupDetails.visibility = View.GONE
                    castAdapter.updateAdapter(castResponse)
                }
                else if (tab?.position == 1) {
                    binding.rvCast.visibility = View.INVISIBLE
                    binding.rvCrew.visibility = View.VISIBLE
                    binding.LinearGroupDetails.visibility = View.GONE
                    crewAdapter.updateAdapter(crewResponse)
                }
                else {
                    binding.rvCast.visibility = View.GONE
                    binding.rvCrew.visibility = View.GONE
                    binding.LinearGroupDetails.visibility = View.VISIBLE
                    with(binding) {
                        rvCrew.visibility = View.GONE
                        rvCast.visibility = View.GONE
                        LinearGroupDetails.visibility = View.VISIBLE
                        lifecycleScope.launch {
                            val details = api.getMovieDetails(args.movieId)
                            val genres = details.genres.map { it.name }
                            val concatenatedGenres = genres.joinToString(separator = "\n")
                            val languages = details.spokenLanguages.map { it.englishName }
                            val concatenatedLanguages = languages.joinToString(separator = "\n")
                            textGenresDetails.text = concatenatedGenres
                            textLanguagesDetails.text = concatenatedLanguages
                            textBudgetDetails.text = if (details.budget != 0) "$${details.budget}" else "not announced"
                            textRevenueDetails.text = if (details.revenue != 0) "$${details.revenue}" else "not announced"
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }

            override fun onTabReselected(tab: TabLayout.Tab?) { }

        })

        lifecycleScope.launch {
            val movieItem = api.getMovieDetails(movieId = args.movieId)
            movieTitle = movieItem.title
            movieId = movieItem.id
            try {
                if (findMovieById(movieId) != null) {  //found
                    isAlreadyAddedToList = true
                    binding.buttonAddtoMyList.text = "Added to Lists"
                    binding.buttonAddtoMyList.setIconResource(R.drawable.round_check_circle_24)
                    binding.textMyRatingDetails.visibility = View.VISIBLE
                    binding.imageStarMyRatingDetails.visibility = View.VISIBLE
                    binding.textRatingValueDetails.visibility = View.VISIBLE
                    binding.textRatingValueDetails.text = api.getRatedMoviesAccount().results.find {it.id == movieId}?.rating.toString()
                }
                binding.buttonAddtoMyList.isClickable = true
            } catch (e: Exception) {
                e.printStackTrace() // Handle exceptions (network errors, etc)
            }
            try {
                if (findMovieByIdFav(movieId) != null) {  //found
                    isFav = true
                    binding.floatingActionButtonLikeMovie.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                e.printStackTrace() // Handle exceptions (network errors, etc)
            }
            try {
                if (findMovieByIdWatchlist(movieId) != null) {  //found
                    isAddedToWatchList = true
                    binding.buttonAddtoWatchlist.textSize = 10f
                    binding.buttonAddtoWatchlist.text = "Added to Watchlist"
                    val size = dpToPx(16f).toInt()
                    binding.buttonAddtoWatchlist.iconSize = size
                    binding.buttonAddtoWatchlist.setIconResource(R.drawable.round_check_circle_24)
                }
                binding.buttonAddtoWatchlist.isClickable = true
            } catch (e: Exception) {
                e.printStackTrace() // Handle exceptions (network errors, etc)
            }
            with(binding) {
                imageBackposterDetails.load("https://image.tmdb.org/t/p/w1280" + movieItem.backdropPath) {
                    crossfade(true)
                    placeholder(R.drawable.custom_movie_back_poster)
//                    error(R.drawable.round_error_outline_24)
                }
                Glide.with(imagePosterDetails)
                    .load("https://image.tmdb.org/t/p/w780" + movieItem.posterPath)
                    .placeholder(R.drawable.custom_poster_shape)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.default_movie_placeholder)
                    .into(imagePosterDetails)
                textMovietitleDetails.text = movieItem.title
                textReleasedate.text = movieItem.releaseDate.substring(0, 4)
                textMoviedurationDetails.text = if (movieItem.runtime != 0) {
                    "${movieItem.runtime} min"
                } else "N/A"
                val director = "Christopher Nolan" //real director from api
                textMoviedirectorDetails.text = Html.fromHtml("Directed by <b>$director</b>")
                textMovieoverviewmainDetails.text = movieItem.overview
                ratingBarDetailsMovie.rating = movieItem.voteAverage.toFloat() / 2
                textRatingvalueDetails.text = binding.ratingBarDetailsMovie.rating.toString()
                val views = 40172  //from api
                val likes = 31833
                val reviews = 12389
                textMovieviewcountDetails.text = numberFormatter(views.toLong())
                textMovielikecountDetails.text = numberFormatter(likes.toLong())
                textMoviereviewcountDetails.text = numberFormatter(reviews.toLong())

//                shimmer.stopShimmer()
//                shimmer.visibility = View.GONE
            }

            binding.buttonRateorReviewDetails.setOnClickListener {
                findNavController().navigate(DetailsMovieFragmentDirections.actionDetailsMovieFragmentToWriteReviewFragment(movieId = movieItem.id))
            }


            try{
                val response = api.getMovieReviews(movieId)
                binding.rvReviews.adapter = MovieDetailsReviewsAdapter(
                    bindinqa = binding,
                    reviews = response.results.map {
                        ReviewWithoutMovieItem(
                            authorName = it.author,
                            authorImage = it.authorDetails.avatarPath,
                            reviewId = it.id,
                            review = it.content,
                            reviewRating = it.authorDetails.rating.toFloat()/2,
                            commentCount = randomInteger(100, 10000)
                        )
                    },
                    onClick = {
                        // more click edende reviewda 5 line elave acilsin
                    }
                )
            }
            catch (e:Exception) {
                Log.e("api", e.toString())
            }
        }

    }

    private suspend fun findMovieById(movieId: Int): ResultRatedMovie? {
        val response = api.getRatedMoviesAccount()
        return response.results.find { it.id == movieId }
    }
    private suspend fun findMovieByIdFav(movieId: Int): ResultFavoriteMovie? {
        val response = api.getFavoriteMovies()
        return response.results.find { it.id == movieId }
    }
    private suspend fun findMovieByIdWatchlist(movieId: Int): ResultWatchlist? {
        val response = api.getWatchlist()
        return response.results.find { it.id == movieId }
    }


    @SuppressLint("ResourceAsColor")
    override fun observeChanges() {
        binding.floatingActionButtonPosterback.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonSeeallReviewsDetailsMovie.setOnClickListener {
            NancyToast.makeText(requireContext(), "[All reviews]", NancyToast.LENGTH_SHORT, NancyToast.INFO, false).show()
        }

        binding.floatingActionButtonLikeMovie.setOnClickListener {
            isFav = false
            binding.floatingActionButtonLikeMovie.visibility = View.GONE
            Snackbar.make(requireView(), "Movie removed from Favorites!", Snackbar.LENGTH_LONG)
                .setAction("Undo") {
                    // how to cancel the rest of process
                    lifecycleScope.launch {
                        api.addOrRemoveFavoriteMovie(
                            requestFavorite = RequestAddRemoveFavorite(
                                favorite = true,
                                mediaId = movieId,
                                mediaType = "movie"
                            )
                        )
                        isFav = true
                        binding.floatingActionButtonLikeMovie.visibility = View.VISIBLE
                        Snackbar.make(requireView(), "Movie added to Favorites!", Snackbar.LENGTH_SHORT).show()
                    }
                }.show()
            lifecycleScope.launch {
                api.addOrRemoveFavoriteMovie(
                    requestFavorite = RequestAddRemoveFavorite(
                        favorite = false,
                        mediaId = movieId,
                        mediaType = "movie"
                    )
                )
            }
        }

        binding.imagePosterDetails.setOnClickListener(object : DoubleClickListener() {
            override fun onDoubleClick() {
                if (!isFav) {
                    lifecycleScope.launch {
                        api.addOrRemoveFavoriteMovie(
                            requestFavorite = RequestAddRemoveFavorite(
                                favorite = true,
                                mediaId = movieId,
                                mediaType = "movie"
                            )
                        )
                    }
                    Snackbar.make(requireView(), "Movie added to Favorites!", Snackbar.LENGTH_SHORT).show()
                    binding.floatingActionButtonLikeMovie.visibility = View.VISIBLE
                }
            }
        })

        binding.buttonAddtoMyList.setOnClickListener {
            if (isAlreadyAddedToList) {
                deleteFromList(movieId = movieId)
                isAlreadyAddedToList = false
            }
            else {
                addToList(movieId = movieId)
                isAlreadyAddedToList = true
            }
        }

        binding.buttonAddtoWatchlist.setOnClickListener { view ->
            lifecycleScope.launch {
                api.addOrRemoveWatchlist(requestWatchlist = RequestAddRemoveWatchlist(
                    mediaId = movieId,
                    mediaType = "movie",
                    watchlist = !isAddedToWatchList
                ))
            }
            if (isAddedToWatchList) {
                Snackbar.make(view, "Movie removed from watchlist.", Snackbar.LENGTH_SHORT).show()
                isAddedToWatchList = false
                binding.buttonAddtoWatchlist.textSize = 11f
                binding.buttonAddtoWatchlist.text = "Add to Watchlist"
                val size = dpToPx(19f).toInt()
                binding.buttonAddtoWatchlist.iconSize = size
                binding.buttonAddtoWatchlist.setIconResource(R.drawable.round_playlist_add_24)
            }
            else {
                Snackbar.make(view, "Movie added to watchlist.", Snackbar.LENGTH_SHORT)
                    .setAction("See all") {
                        findNavController().navigate(DetailsMovieFragmentDirections.actionDetailsMovieFragmentToWatchlistBottomSheetFragment())
                    }.show()
                isAddedToWatchList = true
                binding.buttonAddtoWatchlist.textSize = 10f
                binding.buttonAddtoWatchlist.text = "Added to Watchlist"
                val size = dpToPx(16f).toInt()
                binding.buttonAddtoWatchlist.iconSize = size
                binding.buttonAddtoWatchlist.setIconResource(R.drawable.round_check_circle_24)
            }
        }

    }
    private fun dpToPx(dp: Float): Float {
        return dp * Resources.getSystem().displayMetrics.density
    }

    private fun addToList(movieId: Int) {
        findNavController().navigate(
            DetailsMovieFragmentDirections.actionDetailsMovieFragmentToRateMovieBottomSheetFragment(movieId = movieId, movieTitle = movieTitle)
        )
    }

    private fun deleteFromList(movieId : Int) {

        val customTitle = TextView(requireContext()).apply {
            text = "  Are you sure to remove?"
            setPadding(40, 40, 40, 40) // Adjust padding as needed
            setTextSize(24f)
            setTextColor(Color.WHITE)
        }

        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setCustomTitle(customTitle)
            .setMessage("If you remove this movie from Watched list, the rating for it will also be deleted permanently.")
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                NancyToast.makeText(requireContext(), "Operation cancelled", NancyToast.LENGTH_SHORT, NancyToast.DEFAULT, false).show()
            }
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    api.deleteRateMovie(movieId = movieId)
                    Snackbar.make(requireView(), "Movie removed from recent watched list.", Snackbar.LENGTH_SHORT).show()
                    binding.buttonAddtoMyList.text = "Add to Lists"
                    binding.buttonAddtoMyList.setIconResource(R.drawable.round_list_bulleted_24)
                    binding.textMyRatingDetails.visibility = View.INVISIBLE
                    binding.textRatingValueDetails.visibility = View.INVISIBLE
                    binding.imageStarMyRatingDetails.visibility = View.INVISIBLE
                }
            }.create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(Color.WHITE)

            val textViewId = dialog.context.resources.getIdentifier("android:id/alertTitle", null, null)
            val textView = dialog.findViewById<TextView>(textViewId)
            textView?.setTextColor(Color.WHITE)

            val messageTextViewId = dialog.context.resources.getIdentifier("android:id/message", null, null)
            val messageTextView = dialog.findViewById<TextView>(messageTextViewId)
            messageTextView?.setTextColor(Color.WHITE)
        }

        dialog.show()
    }

    fun observe() {
    }

    fun setAdapters() {
        binding.rvCast.adapter = castAdapter
        binding.rvCrew.adapter = crewAdapter
//        binding.rvReviews.adapter = reviewAdapter
    }

}