/*
 * Copyright 2024 Cerdita App
 * 
 * Data Access Objects para Features Románticos
 * Optimizados con suspend functions y Flow
 */

package im.vector.app.features.romantic.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import im.vector.app.features.romantic.data.database.*

/**
 * DAO para Notas de Amor
 */
@Dao
interface LoveNotesDao {
    
    @Query("SELECT * FROM love_notes ORDER BY createdAt DESC")
    fun getAllNotes(): Flow<List<LoveNoteEntity>>
    
    @Query("SELECT * FROM love_notes WHERE isRead = 0 ORDER BY createdAt DESC")
    fun getUnreadNotes(): Flow<List<LoveNoteEntity>>
    
    @Query("SELECT * FROM love_notes WHERE scheduledFor <= :currentTime AND scheduledFor IS NOT NULL")
    fun getNotesDue(currentTime: Long): List<LoveNoteEntity>
    
    @Query("SELECT * FROM love_notes WHERE id = :id")
    suspend fun getNoteById(id: String): LoveNoteEntity?
    
    @Query("SELECT * FROM love_notes WHERE noteType = :type ORDER BY createdAt DESC")
    fun getNotesByType(type: String): Flow<List<LoveNoteEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: LoveNoteEntity)
    
    @Update
    suspend fun updateNote(note: LoveNoteEntity)
    
    @Delete
    suspend fun deleteNote(note: LoveNoteEntity)
    
    @Query("DELETE FROM love_notes WHERE id = :id")
    suspend fun deleteNoteById(id: String)
    
    @Query("UPDATE love_notes SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)
    
    @Query("SELECT COUNT(*) FROM love_notes WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>
    
    @Query("SELECT * FROM love_notes WHERE senderId = :senderId ORDER BY createdAt DESC")
    fun getNotesBySender(senderId: String): Flow<List<LoveNoteEntity>>
}

/**
 * DAO para Estadísticas de Amor
 */
@Dao
interface AffectionStatsDao {
    
    @Query("SELECT * FROM affection_stats WHERE id = 'stats'")
    fun getStats(): Flow<AffectionStatsEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStats(stats: AffectionStatsEntity)
    
    @Query("UPDATE affection_stats SET kissesSent = kissesSent + 1, lastKissDate = :timestamp, totalLovePoints = totalLovePoints + 10 WHERE id = 'stats'")
    suspend fun incrementKissSent(timestamp: Long)
    
    @Query("UPDATE affection_stats SET hugsSent = hugsSent + 1, lastHugDate = :timestamp, totalLovePoints = totalLovePoints + 10 WHERE id = 'stats'")
    suspend fun incrementHugSent(timestamp: Long)
    
    @Query("UPDATE affection_stats SET kissesReceived = kissesReceived + 1, totalLovePoints = totalLovePoints + 10 WHERE id = 'stats'")
    suspend fun incrementKissReceived()
    
    @Query("UPDATE affection_stats SET hugsReceived = hugsReceived + 1, totalLovePoints = totalLovePoints + 10 WHERE id = 'stats'")
    suspend fun incrementHugReceived()
    
    @Query("UPDATE affection_stats SET currentStreak = currentStreak + 1 WHERE id = 'stats'")
    suspend fun incrementStreak()
    
    @Query("UPDATE affection_stats SET currentStreak = 0 WHERE id = 'stats'")
    suspend fun resetStreak()
    
    @Query("SELECT totalLovePoints FROM affection_stats WHERE id = 'stats'")
    fun getTotalLovePoints(): Flow<Int>
}

/**
 * DAO para Logros
 */
@Dao
interface LoveAchievementsDao {
    
    @Query("SELECT * FROM love_achievements ORDER BY isUnlocked DESC, title ASC")
    fun getAllAchievements(): Flow<List<LoveAchievementEntity>>
    
    @Query("SELECT * FROM love_achievements WHERE isUnlocked = 1")
    fun getUnlockedAchievements(): Flow<List<LoveAchievementEntity>>
    
    @Query("SELECT * FROM love_achievements WHERE id = :id")
    suspend fun getAchievementById(id: String): LoveAchievementEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievement(achievement: LoveAchievementEntity)
    
    @Update
    suspend fun updateAchievement(achievement: LoveAchievementEntity)
    
    @Query("UPDATE love_achievements SET isUnlocked = 1, unlockedAt = :timestamp WHERE id = :id")
    suspend fun unlockAchievement(id: String, timestamp: Long)
    
    @Query("UPDATE love_achievements SET progress = :progress WHERE id = :id")
    suspend fun updateProgress(id: String, progress: Int)
    
    @Query("SELECT COUNT(*) FROM love_achievements WHERE isUnlocked = 1")
    fun getUnlockedCount(): Flow<Int>
}

/**
 * DAO para Hitos de Relación
 */
@Dao
interface RelationshipMilestonesDao {
    
    @Query("SELECT * FROM relationship_milestones ORDER BY date ASC")
    fun getAllMilestones(): Flow<List<RelationshipMilestoneEntity>>
    
    @Query("SELECT * FROM relationship_milestones WHERE milestoneType = :type ORDER BY date ASC")
    fun getMilestonesByType(type: String): Flow<List<RelationshipMilestoneEntity>>
    
    @Query("SELECT * FROM relationship_milestones WHERE id = :id")
    suspend fun getMilestoneById(id: String): RelationshipMilestoneEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestone(milestone: RelationshipMilestoneEntity)
    
    @Update
    suspend fun updateMilestone(milestone: RelationshipMilestoneEntity)
    
    @Delete
    suspend fun deleteMilestone(milestone: RelationshipMilestoneEntity)
    
    @Query("SELECT COUNT(*) FROM relationship_milestones")
    fun getMilestonesCount(): Flow<Int>
}

/**
 * DAO para Temas Románticos
 */
@Dao
interface RomanticThemesDao {
    
    @Query("SELECT * FROM romantic_themes ORDER BY name ASC")
    fun getAllThemes(): Flow<List<RomanticThemeEntity>>
    
    @Query("SELECT * FROM romantic_themes WHERE isActive = 1 LIMIT 1")
    fun getActiveTheme(): Flow<RomanticThemeEntity?>
    
    @Query("SELECT * FROM romantic_themes WHERE category = :category ORDER BY name ASC")
    fun getThemesByCategory(category: String): Flow<List<RomanticThemeEntity>>
    
    @Query("SELECT * FROM romantic_themes WHERE id = :id")
    suspend fun getThemeById(id: String): RomanticThemeEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTheme(theme: RomanticThemeEntity)
    
    @Update
    suspend fun updateTheme(theme: RomanticThemeEntity)
    
    @Query("UPDATE romantic_themes SET isActive = 0 WHERE isActive = 1")
    suspend fun deactivateAllThemes()
    
    @Query("UPDATE romantic_themes SET isActive = 1 WHERE id = :id")
    suspend fun activateTheme(id: String)
    
    @Query("SELECT COUNT(*) FROM romantic_themes WHERE isUnlocked = 1")
    fun getUnlockedThemesCount(): Flow<Int>
}

/**
 * DAO para Mensajes Programados
 */
@Dao
interface ScheduledMessagesDao {
    
    @Query("SELECT * FROM scheduled_messages WHERE isSent = 0 AND scheduledFor <= :currentTime ORDER BY scheduledFor ASC")
    fun getPendingMessages(currentTime: Long): Flow<List<ScheduledMessageEntity>>
    
    @Query("SELECT * FROM scheduled_messages WHERE scheduledFor > :currentTime ORDER BY scheduledFor ASC")
    fun getUpcomingMessages(currentTime: Long): Flow<List<ScheduledMessageEntity>>
    
    @Query("SELECT * FROM scheduled_messages WHERE id = :id")
    suspend fun getMessageById(id: String): ScheduledMessageEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ScheduledMessageEntity)
    
    @Update
    suspend fun updateMessage(message: ScheduledMessageEntity)
    
    @Delete
    suspend fun deleteMessage(message: ScheduledMessageEntity)
    
    @Query("UPDATE scheduled_messages SET isSent = 1, sentAt = :timestamp WHERE id = :id")
    suspend fun markAsSent(id: String, timestamp: Long)
    
    @Query("UPDATE scheduled_messages SET retryCount = retryCount + 1 WHERE id = :id")
    suspend fun incrementRetryCount(id: String)
    
    @Query("SELECT COUNT(*) FROM scheduled_messages WHERE isSent = 0")
    fun getPendingCount(): Flow<Int>
}

