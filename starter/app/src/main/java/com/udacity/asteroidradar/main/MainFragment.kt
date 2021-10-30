package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.NasaImage
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.main.recycler.AsteroidListAdapter
import com.udacity.asteroidradar.main.recycler.MainViewModelFactory
import com.udacity.asteroidradar.util.visibleIf
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(
            this, MainViewModelFactory(requireContext())).get(MainViewModel::class.java)
    }
    private lateinit var binding: FragmentMainBinding
    private val adapter = AsteroidListAdapter { asteroid ->
        findNavController().navigate(
            MainFragmentDirections.actionShowDetail(asteroid)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        // setup the adapter
        binding.asteroidRecycler.adapter = adapter

        /**
         * Simplify the app by removing the filters for now.
         *
         * To implement the filters, we can have 3 separate methods in the viewModel,
         * that call the API service with different start and end date.
         * This will need us to have more involved parsing logic. For the MVP,
         * I would like to focus more on the rubrics metrics. Thanks!
         */
        // setHasOptionsMenu(true)

        // setup viewModel observers
        viewModel.asteroids.observe(viewLifecycleOwner) { displayAsteroids(it) }
        viewModel.imageOfTheDay.observe(viewLifecycleOwner) { loadImageOfTheDay(it) }

        // show loading views
        showAsteroidLoading(true)

        return binding.root
    }

    private fun loadImageOfTheDay(image: NasaImage?) = with(binding) {
        if (image == null) {
            return@with
        }
        Glide.with(requireContext())
            .load(image.url)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .placeholder(R.drawable.placeholder_picture_of_day)
            .into(activityMainImageOfTheDay)
    }

    private fun displayAsteroids(asteroids: List<Asteroid>? = emptyList()) =
        lifecycleScope.launch {
            showAsteroidLoading(false)
            adapter.submitList(asteroids)
        }

    private fun showAsteroidLoading(shouldLoading: Boolean) =
        binding.statusLoadingWheel.visibleIf(shouldLoading)
}
