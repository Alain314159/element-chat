/*
 * Copyright (c) 2026 Element Chat Romántico
 *
 * Entidades adicionales para la base de datos romántica
 */

package im.vector.app.features.romantic.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad para registrar besos enviados
 */
@Entity(tableName = "kisses")
data class KissEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val senderId: String = "",
    val recipientId: String = "",
    val hugType: String = "normal" // normal, long, tight, spin
)

/**
 * Entidad para registrar abrazos enviados
 */
@Entity(tableName = "hugs")
data class HugEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val senderId: String = "",
    val recipientId: String = "",
    val hugType: String = "normal" // normal, long, tight, spin
)

/**
 * Entidad para configuración de relación
 */
@Entity(tableName = "relationship_settings")
data class RelationshipSettingsEntity(
    @PrimaryKey val id: String,
    val timestamp: Long,
    val partnerName: String? = null,
    val partnerUserId: String? = null
)

/**
 * Entidad para regalos románticos
 */
@Entity(tableName = "romantic_gifts")
data class RomanticGiftEntity(
    @PrimaryKey val id: String,
    val giftType: String,
    val giftName: String,
    val message: String?,
    val senderId: String,
    val recipientId: String,
    val sentAt: Long = System.currentTimeMillis(),
    val isAccepted: Boolean = false,
    val acceptedAt: Long? = null
)

/**
 * Entidad para mensajes de amor
 */
@Entity(tableName = "love_messages")
data class LoveMessageEntity(
    @PrimaryKey val id: String,
    val content: String,
    val messageType: String,
    val senderId: String,
    val recipientId: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val readAt: Long? = null
)

/**
 * Entidad para ideas de citas románticas
 */
@Entity(tableName = "romantic_date_ideas")
data class RomanticDateIdeaEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val category: String, // indoor, outdoor, virtual, special
    val estimatedCost: Int = 0,
    val duration: Int = 60, // minutos
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val rating: Int? = null,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Entidad para desafíos de pareja
 */
@Entity(tableName = "romantic_challenges")
data class RomanticChallengeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val challengeType: String, // daily, weekly, monthly, special
    val difficulty: Int = 1, // 1-5
    val lovePoints: Int = 10,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val expiresAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