/**
 * DAO para Cápsulas del Tiempo
 */
@Dao
interface TimeCapsulesDao {
    
    @Query("SELECT * FROM time_capsules ORDER BY openDate ASC")
    fun getAllCapsules(): Flow<List<TimeCapsuleEntity>>
    
    @Query("SELECT * FROM time_capsules WHERE isOpened = 0 AND openDate <= :currentTime ORDER BY openDate ASC")
    fun getAvailableCapsules(currentTime: Long): Flow<List<TimeCapsuleEntity>>
    
    @Query("SELECT * FROM time_capsules WHERE isOpened = 1 ORDER BY openedAt DESC")
    fun getOpenedCapsules(): Flow<List<TimeCapsuleEntity>>
    
    @Query("SELECT * FROM time_capsules WHERE id = :id")
    suspend fun getCapsuleById(id: String): TimeCapsuleEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCapsule(capsule: TimeCapsuleEntity)
    
    @Update
    suspend fun updateCapsule(capsule: TimeCapsuleEntity)
    
    @Delete
    suspend fun deleteCapsule(capsule: TimeCapsuleEntity)
    
    @Query("UPDATE time_capsules SET isOpened = 1, openedAt = :timestamp WHERE id = :id")
    suspend fun openCapsule(id: String, timestamp: Long)
    
