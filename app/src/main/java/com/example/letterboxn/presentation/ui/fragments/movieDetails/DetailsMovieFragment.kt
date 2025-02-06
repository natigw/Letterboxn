package com.example.letterboxn.presentation.ui.fragments.movieDetails

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.common.utils.DoubleClickListener
import com.example.letterboxn.common.utils.dpToPx
import com.example.letterboxn.common.utils.nancyToast
import com.example.letterboxn.common.utils.nancyToastInfo
import com.example.letterboxn.common.utils.numberFormatter
import com.example.letterboxn.common.utils.randomInteger
import com.example.letterboxn.data.remote.model.account.favoriteMovies.ResultFavoriteMovie
import com.example.letterboxn.data.remote.model.account.favoriteMovies.favMovie.RequestAddRemoveFavorite
import com.example.letterboxn.data.remote.model.account.ratedMovies.ResultRatedMovie
import com.example.letterboxn.data.remote.model.account.watchlist.ResultWatchlist
import com.example.letterboxn.data.remote.model.account.watchlist.addWatchlist.RequestAddRemoveWatchlist
import com.example.letterboxn.data.remote.model.movieCredit.Cast
import com.example.letterboxn.data.remote.model.movieCredit.Crew
import com.example.letterboxn.databinding.FragmentDetailsMovieBinding
import com.example.letterboxn.domain.model.ReviewWithoutMovieItem
import com.example.letterboxn.presentation.adapters.MovieDetailsCastAdapter
import com.example.letterboxn.presentation.adapters.MovieDetailsCrewAdapter
import com.example.letterboxn.presentation.adapters.MovieDetailsReviewsAdapter
import com.example.letterboxn.presentation.viewmodels.MovieDetailsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsMovieFragment : BaseFragment<FragmentDetailsMovieBinding>(FragmentDetailsMovieBinding::inflate) {

    private val viewmodel by viewModels<MovieDetailsViewModel>()
    private val args: DetailsMovieFragmentArgs by navArgs()

    val castAdapter = MovieDetailsCastAdapter(
        onClick = {
            findNavController().navigate(
                DetailsMovieFragmentDirections.actionDetailsMovieFragmentToPersonDetailsBottomSheetFragment(
                    it
                )
            )
        }
    )
    val crewAdapter = MovieDetailsCrewAdapter(
        onClick = {
            findNavController().navigate(
                DetailsMovieFragmentDirections.actionDetailsMovieFragmentToPersonDetailsBottomSheetFragment(
                    it
                )
            )
        }
    )
    private val reviewAdapter = MovieDetailsReviewsAdapter()

    var movieTitle: String = ""
    var movieId: Int = 0
    var rating = 0f
    var isFav = false
    private var isAlreadyAddedToList = false
    private var isAddedToWatchList = false

//    private lateinit var shimmer : ShimmerFrameLayout

    override fun onViewCreatedLight() {

//        shimmer = binding.detailsShimmer
//        shimmer.startShimmer()

        setAdapters()

        val savedStateHandled = findNavController().currentBackStackEntry?.savedStateHandle
        savedStateHandled?.getLiveData<Float>("result")
            ?.observe(viewLifecycleOwner) {
                rating = it
            }
        savedStateHandled?.getLiveData<Boolean>("requestStatus")
            ?.observe(viewLifecycleOwner) {
                isAlreadyAddedToList = !it
                Snackbar.make(
                    requireView(),
                    getString(R.string.movie_added_to_recent_watched_list),
                    Snackbar.LENGTH_SHORT
                )
                    .setAction(getString(R.string.see_all)) {
                        findNavController().navigate(DetailsMovieFragmentDirections.actionDetailsMovieFragmentToProfileFragment())
                    }.show()
                binding.buttonAddtoMyList.text = getString(R.string.added_to_watched_list)
                binding.buttonAddtoMyList.setIconResource(R.drawable.round_check_circle_24)
                binding.textMyRatingDetails.visibility = View.VISIBLE
                binding.textRatingValueDetails.visibility = View.VISIBLE
                binding.imageStarMyRatingDetails.visibility = View.VISIBLE
                binding.textRatingValueDetails.text = rating.toString()
            }


        var castResponse = emptyList<Cast>()
        var crewResponse = emptyList<Crew>()
        viewLifecycleOwner.lifecycleScope.launch {
            val movieItem = viewmodel.api.getMovieDetails(movieId = args.movieId)
            castResponse = viewmodel.api.getMovieCredits(movieItem.id).cast
            crewResponse = viewmodel.api.getMovieCredits(movieItem.id).crew

            binding.rvCast.visibility = View.VISIBLE
            binding.rvCrew.visibility = View.INVISIBLE
            castAdapter.updateAdapter(castResponse)
        }


        binding.tablayoutgroup.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    binding.rvCast.visibility = View.VISIBLE
                    binding.rvCrew.visibility = View.INVISIBLE
                    binding.LinearGroupDetails.visibility = View.GONE
                    castAdapter.updateAdapter(castResponse)
                } else if (tab?.position == 1) {
                    binding.rvCast.visibility = View.INVISIBLE
                    binding.rvCrew.visibility = View.VISIBLE
                    binding.LinearGroupDetails.visibility = View.GONE
                    crewAdapter.updateAdapter(crewResponse)
                } else {
                    binding.rvCast.visibility = View.GONE
                    binding.rvCrew.visibility = View.GONE
                    binding.LinearGroupDetails.visibility = View.VISIBLE
                    with(binding) {
                        rvCrew.visibility = View.GONE
                        rvCast.visibility = View.GONE
                        LinearGroupDetails.visibility = View.VISIBLE
                        viewLifecycleOwner.lifecycleScope.launch {
                            val details = viewmodel.api.getMovieDetails(args.movieId)
                            val genres = details.genres.map { it.name }
                            val concatenatedGenres = genres.joinToString(separator = "\n")
                            val languages = details.spokenLanguages.map { it.englishName }
                            val concatenatedLanguages = languages.joinToString(separator = "\n")
                            textGenresDetails.text = concatenatedGenres
                            textLanguagesDetails.text = concatenatedLanguages
                            textBudgetDetails.text =
                                if (details.budget != 0) "$${details.budget}" else getString(R.string.not_announced)
                            textRevenueDetails.text =
                                if (details.revenue != 0) "$${details.revenue}" else getString(R.string.not_announced)
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })

        viewLifecycleOwner.lifecycleScope.launch {
            val movieItem = viewmodel.api.getMovieDetails(movieId = args.movieId)
            movieTitle = movieItem.title
            movieId = movieItem.id
            try {
                if (findMovieById(movieId) != null) {  //found
                    isAlreadyAddedToList = true
                    binding.buttonAddtoMyList.text = getString(R.string.added_to_watched_list)
                    binding.buttonAddtoMyList.setIconResource(R.drawable.round_check_circle_24)
                    binding.textMyRatingDetails.visibility = View.VISIBLE
                    binding.imageStarMyRatingDetails.visibility = View.VISIBLE
                    binding.textRatingValueDetails.visibility = View.VISIBLE
                    binding.textRatingValueDetails.text =
                        viewmodel.api.getRatedMoviesAccount().results.find { it.id == movieId }?.rating.toString()
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
                    binding.buttonAddtoWatchlist.text = getString(R.string.added_to_watchlist)
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
                } else getString(R.string.not_assigned)
                val director = "Christopher Nolan" //real director from viewmodel.api
                textMoviedirectorDetails.text =
                    Html.fromHtml("${getString(R.string.directed_by)} <b>$director</b>")
                textMovieoverviewmainDetails.text = movieItem.overview
                ratingBarDetailsMovie.rating = movieItem.voteAverage.toFloat() / 2
                textRatingvalueDetails.text = binding.ratingBarDetailsMovie.rating.toString()
                val views = 40172  //from viewmodel.api
                val likes = 31833
                val reviews = 12389
                textMovieviewcountDetails.text = numberFormatter(views.toLong())
                textMovielikecountDetails.text = numberFormatter(likes.toLong())
                textMoviereviewcountDetails.text = numberFormatter(reviews.toLong())

//                shimmer.stopShimmer()
//                shimmer.visibility = View.GONE
            }

            binding.buttonRateorReviewDetails.setOnClickListener {
                findNavController().navigate(
                    DetailsMovieFragmentDirections.actionDetailsMovieFragmentToWriteReviewFragment(
                        movieId = movieItem.id
                    )
                )
            }


            try {
                val reviews = viewmodel.api.getMovieReviews(movieId).results.map {
                    ReviewWithoutMovieItem(
                        authorName = it.author,
                        authorImage = it.authorDetails.avatarPath,
                        reviewId = it.id,
                        review = it.content,
                        reviewRating = it.authorDetails.rating.toFloat() / 2,
                        commentCount = randomInteger(100, 10000)
                    )
                }
                binding.textNoReviewsSentenceMovieDetails.visibility =
                    if (reviews.isEmpty()) View.VISIBLE else View.GONE
                binding.buttonSeeallReviewsDetailsMovie.visibility =
                    if (reviews.size > 5) View.VISIBLE else View.GONE
                binding.buttonSeeallReviewsDetailsMovie.setOnClickListener {
                    reviewAdapter.updateAdapter(reviews, true) //all
                    binding.root.post {
                        binding.root.scrollTo(0, binding.buttonSeeallReviewsDetailsMovie.top)
                    }
                    binding.buttonSeeallReviewsDetailsMovie.visibility = View.INVISIBLE
                }
                reviewAdapter.updateAdapter(reviews) //first 5
            } catch (e: Exception) {
                Log.e("viewmodel.api", e.toString())
            }
        }

    }

    private suspend fun findMovieById(movieId: Int): ResultRatedMovie? {
        val response = viewmodel.api.getRatedMoviesAccount()
        return response.results.find { it.id == movieId }
    }

    private suspend fun findMovieByIdFav(movieId: Int): ResultFavoriteMovie? {
        val response = viewmodel.api.getFavoriteMovies()
        return response.results.find { it.id == movieId }
    }

    private suspend fun findMovieByIdWatchlist(movieId: Int): ResultWatchlist? {
        val response = viewmodel.api.getWatchlist()
        return response.results.find { it.id == movieId }
    }


    @SuppressLint("ResourceAsColor")
    override fun observeChanges() {
        binding.floatingActionButtonPosterback.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonSeeallReviewsDetailsMovie.setOnClickListener {
            nancyToastInfo(requireContext(), getString(R.string.all_reviews))
        }

        binding.floatingActionButtonLikeMovie.setOnClickListener {
            isFav = false
            binding.floatingActionButtonLikeMovie.visibility = View.GONE
            Snackbar.make(
                requireView(),
                getString(R.string.movie_removed_from_favorites),
                Snackbar.LENGTH_LONG
            )
                .setAction(getString(R.string.undo)) {
                    //cancel the rest of process
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewmodel.api.addOrRemoveFavoriteMovie(
                            requestFavorite = RequestAddRemoveFavorite(
                                favorite = true,
                                mediaId = movieId,
                                mediaType = "movie" //TODO->enum classa kecir
                            )
                        )
                        isFav = true
                        binding.floatingActionButtonLikeMovie.visibility = View.VISIBLE
                        Snackbar.make(
                            requireView(),
                            getString(R.string.movie_added_to_favorites),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }.show()
            viewLifecycleOwner.lifecycleScope.launch {
                viewmodel.api.addOrRemoveFavoriteMovie(
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
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewmodel.api.addOrRemoveFavoriteMovie(
                            requestFavorite = RequestAddRemoveFavorite(
                                favorite = true,
                                mediaId = movieId,
                                mediaType = "movie"
                            )
                        )
                    }
                    Snackbar.make(
                        requireView(),
                        getString(R.string.movie_added_to_favorites),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    binding.floatingActionButtonLikeMovie.visibility = View.VISIBLE
                }
            }
        })

        binding.buttonAddtoMyList.setOnClickListener {
            if (isAlreadyAddedToList) {
                deleteFromList(movieId = movieId)
                isAlreadyAddedToList = false
            } else {
                addToList(movieId = movieId)
                isAlreadyAddedToList = true
            }
        }

        binding.buttonAddtoWatchlist.setOnClickListener { view ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewmodel.api.addOrRemoveWatchlist(
                    requestWatchlist = RequestAddRemoveWatchlist(
                        mediaId = movieId,
                        mediaType = "movie",
                        watchlist = !isAddedToWatchList
                    )
                )
            }
            if (isAddedToWatchList) {
                Snackbar.make(
                    view,
                    getString(R.string.movie_added_to_watchlist),
                    Snackbar.LENGTH_SHORT
                ).show()
                isAddedToWatchList = false
                binding.buttonAddtoWatchlist.textSize = 11f
                binding.buttonAddtoWatchlist.text = getString(R.string.add_to_watchlist)
                val size = dpToPx(19f).toInt()
                binding.buttonAddtoWatchlist.iconSize = size
                binding.buttonAddtoWatchlist.setIconResource(R.drawable.round_playlist_add_24)
            } else {
                Snackbar.make(
                    view,
                    getString(R.string.movie_added_to_watchlist),
                    Snackbar.LENGTH_SHORT
                )
                    .setAction(getString(R.string.see_all)) {
                        findNavController().navigate(DetailsMovieFragmentDirections.actionDetailsMovieFragmentToWatchlistBottomSheetFragment())
                    }.show()
                isAddedToWatchList = true
                binding.buttonAddtoWatchlist.textSize = 10f
                binding.buttonAddtoWatchlist.text = getString(R.string.added_to_watchlist)
                val size = dpToPx(16f).toInt()
                binding.buttonAddtoWatchlist.iconSize = size
                binding.buttonAddtoWatchlist.setIconResource(R.drawable.round_check_circle_24)
            }
        }

    }

    private fun addToList(movieId: Int) {
        findNavController().navigate(
            DetailsMovieFragmentDirections.actionDetailsMovieFragmentToRateMovieBottomSheetFragment(
                movieId = movieId,
                movieTitle = movieTitle
            )
        )
    }

    private fun deleteFromList(movieId: Int) {
        val customTitle = TextView(requireContext()).apply {
            text = getString(R.string.are_you_sure_to_remove)
            setPadding(40, 40, 40, 40)
            textSize = 24f
            setTextColor(Color.WHITE)
        }

        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setCustomTitle(customTitle)
            .setMessage(getString(R.string.remove_from_list_explanation))
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
                nancyToast(requireContext(), getString(R.string.operation_cancelled))
            }
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    viewmodel.api.deleteRateMovie(movieId = movieId)
                    Snackbar.make(
                        requireView(),
                        getString(R.string.movie_removed_from_watched_list),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    binding.buttonAddtoMyList.text = getString(R.string.add_to_watched_list)
                    binding.buttonAddtoMyList.setIconResource(R.drawable.round_list_bulleted_24)
                    binding.textMyRatingDetails.visibility = View.INVISIBLE
                    binding.textRatingValueDetails.visibility = View.INVISIBLE
                    binding.imageStarMyRatingDetails.visibility = View.INVISIBLE
                }
            }.create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(Color.WHITE)

            val textViewId =
                dialog.context.resources.getIdentifier("android:id/alertTitle", null, null)
            val textView = dialog.findViewById<TextView>(textViewId)
            textView?.setTextColor(Color.WHITE)

            val messageTextViewId =
                dialog.context.resources.getIdentifier("android:id/message", null, null)
            val messageTextView = dialog.findViewById<TextView>(messageTextViewId)
            messageTextView?.setTextColor(Color.WHITE)
        }

        dialog.show()
    }

    private fun setAdapters() {
        binding.rvCast.adapter = castAdapter
        binding.rvCrew.adapter = crewAdapter
        binding.rvReviews.adapter = reviewAdapter
    }

//    override fun onResume() {
//        super.onResume()
//        if (viewmodel..value.isEmpty())
//            startShimmer(binding.detailsShimmer)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        stopShimmer(binding.detailsShimmer)
//    }
}