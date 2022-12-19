package com.filmsaboinf.ofnimlif.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.filmsaboinf.ofnimlif.R
import com.filmsaboinf.ofnimlif.ViewModelLoadData
import com.filmsaboinf.ofnimlif.databinding.FragmentSubBinding
import com.filmsaboinf.ofnimlif.repository.InternetListener
import com.filmsaboinf.ofnimlif.repository.model.FilmPreview
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SubFragment : Fragment() {

    private lateinit var binding: FragmentSubBinding
    private val viewModelLoad by activityViewModels<ViewModelLoadData>()

    private var film: FilmPreview? = null

    private val internetListener by lazy {
        InternetListener(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        film = arguments?.getParcelable<FilmPreview>("film")

        viewModelLoad.loadDataDetail.observe(viewLifecycleOwner) {
            Picasso.get()
                .load(it.img)
                .resize(600, 600)
                .centerInside()
                .into(binding.imgFilm)

            binding.titleFilm.text = it.name
            binding.descriptionFilm.text = it.description
            binding.countryFilm.text = it.country
            binding.rateFilm.text = it.rate
        }

        binding.loadAgainButton.setOnClickListener {
            loadDataInternet()
            binding.loadAgainButton.visibility = View.INVISIBLE
        }

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_subFragment_to_mainFragment)
        }

        loadDataInternet()

    }

    private fun loadDataInternet() {
        film?.let {
            if (internetListener.checkForInternet()) {
                binding.loadAgainButton.visibility = View.INVISIBLE
                viewModelLoad.loadDetailToFilm(it)
                binding.constraintLayout.visibility = View.VISIBLE
            } else {
                binding.constraintLayout.visibility = View.INVISIBLE
                lifecycleScope.launch(Dispatchers.Main) {
                    binding.progressCircular.visibility = View.VISIBLE
                    delay(1000L)
                    Snackbar.make(binding.root, "Need to internet connection", Snackbar.LENGTH_SHORT).show()
                    binding.progressCircular.visibility = View.INVISIBLE
                    binding.loadAgainButton.visibility = View.VISIBLE
                }

            }
        }
    }


}