    @Query("SELECT COUNT(*) FROM time_capsules WHERE isOpened = 0")
    fun getAvailableCount(): Flow<Int>
}

/**
 * DAO para Bucket List
 */
@Dao
interface BucketListDao {
    
    @Query("SELECT * FROM bucket_list_items ORDER BY priority DESC, isCompleted ASC")
    fun getAllItems(): Flow<List<BucketListItemEntity>>
    
    @Query("SELECT * FROM bucket_list_items WHERE isCompleted = 0 ORDER BY priority DESC")
    fun getPendingItems(): Flow<List<BucketListItemEntity>>
    
    @Query("SELECT * FROM bucket_list_items WHERE isCompleted = 1 ORDER BY completedAt DESC")
    fun getCompletedItems(): Flow<List<BucketListItemEntity>>
    
    @Query("SELECT * FROM bucket_list_items WHERE id = :id")
    suspend fun getItemById(id: String): BucketListItemEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: BucketListItemEntity)
    
    @Update
    suspend fun updateItem(item: BucketListItemEntity)
    
    @Delete
    suspend fun deleteItem(item: BucketListItemEntity)
    
    @Query("UPDATE bucket_list_items SET isCompleted = 1, completedAt = :timestamp WHERE id = :id")
    suspend fun completeItem(id: String, timestamp: Long)
    
    @Query("SELECT COUNT(*) FROM bucket_list_items WHERE isCompleted = 1")
    fun getCompletedCount(): Flow<Int>
}

/**
 * DAO para Calendario de Citas
 */
@Dao
interface DateCalendarDao {
    
    @Query("SELECT * FROM date_calendar ORDER BY date ASC")
    fun getAllDates(): Flow<List<DateCalendarEntity>>
    
    @Query("SELECT * FROM date_calendar WHERE date >= :startTime AND date <= :endTime ORDER BY date ASC")
    fun getDatesInRange(startTime: Long, endTime: Long): Flow<List<DateCalendarEntity>>
    
    @Query("SELECT * FROM date_calendar WHERE isCompleted = 0 AND date >= :currentTime ORDER BY date ASC LIMIT 5")
    fun getUpcomingDates(currentTime: Long): Flow<List<DateCalendarEntity>>
    
