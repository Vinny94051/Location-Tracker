package ru.kiryanov.locationtracker.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.kiryanov.locationtracker.data.database.entities.LocationEntity

@Dao
interface LocationDao {

    @Query("SELECT * FROM location_points")
    fun getAll(): List<LocationEntity>

    @Insert
    fun insertAll(vararg users: LocationEntity)

    @Delete
    fun delete(user: LocationEntity)
}