package com.example.letterboxn.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.databinding.SampleExplorePosterBinding
import com.example.letterboxn.domain.model.MovieItem

class MoviePagingAdapter(private val movieClickListener: MovieClickListener) :
    PagingDataAdapter<MovieItem, MoviePagingAdapter.ViewHolder>(MovieDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, movieClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: SampleExplorePosterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MovieItem, movieClickListener: MovieClickListener) {
            binding.textMovienameExplore.text = item.movieTitle
            Glide.with(binding.imageMovieExplore)
                .load("https://image.tmdb.org/t/p/w500" + item.moviePoster)
                .placeholder(R.drawable.custom_poster_shape)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageMovieExplore)
            binding.root.setOnClickListener {
                movieClickListener.onMovieClick(item)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SampleExplorePosterBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class MovieDiffCallback : DiffUtil.ItemCallback<MovieItem>() {
    override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
        return oldItem.movieId == newItem.movieId
    }

    override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
        return oldItem == newItem
    }
}

fun interface MovieClickListener {
    fun onMovieClick(movie: MovieItem)
}