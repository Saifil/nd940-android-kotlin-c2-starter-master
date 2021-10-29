package com.udacity.asteroidradar.main.recycler

import androidx.recyclerview.widget.DiffUtil
import com.udacity.asteroidradar.Asteroid

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minumum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class AsteroidDiffCallback : DiffUtil.ItemCallback<Asteroid>() {
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid) = oldItem == newItem

}