    @Query("SELECT * FROM date_calendar WHERE id = :id")
    suspend fun getDateById(id: String): DateCalendarEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDate(date: DateCalendarEntity)
    
    @Update
    suspend fun updateDate(date: DateCalendarEntity)
    
    @Delete
    suspend fun deleteDate(date: DateCalendarEntity)
    
    @Query("UPDATE date_calendar SET isCompleted = 1, rating = :rating WHERE id = :id")
    suspend fun completeDate(id: String, rating: Int?)
    
    @Query("SELECT AVG(rating) FROM date_calendar WHERE rating IS NOT NULL")
    fun getAverageRating(): Flow<Double?>
}

/**
 * DAO para Playlist de Pareja
 */
@Dao
interface CouplePlaylistDao {
    
    @Query("SELECT * FROM couple_playlist ORDER BY addedAt DESC")
    fun getAllSongs(): Flow<List<CouplePlaylistEntity>>
    
    @Query("SELECT * FROM couple_playlist WHERE isFavorite = 1 ORDER BY addedAt DESC")
    fun getFavoriteSongs(): Flow<List<CouplePlaylistEntity>>
    
    @Query("SELECT * FROM couple_playlist WHERE addedBy = :userId ORDER BY addedAt DESC")
    fun getSongsByUser(userId: String): Flow<List<CouplePlaylistEntity>>
    
    @Query("SELECT * FROM couple_playlist WHERE id = :id")
    suspend fun getSongById(id: String): CouplePlaylistEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: CouplePlaylistEntity)
    
    @Update
    suspend fun updateSong(song: CouplePlaylistEntity)
    
    @Delete
    suspend fun deleteSong(song: CouplePlaylistEntity)
    
    @Query("UPDATE couple_playlist SET isFavorite = :favorite WHERE id = :id")
    suspend fun toggleFavorite(id: String, favorite: Boolean)
    
    @Query("UPDATE couple_playlist SET playCount = playCount + 1 WHERE id = :id")
    suspend fun incrementPlayCount(id: String)
    
    @Query("SELECT COUNT(*) FROM couple_playlist")
    fun getSongsCount(): Flow<Int>
}

/**
 * DAO para Votos de Amor
 */
@Dao
interface LoveVowsDao {
    
    @Query("SELECT * FROM love_vows ORDER BY createdAt DESC")
    fun getAllVows(): Flow<List<LoveVowEntity>>
    
    @Query("SELECT * FROM love_vows WHERE isRecurring = 1 AND isActive = 1 ORDER BY createdAt DESC")
    fun getRecurringVows(): Flow<List<LoveVowEntity>>
    
    @Query("SELECT * FROM love_vows WHERE createdBy = :userId ORDER BY createdAt DESC")
    fun getVowsByUser(userId: String): Flow<List<LoveVowEntity>>
    
    @Query("SELECT * FROM love_vows WHERE id = :id")
    suspend fun getVowById(id: String): LoveVowEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVow(vow: LoveVowEntity)
    
    @Update
    suspend fun updateVow(vow: LoveVowEntity)
    
    @Delete
    suspend fun deleteVow(vow: LoveVowEntity)
    
    @Query("UPDATE love_vows SET completedCount = completedCount + 1 WHERE id = :id")
    suspend fun incrementCompletedCount(id: String)
    
    @Query("SELECT SUM(completedCount) FROM love_vows")
    fun getTotalCompletedVows(): Flow<Int?>
}

/**
 * DAO para lbumes de Fotos
 */
@Dao
interface PhotoAlbumsDao {
    
    @Query("SELECT * FROM photo_albums ORDER BY createdAt DESC")
    fun getAllAlbums(): Flow<List<PhotoAlbumEntity>>
    
