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
        RomanticChallengeEntity::class
    ],
    version = 2,
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
