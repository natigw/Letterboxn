package com.example.letterboxn.presentation.adapters

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseAdapter
import com.example.letterboxn.data.remote.model.movieCredit.Cast
import com.example.letterboxn.databinding.SamplePeopleMoviedetailsBinding

class MovieDetailsCastAdapter(
    val onClick: (Int) -> Unit
) : BaseAdapter<SamplePeopleMoviedetailsBinding>(SamplePeopleMoviedetailsBinding::inflate) {

    var castStaff = emptyList<Cast>()

    override fun getItemCount(): Int {
        return castStaff.size
    }

    override fun onBindLight(binding: SamplePeopleMoviedetailsBinding, position: Int) {

        val personItem = castStaff[position]

        Glide.with(binding.imagePersonMoviedetails)
            .load("https://image.tmdb.org/t/p/h632" + personItem.profilePath)
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.usersample)
            .into(binding.imagePersonMoviedetails)

        binding.root.setOnClickListener {
            onClick(personItem.id)
        }
    }

    fun updateAdapter(newCastStaff : List<Cast>) {
        castStaff = newCastStaff
        notifyDataSetChanged()
    }
}