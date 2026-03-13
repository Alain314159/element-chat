/*
 * Copyright 2024 Cerdita App
 * 
 * Módulo de Dependency Injection para Features Románticos
 * Implementación con Hilt para inyección eficiente
 */

package im.vector.app.features.romantic.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import im.vector.app.features.romantic.data.database.RomanticDatabase
import im.vector.app.features.romantic.data.database.ROMANTIC_DATABASE_CALLBACK
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RomanticDatabaseModule {
    
    @Provides
    @Singleton
    fun provideRomanticDatabase(
        @ApplicationContext context: Context
    ): RomanticDatabase {
        return Room.databaseBuilder(
            context,
            RomanticDatabase::class.java,
            RomanticDatabase.DATABASE_NAME
        )
            .addCallback(ROMANTIC_DATABASE_CALLBACK)
            .addMigrations() // Añadir migraciones aquí
            .build()
    }
    
    @Provides
    @Singleton
    fun provideLoveNotesDao(database: RomanticDatabase) = database.loveNotesDao()
    
    @Provides
    @Singleton
    fun provideAffectionStatsDao(database: RomanticDatabase) = database.affectionStatsDao()
    
    @Provides
    @Singleton
    fun provideLoveAchievementsDao(database: RomanticDatabase) = database.loveAchievementsDao()
    
    @Provides
    @Singleton
    fun provideRelationshipMilestonesDao(database: RomanticDatabase) = database.relationshipMilestonesDao()
    
    @Provides
    @Singleton
    fun provideRomanticThemesDao(database: RomanticDatabase) = database.romanticThemesDao()
    
    @Provides
    @Singleton
    fun provideScheduledMessagesDao(database: RomanticDatabase) = database.scheduledMessagesDao()
    
    @Provides
    @Singleton
    fun provideTimeCapsulesDao(database: RomanticDatabase) = database.timeCapsulesDao()
    
    @Provides
    @Singleton
    fun provideBucketListDao(database: RomanticDatabase) = database.bucketListDao()
    
    @Provides
    @Singleton
    fun provideDateCalendarDao(database: RomanticDatabase) = database.dateCalendarDao()
    
    @Provides
    @Singleton
    fun provideCouplePlaylistDao(database: RomanticDatabase) = database.couplePlaylistDao()
    
    @Provides
    @Singleton
    fun provideLoveVowsDao(database: RomanticDatabase) = database.loveVowsDao()
    
    @Provides
    @Singleton
    fun providePhotoAlbumsDao(database: RomanticDatabase) = database.photoAlbumsDao()
    
    @Provides
    @Singleton
    fun provideAlbumPhotosDao(database: RomanticDatabase) = database.albumPhotosDao()
    
    @Provides
    @Singleton
    fun provideSpecialPlacesDao(database: RomanticDatabase) = database.specialPlacesDao()
    
    @Provides
    @Singleton
    fun provideRomanticRemindersDao(database: RomanticDatabase) = database.romanticRemindersDao()
}
