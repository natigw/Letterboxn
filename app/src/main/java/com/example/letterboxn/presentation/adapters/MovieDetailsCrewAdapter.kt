package com.example.letterboxn.presentation.adapters

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseAdapter
import com.example.letterboxn.data.remote.model.movieCredit.Crew
import com.example.letterboxn.databinding.SamplePeopleMoviedetailsBinding

class MovieDetailsCrewAdapter(
    val onClick: (Int) -> Unit
) : BaseAdapter<SamplePeopleMoviedetailsBinding>(SamplePeopleMoviedetailsBinding::inflate) {

    var crewStaff = emptyList<Crew>()

    override fun getItemCount(): Int {
        return crewStaff.size
    }

    override fun onBindLight(binding: SamplePeopleMoviedetailsBinding, position: Int) {
        val personItem = crewStaff[position]
        Glide.with(binding.imagePersonMoviedetails)
            .load("https://image.tmdb.org/t/p/h632" + personItem.profilePath)
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.placeholder_user)
            .into(binding.imagePersonMoviedetails)
        binding.root.setOnClickListener {
            onClick(personItem.id)
        }
    }

    fun updateAdapter(crewStaff : List<Crew>) {
        this.crewStaff = crewStaff
        notifyDataSetChanged()
    }
}