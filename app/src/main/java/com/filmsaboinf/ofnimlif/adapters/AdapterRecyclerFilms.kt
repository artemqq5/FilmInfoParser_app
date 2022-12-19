package com.filmsaboinf.ofnimlif.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.filmsaboinf.ofnimlif.R
import com.filmsaboinf.ofnimlif.databinding.ItemRecyclerFilmBinding
import com.filmsaboinf.ofnimlif.repository.model.FilmPreview
import com.squareup.picasso.Picasso

class AdapterRecyclerFilms(
    var listOfFilms: List<FilmPreview>, val openDetails: (FilmPreview) -> Unit
) : RecyclerView.Adapter<AdapterRecyclerFilms.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemRecyclerFilmBinding.bind(view)

        fun bindItem(item: FilmPreview) {

            binding.title.text = item.name
            Picasso.get()
                .load(item.img)
                .resize(600, 600)
                .centerInside()
                .into(binding.imageFilm)

            binding.root.setOnClickListener {
                openDetails(item)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_recycler_film, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(listOfFilms[position])
    }

    override fun getItemCount(): Int {
        return listOfFilms.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeList(newList: List<FilmPreview>) {
        listOfFilms = newList
        notifyDataSetChanged()
    }

}