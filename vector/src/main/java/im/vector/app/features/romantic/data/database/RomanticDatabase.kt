/*
 * Copyright 2024 Cerdita App
 * 
 * Base de Datos Room para Features Románticos
 * Optimizada con WAL mode, auto-vacuum y migraciones
 */

package im.vector.app.features.romantic.data.database

import androidx.room.*
import im.vector.app.features.romantic.data.dao.*

@Database(
    entities = [
        LoveNoteEntity::class,
        AffectionStatsEntity::class,
        LoveAchievementEntity::class,
        RelationshipMilestoneEntity::class,
        RomanticThemeEntity::class,
        MascotEntity::class,
        ScheduledMessageEntity::class,
        TimeCapsuleEntity::class,
        BucketListItemEntity::class,
        DateCalendarEntity::class,
        CouplePlaylistEntity::class,
        LoveVowEntity::class,
        PhotoAlbumEntity::class,
        AlbumPhotoEntity::class,
        SpecialPlaceEntity::class,
        CoupleContractEntity::class,
        LoveCertificateEntity::class,
        RomanticReminderEntity::class,
        RomanticUsageStatsEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class RomanticDatabase : RoomDatabase() {
    
    abstract fun loveNotesDao(): LoveNotesDao
    abstract fun affectionStatsDao(): AffectionStatsDao
    abstract fun loveAchievementsDao(): LoveAchievementsDao
    abstract fun relationshipMilestonesDao(): RelationshipMilestonesDao
    abstract fun romanticThemesDao(): RomanticThemesDao
    abstract fun scheduledMessagesDao(): ScheduledMessagesDao
    abstract fun timeCapsulesDao(): TimeCapsulesDao
    abstract fun bucketListDao(): BucketListDao
    abstract fun dateCalendarDao(): DateCalendarDao
    abstract fun couplePlaylistDao(): CouplePlaylistDao
    abstract fun loveVowsDao(): LoveVowsDao
    abstract fun photoAlbumsDao(): PhotoAlbumsDao
    abstract fun albumPhotosDao(): AlbumPhotosDao
    abstract fun specialPlacesDao(): SpecialPlacesDao
    abstract fun romanticRemindersDao(): RomanticRemindersDao
    
    companion object {
        const val DATABASE_NAME = "cerdita_romantic_db"
    }
}

/**
 * Type Converters para tipos personalizados
 */
class Converters {
    
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString("|")
    }
    
    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isEmpty()) emptyList() else value.split("|")
    }
    
    @TypeConverter
    fun fromLongList(value: List<Long>): String {
        return value.joinToString(",")
    }
    
    @TypeConverter
    fun toLongList(value: String): List<Long> {
        return if (value.isEmpty()) emptyList() else value.split(",").map { it.toLong() }
    }
    
    @TypeConverter
    fun fromIntList(value: List<Int>): String {
        return value.joinToString(",")
    }
    
    @TypeConverter
    fun toIntList(value: String): List<Int> {
        return if (value.isEmpty()) emptyList() else value.split(",").map { it.toInt() }
    }
    
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): Long? {
        return value?.atZone(java.time.ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }
    
    @TypeConverter
    fun toLocalDateTime(value: Long?): LocalDateTime? {
        return value?.let { 
            java.time.Instant.ofEpochMilli(it)
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime() 
        }
    }
}

/**
 * Migraciones para actualizaciones de base de datos
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Ejemplo: Añadir nueva columna
        // database.execSQL("ALTER TABLE love_notes ADD COLUMN new_column TEXT DEFAULT NULL")
    }
}

/**
 * Callback para eventos de la base de datos
 */
val ROMANTIC_DATABASE_CALLBACK = object : RoomDatabase.Callback() {
    
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // Datos iniciales
        db.execSQL("INSERT INTO affection_stats (id, kissesSent, hugsSent, totalLovePoints) VALUES ('stats', 0, 0, 0)")
        db.execSQL("INSERT INTO romantic_usage_stats (id) VALUES ('stats')")
    }
    
    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        // Habilitar WAL mode para mejor concurrencia
        db.execSQL("PRAGMA journal_mode=WAL")
        // Habilitar auto-vacuum
        db.execSQL("PRAGMA auto_vacuum=INCREMENTAL")
        // Aumentar cache size
        db.execSQL("PRAGMA cache_size=-6400") // 6MB cache
    }
}
