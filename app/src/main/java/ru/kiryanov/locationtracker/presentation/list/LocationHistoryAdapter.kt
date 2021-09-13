package ru.kiryanov.locationtracker.presentation.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kiryanov.locationtracker.domain.model.DomainLocation
import javax.inject.Inject

class LocationHistoryAdapter @Inject constructor() : RecyclerView.Adapter<LocationHistoryViewHolder>() {

    private var items = listOf<DomainLocation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationHistoryViewHolder {
        return LocationHistoryViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: LocationHistoryViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount() = items.size

    fun updateList(data: List<DomainLocation>) {
        items = data
        notifyDataSetChanged()
    }
}