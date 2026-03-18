/*
 * Copyright (c) 2026 Element Chat Romántico
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.app.features.romantic.data.repository

import im.vector.app.features.romantic.data.dao.RomanticDao
import im.vector.app.features.romantic.data.database.*
import im.vector.app.features.romantic.data.database.RomanticDatabase
import im.vector.app.features.romantic.stats.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositorio unificado para todos los datos románticos.
 * Centraliza el acceso a la base de datos Room y SharedPreferences.
 */
@Singleton
class RomanticRepository @Inject constructor(
    private val romanticDao: RomanticDao,
    private val database: RomanticDatabase
) {
    // ==================== DÍAS DE RELACIÓN ====================
    
    /**
     * Obtiene los días juntos como Flow.
     */
    fun getRelationshipDays(): Flow<Long> {
        return romanticDao.getRelationshipDaysFlow()
    }
    
    /**
     * Obtiene los días juntos como valor único.
     */
    suspend fun getRelationshipDaysValue(): Long {
        return romanticDao.getRelationshipDays() ?: 0L
    }
    
    /**
     * Establece la fecha de inicio de la relación.
     */
    suspend fun setRelationshipStartDate(timestamp: Long) {
        romanticDao.setRelationshipStartDate(timestamp)
    }
    
    /**
     * Obtiene la fecha de inicio de la relación.
     */
    suspend fun getRelationshipStartDate(): Long? {
        return romanticDao.getRelationshipStartDate()
    }
    
    // ==================== NOTAS DE AMOR ====================
    
    /**
     * Obtiene todas las notas de amor como Flow.
     */
    fun getLoveNotes(): Flow<List<LoveNoteEntity>> {
        return romanticDao.getAllLoveNotes()
    }
    
    /**
     * Obtiene una nota de amor por ID.
     */
    suspend fun getLoveNoteById(id: String): LoveNoteEntity? {
        return romanticDao.getLoveNoteById(id)
    }
    
    /**
     * Crea una nueva nota de amor.
     */
    suspend fun createLoveNote(note: LoveNoteEntity) {
        romanticDao.insertLoveNote(note)
    }
    
    /**
     * Actualiza una nota de amor existente.
     */
    suspend fun updateLoveNote(note: LoveNoteEntity) {
        romanticDao.updateLoveNote(note)
    }
    
    /**
     * Elimina una nota de amor.
     */
    suspend fun deleteLoveNote(id: String) {
        romanticDao.deleteLoveNote(id)
    }
    
    /**
     * Marca una nota de amor como leída.
     */
    suspend fun markLoveNoteAsRead(id: String) {
        romanticDao.markLoveNoteAsRead(id)
    }
    
    // ==================== REGALOS ROMÁNTICOS ====================
    
    /**
     * Obtiene todos los regalos románticos como Flow.
     */
    fun getRomanticGifts(): Flow<List<RomanticGiftEntity>> {
        return romanticDao.getAllRomanticGifts()
    }
    
    /**
     * Crea un nuevo regalo romántico.
     */
    suspend fun createRomanticGift(gift: RomanticGiftEntity) {
        romanticDao.insertRomanticGift(gift)
    }
    
    /**
     * Obtiene un regalo por ID.
     */
    suspend fun getRomanticGiftById(id: String): RomanticGiftEntity? {
        return romanticDao.getRomanticGiftById(id)
    }
    
    // ==================== CÁPSULAS DEL TIEMPO ====================
    
    /**
     * Obtiene todas las cápsulas del tiempo como Flow.
     */
    fun getTimeCapsules(): Flow<List<TimeCapsuleEntity>> {
        return romanticDao.getAllTimeCapsules()
    }
    
    /**
     * Crea una nueva cápsula del tiempo.
     */
    suspend fun createTimeCapsule(capsule: TimeCapsuleEntity) {
        romanticDao.insertTimeCapsule(capsule)
    }
    
    /**
     * Obtiene una cápsula del tiempo por ID.
     */
    suspend fun getTimeCapsuleById(id: String): TimeCapsuleEntity? {
        return romanticDao.getTimeCapsuleById(id)
    }
    
    /**
     * Actualiza una cápsula del tiempo.
     */
    suspend fun updateTimeCapsule(capsule: TimeCapsuleEntity) {
        romanticDao.updateTimeCapsule(capsule)
    }
    
    /**
     * Elimina una cápsula del tiempo.
     */
    suspend fun deleteTimeCapsule(id: String) {
        romanticDao.deleteTimeCapsule(id)
    }
    
    // ==================== HITOS DE RELACIÓN ====================
    
    /**
     * Obtiene todos los hitos de relación como Flow.
     */
    fun getRelationshipMilestones(): Flow<List<RelationshipMilestoneEntity>> {
        return romanticDao.getAllRelationshipMilestones()
    }
    
    /**
     * Crea un nuevo hito de relación.
     */
    suspend fun createRelationshipMilestone(milestone: RelationshipMilestoneEntity) {
        romanticDao.insertRelationshipMilestone(milestone)
    }
    
    /**
     * Obtiene un hito por ID.
     */
    suspend fun getRelationshipMilestoneById(id: String): RelationshipMilestoneEntity? {
        return romanticDao.getRelationshipMilestoneById(id)
    }
    
    /**
     * Actualiza un hito de relación.
     */
    suspend fun updateRelationshipMilestone(milestone: RelationshipMilestoneEntity) {
        romanticDao.updateRelationshipMilestone(milestone)
    }
    
    /**
     * Elimina un hito de relación.
     */
    suspend fun deleteRelationshipMilestone(id: String) {
        romanticDao.deleteRelationshipMilestone(id)
    }
    
    // ==================== MENSAJES DE AMOR ====================
    
    /**
     * Obtiene todos los mensajes de amor como Flow.
     */
    fun getLoveMessages(): Flow<List<LoveMessageEntity>> {
        return romanticDao.getAllLoveMessages()
    }
    
    /**
     * Crea un nuevo mensaje de amor.
     */
    suspend fun createLoveMessage(message: LoveMessageEntity) {
        romanticDao.insertLoveMessage(message)
    }
    
    /**
     * Obtiene un mensaje de amor por ID.
     */
    suspend fun getLoveMessageById(id: String): LoveMessageEntity? {
        return romanticDao.getLoveMessageById(id)
    }
    
    /**
     * Actualiza un mensaje de amor.
     */
    suspend fun updateLoveMessage(message: LoveMessageEntity) {
        romanticDao.updateLoveMessage(message)
    }
    
    /**
     * Elimina un mensaje de amor.
     */
    suspend fun deleteLoveMessage(id: String) {
        romanticDao.deleteLoveMessage(id)
    }
    
    // ==================== ESTADÍSTICAS DE CARIÑO ====================
    
    /**
     * Obtiene las estadísticas de cariño (besos y abrazos enviados).
     */
    suspend fun getAffectionStats(): AffectionStats {
        val totalKisses = romanticDao.getTotalKisses()
        val totalHugs = romanticDao.getTotalHugs()
        val lastKissTimestamp = romanticDao.getLastKissTimestamp()
        val lastHugTimestamp = romanticDao.getLastHugTimestamp()
        
        return AffectionStats(
            totalKisses = totalKisses ?: 0,
            totalHugs = totalHugs ?: 0,
            lastKissTimestamp = lastKissTimestamp,
            lastHugTimestamp = lastHugTimestamp
        )
    }
    
    /**
     * Registra un beso enviado.
     */
    suspend fun registerKiss(timestamp: Long = System.currentTimeMillis()) {
        romanticDao.insertKiss(timestamp)
    }
    
    /**
     * Registra un abrazo enviado.
     */
    suspend fun registerHug(timestamp: Long = System.currentTimeMillis()) {
        romanticDao.insertHug(timestamp)
    }
    
    /**
     * Obtiene el nivel de cariño basado en las interacciones.
     */
    suspend fun getAffectionLevel(): Int {
        val stats = getAffectionStats()
        val total = stats.totalKisses + stats.totalHugs
        return when {
            total < 10 -> 1
            total < 50 -> 2
            total < 100 -> 3
            total < 500 -> 4
            total < 1000 -> 5
            else -> 6
        }
    }
    
    // ==================== CITAS ROMÁNTICAS ====================
    
    /**
     * Obtiene todas las ideas de citas románticas.
     */
    fun getRomanticDateIdeas(): Flow<List<RomanticDateIdeaEntity>> {
        return romanticDao.getAllRomanticDateIdeas()
    }
    
    /**
     * Crea una nueva idea de cita romántica.
     */
    suspend fun createRomanticDateIdea(idea: RomanticDateIdeaEntity) {
        romanticDao.insertRomanticDateIdea(idea)
    }
    
    /**
     * Obtiene una idea de cita por ID.
     */
    suspend fun getRomanticDateIdeaById(id: String): RomanticDateIdeaEntity? {
        return romanticDao.getRomanticDateIdeaById(id)
    }
    
    /**
     * Actualiza una idea de cita romántica.
     */
    suspend fun updateRomanticDateIdea(idea: RomanticDateIdeaEntity) {
        romanticDao.updateRomanticDateIdea(idea)
    }
    
    /**
     * Elimina una idea de cita romántica.
     */
    suspend fun deleteRomanticDateIdea(id: String) {
        romanticDao.deleteRomanticDateIdea(id)
    }
    
    // ==================== DESAFÍOS DE PAREJA ====================
    
    /**
     * Obtiene todos los desafíos de pareja.
     */
    fun getCoupleChallenges(): Flow<List<RomanticChallengeEntity>> {
        return romanticDao.getAllRomanticChallenges()
    }
    
    /**
     * Crea un nuevo desafío de pareja.
     */
    suspend fun createCoupleChallenge(challenge: RomanticChallengeEntity) {
        romanticDao.insertRomanticChallenge(challenge)
    }
    
    /**
     * Obtiene un desafío por ID.
     */
    suspend fun getCoupleChallengeById(id: String): RomanticChallengeEntity? {
        return romanticDao.getRomanticChallengeById(id)
    }
    
    /**
     * Actualiza un desafío de pareja.
     */
    suspend fun updateCoupleChallenge(challenge: RomanticChallengeEntity) {
        romanticDao.updateRomanticChallenge(challenge)
    }
    
    /**
     * Elimina un desafío de pareja.
     */
    suspend fun deleteCoupleChallenge(id: String) {
        romanticDao.deleteRomanticChallenge(id)
    }
    
    /**
     * Marca un desafío como completado.
     */
    suspend fun completeCoupleChallenge(id: String, completedAt: Long = System.currentTimeMillis()) {
        romanticDao.completeRomanticChallenge(id, completedAt)
    }

    // ==================== ESTADÍSTICAS DE AMOR ====================

    /**
     * Obtiene las estadísticas completas de amor.
     */
    suspend fun getLoveStatistics(): LoveStatisticsEntity? {
        return romanticDao.getLoveStatistics()
    }

    /**
     * Obtiene las estadísticas de amor como Flow.
     */
    fun getLoveStatisticsFlow(): Flow<LoveStatisticsEntity?> {
        return romanticDao.getLoveStatisticsFlow()
    }

    /**
     * Inserta o actualiza las estadísticas de amor.
     */
    suspend fun insertLoveStatistics(stats: LoveStatisticsEntity) {
        romanticDao.insertLoveStatistics(stats)
    }

    /**
     * Registra un mensaje para estadísticas.
     */
    suspend fun registerMessageStats(message: RomanticMessageStatsEntity) {
        romanticDao.insertRomanticMessageStats(message)
    }

    /**
     * Obtiene las fechas especiales.
     */
    fun getSpecialDates(): Flow<List<SpecialDateEntity>> {
        return romanticDao.getAllSpecialDates()
    }

    /**
     * Agrega una fecha especial.
     */
    suspend fun addSpecialDate(date: SpecialDateEntity) {
        romanticDao.insertSpecialDate(date)
    }

    /**
     * Elimina una fecha especial.
     */
    suspend fun deleteSpecialDate(date: SpecialDateEntity) {
        romanticDao.deleteSpecialDate(date)
    }

    /**
     * Obtiene las estadísticas diarias recientes.
     */
    fun getRecentDailyStats(): Flow<List<DailyLoveStatsEntity>> {
        return romanticDao.getRecentDailyStats()
    }

    /**
     * Obtiene las estadísticas de un día específico.
     */
    suspend fun getDailyStats(date: String): DailyLoveStatsEntity? {
        return romanticDao.getDailyStats(date)
    }

    /**
     * Inserta o actualiza las estadísticas diarias.
     */
    suspend fun insertDailyStats(stats: DailyLoveStatsEntity) {
        romanticDao.insertDailyStats(stats)
    }

    /**
     * Actualiza las estadísticas diarias.
     */
    suspend fun updateDailyStats(stats: DailyLoveStatsEntity) {
        romanticDao.updateDailyStats(stats)
    }

    /**
     * Obtiene el conteo de uso de una palabra romántica.
     */
    suspend fun getWordUsageCount(word: String): Int {
        return romanticDao.getWordUsageCount(word)
    }
}

/**
 * Data class para estadísticas de cariño.
 */
data class AffectionStats(
    val totalKisses: Int,
    val totalHugs: Int,
    val lastKissTimestamp: Long?,
    val lastHugTimestamp: Long?
) {
    val totalInteractions: Int
        get() = totalKisses + totalHugs
}
