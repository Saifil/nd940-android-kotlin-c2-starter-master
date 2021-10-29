package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.NasaImage
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.main.recycler.AsteroidListAdapter
import com.udacity.asteroidradar.util.hide
import com.udacity.asteroidradar.util.show
import com.udacity.asteroidradar.util.visibleIf
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private lateinit var binding: FragmentMainBinding
    private val adapter = AsteroidListAdapter { navToDetailsPage(it) }

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
        viewModel.errorEvent.observe(viewLifecycleOwner, this::showErrorBar)
        viewModel.loadingEvent.observe(viewLifecycleOwner, this::showLoading)
        viewModel.asteroids.observe(viewLifecycleOwner) { displayAsteroids(it) }
        viewModel.imageOTD.observe(viewLifecycleOwner, this::loadImageOfTheDay)

        // setup image of the day
        binding.statusLoadingWheelImageOfTheDay.show()
        viewModel.fetchNasaImageOfTheDay()

        return binding.root
    }

    private fun loadImageOfTheDay(image: NasaImage?) = with(binding) {
        if (image == null || !image.isSupported) {
            statusLoadingWheelImageOfTheDay.hide()
            return@with
        }
        Picasso.with(requireContext()).load(image.url)
            .error(R.drawable.placeholder_picture_of_day)
            .into(activityMainImageOfTheDay, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    statusLoadingWheelImageOfTheDay.hide()
                }

                override fun onError() {
                    statusLoadingWheelImageOfTheDay.hide()
                }
            })
    }

    private fun displayAsteroids(asteroids: ArrayList<Asteroid>) =
        lifecycleScope.launch { adapter.submitList(asteroids) }

    private fun showLoading(shouldLoading: Boolean) =
        binding.statusLoadingWheel.visibleIf(shouldLoading)

    private fun showErrorBar(errorMessage: String?) =
        Snackbar.make(
            binding.root,
            errorMessage ?: requireContext().getString(R.string.loading_error),
            Snackbar.LENGTH_LONG
        ).show()

    private fun navToDetailsPage(asteroid: Asteroid) {
        findNavController().navigate(
            MainFragmentDirections.actionShowDetail(asteroid)
        )
    }
}
