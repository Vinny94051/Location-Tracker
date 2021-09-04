package ru.kiryanov.locationtracker.data.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.kiryanov.locationtracker.data.database.AppDatabase
import ru.kiryanov.locationtracker.data.database.dao.LocationDao
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "location_points"
    ).build()

    @Provides
    fun provideLocationDao(db: AppDatabase) : LocationDao = db.locationsDao()
}