package com.filmsaboinf.ofnimlif.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.filmsaboinf.ofnimlif.R
import com.filmsaboinf.ofnimlif.ViewModelLoadData
import com.filmsaboinf.ofnimlif.adapters.AdapterRecyclerFilms
import com.filmsaboinf.ofnimlif.databinding.FragmentMainBinding
import com.filmsaboinf.ofnimlif.repository.InternetListener
import com.filmsaboinf.ofnimlif.repository.model.FilmPreview
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private val timeOut = 0L

    private lateinit var binding: FragmentMainBinding

    private val viewModelLoad by activityViewModels<ViewModelLoadData>()

    private val adapterRecyclerFilms by lazy {
        AdapterRecyclerFilms(arrayListOf(), openDetailsFragment)
    }

    private val internetListener by lazy {
        InternetListener(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewFilm.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewFilm.adapter = adapterRecyclerFilms

        viewModelLoad.loadData.observe(viewLifecycleOwner) { list ->
            if (!list.isNullOrEmpty()) {
                binding.progressCircular.visibility = View.INVISIBLE
                adapterRecyclerFilms.changeList(list)
                binding.recyclerViewFilm.visibility = View.VISIBLE
            } else {
                binding.progressCircular.visibility = View.VISIBLE
                binding.recyclerViewFilm.visibility = View.INVISIBLE
            }
        }

        if (viewModelLoad.loadData.value.isNullOrEmpty()) {
            loadDataInternet()
        }

        binding.loadAgainButton.setOnClickListener {
            loadDataInternet()
        }

    }

    private val openDetailsFragment: (FilmPreview) -> Unit = { filmItem ->
        val bundle = bundleOf("film" to filmItem)
        findNavController().navigate(R.id.action_mainFragment_to_subFragment, bundle)
    }

    private fun loadDataInternet() {
        if (internetListener.checkForInternet()) {
            binding.progressCircular.visibility = View.VISIBLE
            binding.loadAgainButton.visibility = View.INVISIBLE
            viewModelLoad.loadFilmsData(1)
        } else {
            binding.progressCircular.visibility = View.INVISIBLE
            binding.loadAgainButton.visibility = View.VISIBLE
            getDownButton()
        }
    }

    private fun getDownButton() {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.loadAgainButton.isEnabled = false
            binding.loadAgainButton.setImageResource(R.drawable.ic_baseline_refresh_32_disable)
            delay(1000L)
            Snackbar.make(binding.root, "Need to internet connection", Snackbar.LENGTH_SHORT).show()
            binding.loadAgainButton.setImageResource(R.drawable.ic_baseline_refresh_32_enable)
            binding.loadAgainButton.isEnabled = true

        }
    }

}


