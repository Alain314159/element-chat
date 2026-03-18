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
        DailyLoveStatsEntity::class,
        // Mejoras de features románticos (Sección 10)
        HugReactionEntity::class,
        MascotOutfitEntity::class,
        MascotInventoryEntity::class,
        CustomMilestoneEntity::class,
        CustomCouponEntity::class,
        CouponRedemptionHistoryEntity::class,
        LocationTriggerEntity::class,
        CollaborativeNoteEntity::class,
        NoteContributionEntity::class
    ],
    version = 4,
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
 * Migración de versión 3 a 4 - Mejoras de Features Románticos (Sección 10)
 * Añade: reacciones a abrazos, outfits de mascotas, hitos personalizables,
 * cupones personalizados, y notas con ubicación
 */
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Tabla para reacciones a abrazos
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `hug_reactions` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `hugId` INTEGER NOT NULL,
                `reactionType` TEXT NOT NULL,
                `userId` TEXT NOT NULL,
                `timestamp` INTEGER NOT NULL,
                `isNotified` INTEGER NOT NULL DEFAULT 0
            )
        """)

        // Tabla para outfits de mascotas
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `mascot_outfits` (
                `id` TEXT NOT NULL,
                `name` TEXT NOT NULL,
                `type` TEXT NOT NULL,
                `rarity` TEXT NOT NULL,
                `unlockCondition` TEXT NOT NULL,
                `unlockRequirement` INTEGER NOT NULL DEFAULT 0,
                `coinCost` INTEGER NOT NULL DEFAULT 0,
                `description` TEXT,
                `iconUrl` TEXT,
                `isUnlocked` INTEGER NOT NULL DEFAULT 0,
                `unlockedAt` INTEGER,
                PRIMARY KEY(`id`)
            )
        """)

        // Tabla para inventario de mascotas
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `mascot_inventory` (
                `id` TEXT NOT NULL,
                `mascotId` TEXT NOT NULL,
                `ownedOutfitIds` TEXT NOT NULL DEFAULT '',
                `equippedOutfitId` TEXT,
                `lastUpdated` INTEGER NOT NULL DEFAULT 0,
                PRIMARY KEY(`id`)
            )
        """)

        // Tabla para hitos personalizados
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `custom_milestones` (
                `id` TEXT NOT NULL,
                `title` TEXT NOT NULL,
                `description` TEXT NOT NULL,
                `date` INTEGER NOT NULL,
                `photoUri` TEXT,
                `audioNoteUri` TEXT,
                `tags` TEXT NOT NULL DEFAULT '',
                `isCustom` INTEGER NOT NULL DEFAULT 1,
                `createdBy` TEXT NOT NULL,
                `createdAt` INTEGER NOT NULL DEFAULT 0,
                `isSynced` INTEGER NOT NULL DEFAULT 0,
                `syncedAt` INTEGER,
                PRIMARY KEY(`id`)
            )
        """)

        // Tabla para cupones personalizados
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `custom_coupons` (
                `id` TEXT NOT NULL,
                `title` TEXT NOT NULL,
                `description` TEXT NOT NULL,
                `conditions` TEXT NOT NULL DEFAULT '',
                `expirationDate` INTEGER,
                `mediaAttachments` TEXT NOT NULL DEFAULT '',
                `redemptionCount` INTEGER NOT NULL DEFAULT 1,
                `maxRedemptions` INTEGER NOT NULL DEFAULT 1,
                `isTransferable` INTEGER NOT NULL DEFAULT 1,
                `createdBy` TEXT NOT NULL,
                `createdAt` INTEGER NOT NULL DEFAULT 0,
                `isRedeemed` INTEGER NOT NULL DEFAULT 0,
                `redeemedAt` INTEGER,
                `redeemedBy` TEXT,
                PRIMARY KEY(`id`)
            )
        """)

        // Tabla para historial de redención de cupones
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `coupon_redemption_history` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `couponId` TEXT NOT NULL,
                `couponTitle` TEXT NOT NULL,
                `redeemedBy` TEXT NOT NULL,
                `redeemedAt` INTEGER NOT NULL DEFAULT 0,
                `notes` TEXT,
                `photoUri` TEXT
            )
        """)

        // Tabla para triggers de ubicación
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `location_triggers` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `noteId` TEXT NOT NULL,
                `latitude` REAL NOT NULL,
                `longitude` REAL NOT NULL,
                `radiusMeters` REAL NOT NULL,
                `triggerOnce` INTEGER NOT NULL DEFAULT 1,
                `isTriggered` INTEGER NOT NULL DEFAULT 0,
                `triggeredAt` INTEGER,
                `locationName` TEXT
            )
        """)

        // Tabla para notas colaborativas
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `collaborative_notes` (
                `id` TEXT NOT NULL,
                `title` TEXT NOT NULL,
                `isLocked` INTEGER NOT NULL DEFAULT 0,
                `unlockCondition` TEXT,
                `createdAt` INTEGER NOT NULL DEFAULT 0,
                `createdBy` TEXT NOT NULL,
                `lastModified` INTEGER NOT NULL DEFAULT 0,
                PRIMARY KEY(`id`)
            )
        """)

        // Tabla para contribuciones a notas colaborativas
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `note_contributions` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `noteId` TEXT NOT NULL,
                `authorId` TEXT NOT NULL,
                `content` TEXT NOT NULL,
                `mediaAttachments` TEXT NOT NULL DEFAULT '',
                `timestamp` INTEGER NOT NULL DEFAULT 0,
                `order` INTEGER NOT NULL DEFAULT 0
            )
        """)

        // Insertar outfits iniciales (comunes por defecto)
        database.execSQL("""
            INSERT INTO mascot_outfits (id, name, type, rarity, unlockCondition, unlockRequirement, coinCost, description, isUnlocked, unlockedAt)
            VALUES 
                ('outfit_default', 'Outfit Default', 'FULL', 'COMMON', 'DEFAULT', 0, 0, 'Outfit predeterminado de la mascota', 1, strftime('%s', 'now') * 1000),
                ('hat_basic', 'Gorro Básico', 'HAT', 'COMMON', 'LEVEL', 5, 0, 'Un gorro sencillo para tu mascota', 0, NULL),
                ('shirt_love', 'Camisa de Amor', 'SHIRT', 'RARE', 'XP', 100, 50, 'Camisa con corazones', 0, NULL)
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
