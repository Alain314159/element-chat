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
import im.vector.app.features.romantic.data.database.MIGRATION_1_2
import im.vector.app.features.romantic.data.database.MIGRATION_2_3
import im.vector.app.features.romantic.data.database.MIGRATION_3_4
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
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
            .fallbackToDestructiveMigration()
            .build()
    }

    // Único DAO proporcionado - todos los demás fueron eliminados porque no existen
    @Provides
    @Singleton
    fun provideRomanticDao(database: RomanticDatabase) = database.romanticDao()
}
