/*
 * Copyright 2024 Cerdita App
 * 
 * Entidades de Base de Datos para Features Románticos
 * Implementación con Room para persistencia eficiente
 */

package im.vector.app.features.romantic.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDateTime

/**
 * Entidad para Notas de Amor
 */
@Entity(tableName = "love_notes")
data class LoveNoteEntity(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val noteType: String, // QUESTION, CHALLENGE, MESSAGE, etc.
    val createdAt: Long,
    val scheduledFor: Long?,
    val isLocked: Boolean,
    val lockHint: String?,
    val isRead: Boolean,
    val attachmentCount: Int,
    val senderId: String,
    val recipientId: String
)

/**
 * Entidad para Contador de Amor (Besos/Abrazos)
 */
@Entity(tableName = "affection_stats")
data class AffectionStatsEntity(
    @PrimaryKey val id: String = "stats",
    val kissesSent: Int = 0,
    val kissesReceived: Int = 0,
    val hugsSent: Int = 0,
    val hugsReceived: Int = 0,
    val lastKissDate: Long?,
    val lastHugDate: Long?,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalLovePoints: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
)

/**
 * Entidad para Logros Desbloqueados
 */
@Entity(tableName = "love_achievements")
data class LoveAchievementEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean,
    val unlockedAt: Long?,
    val progress: Int,
    val target: Int
)

/**
 * Entidad para Hitos de Relación
 */
@Entity(tableName = "relationship_milestones")
data class RelationshipMilestoneEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val milestoneType: String,
    val date: Long,
    val icon: String,
    val isCustom: Boolean,
    val photos: List<String>,
    val notes: String?
)

/**
 * Entidad para Configuración de Temas
 */
@Entity(tableName = "romantic_themes")
data class RomanticThemeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String, // MASCOT, FLOWER, SEASON
    val primaryColor: String,
    val secondaryColor: String,
    val accentColor: String,
    val iconPack: String,
    val soundPack: String,
    val isActive: Boolean,
    val isPremium: Boolean,
    val isUnlocked: Boolean
)

/**
 * Entidad para Mascotas (Cerdita & Koalita)
 */
@Entity(tableName = "mascots")
data class MascotEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String, // CERDITA, KOALITA
    val level: Int = 1,
    val experience: Int = 0,
    val mood: String = "HAPPY",
    val outfit: String?,
    val accessories: List<String>,
    val unlockedAt: Long,
    val lastInteraction: Long?
)

/**
 * Entidad para Mensajes Programados
 */
@Entity(tableName = "scheduled_messages")
data class ScheduledMessageEntity(
    @PrimaryKey val id: String,
    val content: String,
    val messageType: String, // TEXT, IMAGE, AUDIO, VIDEO
    val scheduledFor: Long,
    val isSent: Boolean,
    val sentAt: Long?,
    val recipientId: String,
    val retryCount: Int = 0
)

/**
 * Entidad para Cápsula del Tiempo
 */
@Entity(tableName = "time_capsules")
data class TimeCapsuleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val mediaUrls: List<String>,
    val openDate: Long,
    val isOpened: Boolean,
    val openedAt: Long?,
    val createdAt: Long,
    val createdBy: String
)

/**
 * Entidad para Bucket List de Pareja
 */
@Entity(tableName = "bucket_list_items")
data class BucketListItemEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val category: String, // TRAVEL, ACTIVITY, GOAL, EXPERIENCE
    val priority: Int,
    val isCompleted: Boolean,
    val completedAt: Long?,
    val notes: String?,
    val photos: List<String>
)

/**
 * Entidad para Calendario de Citas
 */
@Entity(tableName = "date_calendar")
data class DateCalendarEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val date: Long,
    val duration: Int, // minutos
    val location: String?,
    val type: String, // DINNER, MOVIE, WALK, SURPRISE
    val isCompleted: Boolean,
    val rating: Int?, // 1-5
    val notes: String?,
    val photos: List<String>
)

