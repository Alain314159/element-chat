/*
 * Copyright (c) 2026 Element Chat Romántico
 *
 * DAO unificado para operaciones románticas principales
 */

package im.vector.app.features.romantic.data.dao

import androidx.room.*
import im.vector.app.features.romantic.data.database.*
import im.vector.app.features.romantic.stats.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO unificado para operaciones principales de datos románticos.
 * Incluye métodos para días de relación, estadísticas de cariño, y más.
 */
@Dao
interface RomanticDao {

    // ==================== DÍAS DE RELACIÓN ====================

    @Query("SELECT timestamp FROM relationship_settings WHERE id = 'start_date' LIMIT 1")
    suspend fun getRelationshipStartDate(): Long?

    @Query("INSERT OR REPLACE INTO relationship_settings (id, timestamp) VALUES ('start_date', :timestamp)")
    suspend fun setRelationshipStartDate(timestamp: Long)

    @Query("SELECT (julianday('now') - julianday(datetime(:timestamp/1000, 'unixepoch'))) as days FROM relationship_settings WHERE id = 'start_date' LIMIT 1")
    suspend fun getRelationshipDays(timestamp: Long): Long

    @Query("SELECT timestamp FROM relationship_settings WHERE id = 'start_date' LIMIT 1")
    fun getRelationshipDaysFlow(): Flow<Long?>

    // ==================== ESTADÍSTICAS DE CARIÑO ====================

    @Query("SELECT COUNT(*) FROM kisses")
    suspend fun getTotalKisses(): Int?

    @Query("SELECT COUNT(*) FROM hugs")
    suspend fun getTotalHugs(): Int?

    @Query("SELECT MAX(timestamp) FROM kisses")
    suspend fun getLastKissTimestamp(): Long?

    @Query("SELECT MAX(timestamp) FROM hugs")
    suspend fun getLastHugTimestamp(): Long?

    @Query("INSERT INTO kisses (timestamp) VALUES (:timestamp)")
    suspend fun insertKiss(timestamp: Long = System.currentTimeMillis())

    @Query("INSERT INTO hugs (timestamp) VALUES (:timestamp)")
    suspend fun insertHug(timestamp: Long = System.currentTimeMillis())

    @Query("SELECT * FROM kisses ORDER BY timestamp DESC LIMIT 10")
    fun getRecentKisses(): Flow<List<KissEntity>>

    @Query("SELECT * FROM hugs ORDER BY timestamp DESC LIMIT 10")
    fun getRecentHugs(): Flow<List<HugEntity>>

    // ==================== NOTAS DE AMOR ====================

    @Query("SELECT * FROM love_notes ORDER BY createdAt DESC")
    fun getAllLoveNotes(): Flow<List<LoveNoteEntity>>

    @Query("SELECT * FROM love_notes WHERE id = :id")
    suspend fun getLoveNoteById(id: String): LoveNoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoveNote(note: LoveNoteEntity)

    @Update
    suspend fun updateLoveNote(note: LoveNoteEntity)

    @Query("DELETE FROM love_notes WHERE id = :id")
    suspend fun deleteLoveNote(id: String)

    @Query("UPDATE love_notes SET isRead = 1 WHERE id = :id")
    suspend fun markLoveNoteAsRead(id: String)

    // ==================== REGALOS ROMÁNTICOS ====================

    @Query("SELECT * FROM romantic_gifts ORDER BY sentAt DESC")
    fun getAllRomanticGifts(): Flow<List<RomanticGiftEntity>>

    @Query("SELECT * FROM romantic_gifts WHERE id = :id")
    suspend fun getRomanticGiftById(id: String): RomanticGiftEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRomanticGift(gift: RomanticGiftEntity)

    // ==================== CÁPSULAS DEL TIEMPO ====================

    @Query("SELECT * FROM time_capsules ORDER BY openDate ASC")
    fun getAllTimeCapsules(): Flow<List<TimeCapsuleEntity>>

    @Query("SELECT * FROM time_capsules WHERE id = :id")
    suspend fun getTimeCapsuleById(id: String): TimeCapsuleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeCapsule(capsule: TimeCapsuleEntity)

    @Update
    suspend fun updateTimeCapsule(capsule: TimeCapsuleEntity)

    @Query("DELETE FROM time_capsules WHERE id = :id")
    suspend fun deleteTimeCapsule(id: String)

    // ==================== HITOS DE RELACIÓN ====================

    @Query("SELECT * FROM relationship_milestones ORDER BY date ASC")
    fun getAllRelationshipMilestones(): Flow<List<RelationshipMilestoneEntity>>

