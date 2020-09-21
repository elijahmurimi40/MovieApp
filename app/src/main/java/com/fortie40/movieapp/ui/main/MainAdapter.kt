package com.fortie40.movieapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.fortie40.movieapp.R
import com.fortie40.movieapp.data.models.MovieResponse

class MainAdapter: ListAdapter<MovieResponse, MainViewHolder>(MovieResponseDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_vertical, parent, false)
        return MainViewHolder((view))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val response = getItem(position)
        val title = response.title
        val movies = response.movieList

        holder.titleVV.text = title
        val itemAdapter = ItemAdapter()
        holder.titleRV.setHasFixedSize(true)
        holder.titleRV.layoutManager = LinearLayoutManager(holder.titleRV.context, LinearLayoutManager.HORIZONTAL, false)
        holder.titleRV.adapter = itemAdapter
        itemAdapter.submitList(movies)
    }

    class MovieResponseDiffCallback: DiffUtil.ItemCallback<MovieResponse>() {
        override fun areItemsTheSame(oldItem: MovieResponse, newItem: MovieResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieResponse, newItem: MovieResponse): Boolean {
            return oldItem == newItem
        }
    }
}