/**
 * Entidad para Playlist de Pareja
 */
@Entity(tableName = "couple_playlist")
data class CouplePlaylistEntity(
    @PrimaryKey val id: String,
    val songTitle: String,
    val artist: String,
    val album: String?,
    val duration: Int,
    val addedBy: String,
    val addedAt: Long,
    val playCount: Int = 0,
    val isFavorite: Boolean,
    val memories: String? // Por qué es especial esta canción
)

/**
 * Entidad para Votos/Promesas de Pareja
 */
@Entity(tableName = "love_vows")
data class LoveVowEntity(
    @PrimaryKey val id: String,
    val title: String,
    val promise: String,
    val category: String, // DAILY, WEEKLY, MONTHLY, LIFETIME
    val isRecurring: Boolean,
    val frequency: String?, // DAILY, WEEKLY, MONTHLY
    val isCompleted: Boolean,
    val completedCount: Int = 0,
    val createdAt: Long,
    val createdBy: String
)

/**
 * Entidad para lbum de Fotos
 */
@Entity(tableName = "photo_albums")
data class PhotoAlbumEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val coverPhoto: String,
    val createdAt: Long,
    val photoCount: Int,
    val isPrivate: Boolean,
    val tags: List<String>
)

/**
 * Entidad para Fotos del lbum
 */
@Entity(tableName = "album_photos")
data class AlbumPhotoEntity(
    @PrimaryKey val id: String,
    val albumId: String,
    val photoUrl: String,
    val thumbnailUrl: String,
    val caption: String?,
    val takenAt: Long?,
    val uploadedAt: Long,
    val isFavorite: Boolean,
    val tags: List<String>
)

/**
 * Entidad para Mapa de Lugares Especiales
 */
@Entity(tableName = "special_places")
data class SpecialPlaceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val latitude: Double,
    val longitude: Double,
    val placeType: String, // FIRST_DATE, FIRST_KISS, PROPOSAL, etc.
    val visitedAt: Long,
    val addedAt: Long,
    val photos: List<String>,
    val notes: String?,
    val isFavorite: Boolean
)

/**
 * Entidad para Contratos de Pareja
 */
@Entity(tableName = "couple_contracts")
data class CoupleContractEntity(
    @PrimaryKey val id: String,
    val title: String,
    val terms: List<String>,
    val startDate: Long,
    val endDate: Long?,
    val isActive: Boolean,
    val signedByBoth: Boolean,
    val createdAt: Long,
    val createdBy: String
)

/**
 * Entidad para Certificados de Amor
 */
@Entity(tableName = "love_certificates")
data class LoveCertificateEntity(
    @PrimaryKey val id: String,
    val certificateType: String, // ANNIVERSARY, BIRTHDAY, ACHIEVEMENT
    val title: String,
    val description: String,
    val issuedTo: String,
    val issuedBy: String,
    val issuedAt: Long,
    val validUntil: Long?,
    val certificateUrl: String?,
    val isDigital: Boolean
)

/**
 * Entidad para Recordatorios Románticos
 */
@Entity(tableName = "romantic_reminders")
data class RomanticReminderEntity(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val reminderType: String, // DAILY, WEEKLY, SPECIAL_DATE
    val scheduledTime: String, // HH:mm
    val daysOfWeek: List<Int>?, // 1-7 (Lunes-Domingo)
    val specialDate: Long?,
    val isActive: Boolean,
    val lastTriggered: Long?
)

/**
 * Entidad para Estadísticas de Uso Romántico
 */
@Entity(tableName = "romantic_usage_stats")
data class RomanticUsageStatsEntity(
    @PrimaryKey val id: String = "stats",
    val totalMessagesSent: Int = 0,
    val totalLoveNotesCreated: Int = 0,
    val totalHugsSent: Int = 0,
    val totalKissesSent: Int = 0,
    val totalGamesPlayed: Int = 0,
    val totalAchievementsUnlocked: Int = 0,
    val totalDaysTogether: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
)
