package com.udacity.asteroidradar.main.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidListItemViewBinding

class AsteroidListAdapter(
    private val itemClickListener: (item: Asteroid) -> Unit
) : ListAdapter<Asteroid, RecyclerView.ViewHolder>(AsteroidDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AsteroidListViewHolder.from(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val asteroidListViewHolder = holder as AsteroidListViewHolder
        asteroidListViewHolder.bind(itemClickListener, getItem(position))
    }

    class AsteroidListViewHolder(private val binding: AsteroidListItemViewBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            itemClickListener: (item: Asteroid) -> Unit,
            item: Asteroid
        ) = with(binding) {
            name.text = item.codename
            date.text = item.closeApproachDate
            if (item.isPotentiallyHazardous) {
                icon.setImageResource(R.drawable.ic_status_potentially_hazardous)
            } else {
                icon.setImageResource(R.drawable.ic_status_normal)
            }
            root.setOnClickListener { itemClickListener(item) }
        }

        companion object {
            fun from(parent: ViewGroup) : RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AsteroidListItemViewBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )

                return AsteroidListViewHolder(binding)
            }
        }
    }

}