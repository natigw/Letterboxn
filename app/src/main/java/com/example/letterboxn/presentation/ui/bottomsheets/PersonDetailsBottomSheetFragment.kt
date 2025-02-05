package com.example.letterboxn.presentation.ui.bottomsheets

import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseBottomSheetFragment
import com.example.letterboxn.data.remote.api.MovieApi
import com.example.letterboxn.databinding.BottomsheetfragmentPersonDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PersonDetailsBottomSheetFragment : BaseBottomSheetFragment<BottomsheetfragmentPersonDetailsBinding>(BottomsheetfragmentPersonDetailsBinding::inflate) {

    @Inject
    lateinit var api : MovieApi

    private val args : PersonDetailsBottomSheetFragmentArgs by navArgs()

    override fun onViewCreatedLight() {

        viewLifecycleOwner.lifecycleScope.launch {
            val personItem = api.getPersonDetails(personId = args.personId)

            with(binding){
                Glide.with(imageImagePerson)
                    .load("https://image.tmdb.org/t/p/w500" + personItem.profilePath)
                    .placeholder(R.drawable.placeholder_user)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageImagePerson)
                textNamePerson.text = personItem.name
                textDepartmentPerson.text = "Department: ${personItem.knownForDepartment}"
                val birth = personItem.birthday
                if (birth == null) textBirthdayPerson.visibility = View.GONE else textBirthdayPerson.text = "Birthday: $birth"
                val death = personItem.deathday
                if (death == null) textDeathdayPerson.visibility = View.GONE else textDeathdayPerson.text = "Deathday: $death"
                if (personItem.homepage == null) textLinkPerson.visibility = View.GONE else textLinkPerson.text = "Homepage: ${personItem.homepage}"
                if (personItem.biography == null || personItem.biography == "") textBiographysozle.visibility = View.INVISIBLE else textBioPerson.text = personItem.biography
            }
        }
    }

}