    @Query("SELECT * FROM relationship_milestones WHERE id = :id")
    suspend fun getRelationshipMilestoneById(id: String): RelationshipMilestoneEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRelationshipMilestone(milestone: RelationshipMilestoneEntity)

    @Update
    suspend fun updateRelationshipMilestone(milestone: RelationshipMilestoneEntity)

    @Query("DELETE FROM relationship_milestones WHERE id = :id")
    suspend fun deleteRelationshipMilestone(id: String)

    // ==================== MENSAJES DE AMOR ====================

    @Query("SELECT * FROM love_messages ORDER BY createdAt DESC")
    fun getAllLoveMessages(): Flow<List<LoveMessageEntity>>

    @Query("SELECT * FROM love_messages WHERE id = :id")
    suspend fun getLoveMessageById(id: String): LoveMessageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoveMessage(message: LoveMessageEntity)

    @Update
    suspend fun updateLoveMessage(message: LoveMessageEntity)

    @Query("DELETE FROM love_messages WHERE id = :id")
    suspend fun deleteLoveMessage(id: String)

    // ==================== CITAS ROMÁNTICAS ====================

    @Query("SELECT * FROM romantic_date_ideas ORDER BY createdAt DESC")
    fun getAllRomanticDateIdeas(): Flow<List<RomanticDateIdeaEntity>>

    @Query("SELECT * FROM romantic_date_ideas WHERE id = :id")
    suspend fun getRomanticDateIdeaById(id: String): RomanticDateIdeaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRomanticDateIdea(idea: RomanticDateIdeaEntity)

    @Update
    suspend fun updateRomanticDateIdea(idea: RomanticDateIdeaEntity)

    @Query("DELETE FROM romantic_date_ideas WHERE id = :id")
    suspend fun deleteRomanticDateIdea(id: String)

    // ==================== DESAFÍOS DE PAREJA ====================

    @Query("SELECT * FROM romantic_challenges ORDER BY createdAt DESC")
    fun getAllRomanticChallenges(): Flow<List<RomanticChallengeEntity>>

    @Query("SELECT * FROM romantic_challenges WHERE id = :id")
    suspend fun getRomanticChallengeById(id: String): RomanticChallengeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRomanticChallenge(challenge: RomanticChallengeEntity)

    @Update
    suspend fun updateRomanticChallenge(challenge: RomanticChallengeEntity)

    @Query("DELETE FROM romantic_challenges WHERE id = :id")
    suspend fun deleteRomanticChallenge(id: String)

    @Query("UPDATE romantic_challenges SET isCompleted = 1, completedAt = :completedAt WHERE id = :id")
    suspend fun completeRomanticChallenge(id: String, completedAt: Long = System.currentTimeMillis())

    // ==================== ESTADÍSTICAS DE AMOR ====================

    @Query("SELECT * FROM love_statistics WHERE id = 'stats' LIMIT 1")
    suspend fun getLoveStatistics(): LoveStatisticsEntity?

    @Query("SELECT * FROM love_statistics WHERE id = 'stats' LIMIT 1")
    fun getLoveStatisticsFlow(): Flow<LoveStatisticsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoveStatistics(stats: LoveStatisticsEntity)

    @Query("SELECT COUNT(*) FROM romantic_message_stats WHERE senderId = :userId")
    suspend fun getTotalMessagesSent(userId: String): Int

    @Query("SELECT COUNT(*) FROM romantic_message_stats WHERE recipientId = :userId")
    suspend fun getTotalMessagesReceived(userId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRomanticMessageStats(message: RomanticMessageStatsEntity)

    @Query("SELECT * FROM special_dates ORDER BY dateTimestamp ASC")
    fun getAllSpecialDates(): Flow<List<SpecialDateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpecialDate(date: SpecialDateEntity)

    @Delete
    suspend fun deleteSpecialDate(date: SpecialDateEntity)

    @Query("SELECT * FROM daily_love_stats ORDER BY date DESC LIMIT 30")
    fun getRecentDailyStats(): Flow<List<DailyLoveStatsEntity>>

    @Query("SELECT * FROM daily_love_stats WHERE date = :date LIMIT 1")
    suspend fun getDailyStats(date: String): DailyLoveStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyStats(stats: DailyLoveStatsEntity)

    @Update
    suspend fun updateDailyStats(stats: DailyLoveStatsEntity)

    @Query("""
        SELECT COUNT(*) FROM romantic_message_stats 
        WHERE romanticWordsDetected LIKE '%' || :word || '%'
    """)
    suspend fun getWordUsageCount(word: String): Int
}