    @Query("SELECT * FROM photo_albums WHERE id = :id")
    suspend fun getAlbumById(id: String): PhotoAlbumEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: PhotoAlbumEntity)
    
    @Update
    suspend fun updateAlbum(album: PhotoAlbumEntity)
    
    @Delete
    suspend fun deleteAlbum(album: PhotoAlbumEntity)
    
    @Query("UPDATE photo_albums SET photoCount = photoCount + 1 WHERE id = :albumId")
    suspend fun incrementPhotoCount(albumId: String)
}

/**
 * DAO para Fotos del lbum
 */
@Dao
interface AlbumPhotosDao {
    
    @Query("SELECT * FROM album_photos WHERE albumId = :albumId ORDER BY uploadedAt DESC")
    fun getPhotosByAlbum(albumId: String): Flow<List<AlbumPhotoEntity>>
    
    @Query("SELECT * FROM album_photos WHERE isFavorite = 1 ORDER BY uploadedAt DESC")
    fun getFavoritePhotos(): Flow<List<AlbumPhotoEntity>>
    
    @Query("SELECT * FROM album_photos WHERE id = :id")
    suspend fun getPhotoById(id: String): AlbumPhotoEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: AlbumPhotoEntity)
    
    @Update
    suspend fun updatePhoto(photo: AlbumPhotoEntity)
    
    @Delete
    suspend fun deletePhoto(photo: AlbumPhotoEntity)
    
    @Query("UPDATE album_photos SET isFavorite = :favorite WHERE id = :id")
    suspend fun toggleFavorite(id: String, favorite: Boolean)
    
    @Query("DELETE FROM album_photos WHERE albumId = :albumId")
    suspend fun deleteAllPhotosFromAlbum(albumId: String)
}

/**
 * DAO para Lugares Especiales
 */
@Dao
interface SpecialPlacesDao {
    
    @Query("SELECT * FROM special_places ORDER BY visitedAt DESC")
    fun getAllPlaces(): Flow<List<SpecialPlaceEntity>>
    
    @Query("SELECT * FROM special_places WHERE placeType = :type ORDER BY visitedAt DESC")
    fun getPlacesByType(type: String): Flow<List<SpecialPlaceEntity>>
    
    @Query("SELECT * FROM special_places WHERE isFavorite = 1 ORDER BY visitedAt DESC")
    fun getFavoritePlaces(): Flow<List<SpecialPlaceEntity>>
    
    @Query("SELECT * FROM special_places WHERE id = :id")
    suspend fun getPlaceById(id: String): SpecialPlaceEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(place: SpecialPlaceEntity)
    
    @Update
    suspend fun updatePlace(place: SpecialPlaceEntity)
    
    @Delete
    suspend fun deletePlace(place: SpecialPlaceEntity)
    
    @Query("UPDATE special_places SET isFavorite = :favorite WHERE id = :id")
    suspend fun toggleFavorite(id: String, favorite: Boolean)
    
    @Query("SELECT COUNT(*) FROM special_places")
    fun getPlacesCount(): Flow<Int>
}

/**
 * DAO para Recordatorios Románticos
 */
@Dao
interface RomanticRemindersDao {
    
    @Query("SELECT * FROM romantic_reminders WHERE isActive = 1 ORDER BY reminderType, scheduledTime")
    fun getActiveReminders(): Flow<List<RomanticReminderEntity>>
    
    @Query("SELECT * FROM romantic_reminders WHERE id = :id")
    suspend fun getReminderById(id: String): RomanticReminderEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: RomanticReminderEntity)
    
    @Update
    suspend fun updateReminder(reminder: RomanticReminderEntity)
    
    @Delete
    suspend fun deleteReminder(reminder: RomanticReminderEntity)
    
    @Query("UPDATE romantic_reminders SET lastTriggered = :timestamp WHERE id = :id")
    suspend fun markAsTriggered(id: String, timestamp: Long)
    
    @Query("UPDATE romantic_reminders SET isActive = :active WHERE id = :id")
    suspend fun toggleActive(id: String, active: Boolean)
    
    @Query("SELECT COUNT(*) FROM romantic_reminders WHERE isActive = 1")
    fun getActiveCount(): Flow<Int>
}
