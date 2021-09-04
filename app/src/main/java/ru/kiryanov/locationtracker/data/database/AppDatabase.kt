package ru.kiryanov.locationtracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kiryanov.locationtracker.data.database.dao.LocationDao
import ru.kiryanov.locationtracker.data.database.entities.LocationEntity

@Database(
    entities = [LocationEntity::class],
    version = AppDatabase.VERSION
)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val VERSION = 1
    }

    abstract fun locationsDao(): LocationDao
}