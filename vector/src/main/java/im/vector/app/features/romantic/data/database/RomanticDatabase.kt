/*
 * Copyright 2024 Cerdita App
 * 
 * Base de Datos Room para Features Románticos
 * Optimizada con WAL mode, auto-vacuum y migraciones
 */

package im.vector.app.features.romantic.data.database

import androidx.room.*
import im.vector.app.features.romantic.data.dao.*
import java.time.LocalDateTime

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
        RomanticUsageStatsEntity::class,
        // Nuevas entidades para features románticos
        KissEntity::class,
        HugEntity::class,
        RelationshipSettingsEntity::class,
        RomanticGiftEntity::class,
        LoveMessageEntity::class,
        RomanticDateIdeaEntity::class,
        RomanticChallengeEntity::class,
        // Estadísticas de amor
        LoveStatisticsEntity::class,
        RomanticMessageStatsEntity::class,
        SpecialDateEntity::class,
        DailyLoveStatsEntity::class
    ],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class RomanticDatabase : RoomDatabase() {

    // Único DAO existente - todos los demás fueron eliminados porque no existen
    abstract fun romanticDao(): RomanticDao

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
        // Crear nuevas tablas para features románticos
        
        // Tabla para besos
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `kisses` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `timestamp` INTEGER NOT NULL,
                `senderId` TEXT NOT NULL,
                `recipientId` TEXT NOT NULL,
                `hugType` TEXT NOT NULL DEFAULT 'normal'
            )
        """)
        
        // Tabla para abrazos
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `hugs` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `timestamp` INTEGER NOT NULL,
                `senderId` TEXT NOT NULL,
                `recipientId` TEXT NOT NULL,
                `hugType` TEXT NOT NULL DEFAULT 'normal'
            )
        """)
        
        // Tabla para configuración de relación
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `relationship_settings` (
                `id` TEXT NOT NULL,
                `timestamp` INTEGER NOT NULL,
                `partnerName` TEXT,
                `partnerUserId` TEXT,
                PRIMARY KEY(`id`)
            )
        """)
        
        // Tabla para regalos románticos
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `romantic_gifts` (
                `id` TEXT NOT NULL,
                `giftType` TEXT NOT NULL,
                `giftName` TEXT NOT NULL,
                `message` TEXT,
                `senderId` TEXT NOT NULL,
                `recipientId` TEXT NOT NULL,
                `sentAt` INTEGER NOT NULL,
                `isAccepted` INTEGER NOT NULL DEFAULT 0,
                `acceptedAt` INTEGER,
                PRIMARY KEY(`id`)
            )
        """)
        
        // Tabla para mensajes de amor
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `love_messages` (
                `id` TEXT NOT NULL,
                `content` TEXT NOT NULL,
                `messageType` TEXT NOT NULL,
                `senderId` TEXT NOT NULL,
                `recipientId` TEXT NOT NULL,
                `createdAt` INTEGER NOT NULL,
                `isRead` INTEGER NOT NULL DEFAULT 0,
                `readAt` INTEGER,
                PRIMARY KEY(`id`)
            )
        """)
        
        // Tabla para ideas de citas románticas
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `romantic_date_ideas` (
                `id` TEXT NOT NULL,
                `title` TEXT NOT NULL,
                `description` TEXT NOT NULL,
                `category` TEXT NOT NULL,
                `estimatedCost` INTEGER NOT NULL DEFAULT 0,
                `duration` INTEGER NOT NULL DEFAULT 60,
                `isCompleted` INTEGER NOT NULL DEFAULT 0,
                `completedAt` INTEGER,
                `rating` INTEGER,
                `notes` TEXT,
                `createdAt` INTEGER NOT NULL,
                PRIMARY KEY(`id`)
            )
        """)
        
        // Tabla para desafíos de pareja
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `romantic_challenges` (
                `id` TEXT NOT NULL,
                `title` TEXT NOT NULL,
                `description` TEXT NOT NULL,
                `challengeType` TEXT NOT NULL,
                `difficulty` INTEGER NOT NULL DEFAULT 1,
                `lovePoints` INTEGER NOT NULL DEFAULT 10,
                `isCompleted` INTEGER NOT NULL DEFAULT 0,
                `completedAt` INTEGER,
                `expiresAt` INTEGER,
                `createdAt` INTEGER NOT NULL,
                PRIMARY KEY(`id`)
            )
        """)
    }
}

/**
 * Migración de versión 2 a 3 - Estadísticas de Amor
 */
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Tabla para estadísticas de amor
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `love_statistics` (
                `id` TEXT NOT NULL,
                `totalMessagesSent` INTEGER NOT NULL DEFAULT 0,
                `totalMessagesReceived` INTEGER NOT NULL DEFAULT 0,
                `totalHugsSent` INTEGER NOT NULL DEFAULT 0,
                `totalKissesSent` INTEGER NOT NULL DEFAULT 0,
                `totalNotesCreated` INTEGER NOT NULL DEFAULT 0,
                `totalPhotosShared` INTEGER NOT NULL DEFAULT 0,
                `totalAudiosSent` INTEGER NOT NULL DEFAULT 0,
                `totalVideosSent` INTEGER NOT NULL DEFAULT 0,
                `totalLinks` INTEGER NOT NULL DEFAULT 0,
                `totalStickers` INTEGER NOT NULL DEFAULT 0,
                `longestStreak` INTEGER NOT NULL DEFAULT 0,
                `currentStreak` INTEGER NOT NULL DEFAULT 0,
                `streakLastUpdated` INTEGER,
                `relationshipStartDate` INTEGER,
                `lastUpdated` INTEGER NOT NULL DEFAULT 0,
                PRIMARY KEY(`id`)
            )
        """)

        // Tabla para tracking de mensajes románticos
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `romantic_message_stats` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `content` TEXT NOT NULL,
                `messageType` TEXT NOT NULL,
                `senderId` TEXT NOT NULL,
                `recipientId` TEXT NOT NULL,
                `timestamp` INTEGER NOT NULL DEFAULT 0,
                `romanticWordsDetected` TEXT NOT NULL DEFAULT ''
            )
        """)

        // Tabla para fechas especiales
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `special_dates` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `title` TEXT NOT NULL,
                `dateTimestamp` INTEGER NOT NULL,
                `type` TEXT NOT NULL,
                `createdAt` INTEGER NOT NULL DEFAULT 0
            )
        """)

        // Tabla para estadísticas diarias
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `daily_love_stats` (
                `date` TEXT NOT NULL,
                `messagesSent` INTEGER NOT NULL DEFAULT 0,
                `messagesReceived` INTEGER NOT NULL DEFAULT 0,
                `hugsSent` INTEGER NOT NULL DEFAULT 0,
                `kissesSent` INTEGER NOT NULL DEFAULT 0,
                `notesCreated` INTEGER NOT NULL DEFAULT 0,
                `photosShared` INTEGER NOT NULL DEFAULT 0,
                `lastUpdated` INTEGER NOT NULL DEFAULT 0,
                PRIMARY KEY(`date`)
            )
        """)

        // Insertar estadísticas iniciales
        database.execSQL("INSERT INTO love_statistics (id, lastUpdated) VALUES ('stats', strftime('%s', 'now') * 1000)")
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
