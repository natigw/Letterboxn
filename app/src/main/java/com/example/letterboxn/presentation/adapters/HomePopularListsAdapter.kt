package com.example.letterboxn.presentation.adapters

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.common.base.BaseAdapter
import com.example.letterboxn.common.utils.numberFormatter
import com.example.letterboxn.databinding.SamplePopularListHomeBinding
import com.example.letterboxn.domain.model.home.popularLists.PopularListItem
import com.google.android.material.imageview.ShapeableImageView

class HomePopularListsAdapter(
    val onClick: (PopularListItem) -> Unit
) : BaseAdapter<SamplePopularListHomeBinding>(SamplePopularListHomeBinding::inflate) {

    private var lists: List<PopularListItem> = emptyList()

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindLight(binding: SamplePopularListHomeBinding, position: Int) {

        val collection = lists[position]

        with(binding) {
            putImage(imageListOne, collection.movie1.moviePoster)
            putImage(imageListTwo, collection.movie2.moviePoster)
            putImage(imageListThree, collection.movie3.moviePoster)
            putImage(imageListFour, collection.movie4.moviePoster)
            textListNamePopularList.text = collection.listTitle
            Glide.with(imageAuthorPopularList)
                .load("https://image.tmdb.org/t/p/h632${collection.listAuthorImage}")
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageAuthorPopularList)
            textAuthorNamePopularLists.text = collection.listAuthorName
            textLikeCountPopularList.text = numberFormatter(collection.likeCount.toLong())
            textCommentCountPopularList.text = numberFormatter(collection.commentCount.toLong())
            root.setOnClickListener {
                onClick(collection)
            }
        }
    }

    private fun putImage(dest: ShapeableImageView, moviePoster: String?) {
        Glide.with(dest)
            .load("https://image.tmdb.org/t/p/w500$moviePoster")
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(dest)
    }

    fun updateAdapter(newLists: List<PopularListItem>) {
        lists = newLists
        notifyDataSetChanged()
    }
}