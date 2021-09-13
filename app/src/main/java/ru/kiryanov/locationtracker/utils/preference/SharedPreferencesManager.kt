package ru.kiryanov.locationtracker.utils.preference

interface SharedPreferencesManager {
    fun save(key: String, value: Boolean)

    fun get(key: String) : Boolean
}