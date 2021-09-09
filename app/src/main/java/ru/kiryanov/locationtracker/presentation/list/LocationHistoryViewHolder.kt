package ru.kiryanov.locationtracker.presentation.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import kotlinx.android.synthetic.main.item_location_history.view.*
import ru.kiryanov.locationtracker.R
import ru.kiryanov.locationtracker.domain.DomainLocation
import vlnny.base.adapter.BaseViewHolder
import vlnny.base.ext.round

class LocationHistoryViewHolder private constructor(itemView: View) :
    BaseViewHolder<DomainLocation>(itemView) {

    companion object {
        fun create(parent: ViewGroup) = LocationHistoryViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_location_history,
                    parent,
                    false
                )
        )
    }

    private val latitudeTextView : AppCompatTextView by lazy { itemView.latitudeTextView }
    private val longitudeTextView : AppCompatTextView by lazy { itemView.longitudeTextView }
    private val dateTextView : AppCompatTextView by lazy { itemView.dateTextView }

    @SuppressLint("SetTextI18n")
    override fun bindView(item: DomainLocation) {
        latitudeTextView.text = "Lat: ${item.location.latitude.round(5)}"
        longitudeTextView.text = "Lan: ${item.location.longitude.round(5)}"
        dateTextView.text = item.date
    }
}