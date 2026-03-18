/*
 * Copyright 2024 Cerdita App
 *
 * Entidades para Mejoras de Features Románticos
 * Implementación con Room para persistencia
 */

package im.vector.app.features.romantic.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

// ==================== 1. HUG REACTIONS - MEJORA PARA HUGBUTTON ====================

/**
 * Tipos de reacción a abrazos
 */
enum class HugReactionType {
    HEART,      // ❤️
    LOVE_FACE,  // 🥰
    SMILE,      // 😊
    FIRE,       // 🔥
    HUG_BACK    // 🤗
}

/**
 * Entidad para reacciones a abrazos
 * Permite reaccionar a abrazos recibidos
 */
@Entity(tableName = "hug_reactions")
data class HugReactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hugId: Int,
    val reactionType: String, // HEART, LOVE_FACE, SMILE, FIRE, HUG_BACK
    val userId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isNotified: Boolean = false
)

// ==================== 2. MASCOT OUTFITS - MEJORA PARA MASCOTSYSTEM ====================

/**
 * Tipos de outfit para mascotas
 */
enum class OutfitType {
    HAT,        // Sombreros, gorros
    SHIRT,      // Camisas, vestidos
    ACCESSORY,  // Accesorios varios
    SHOES,      // Zapatos
    FULL        // Disfraces completos
}

/**
 * Rareza de outfits
 */
enum class OutfitRarity {
    COMMON,     // Común - desbloqueado por defecto
    RARE,       // Raro - requiere XP
    EPIC,       // Épico - requiere logros
    LEGENDARY   // Legendario - eventos especiales
}

/**
 * Condición de desbloqueo de outfit
 */
enum class UnlockCondition {
    DEFAULT,        // Desbloqueado por defecto
    LEVEL,          // Desbloquear por nivel de mascota
    XP,             // Desbloquear por XP acumulada
    ACHIEVEMENT,    // Desbloquear por logro
    EVENT,          // Evento especial
    PURCHASE        // Compra con monedas
}

/**
 * Entidad para outfits de mascotas
 */
@Entity(tableName = "mascot_outfits")
data class MascotOutfitEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String, // HAT, SHIRT, ACCESSORY, SHOES, FULL
    val rarity: String, // COMMON, RARE, EPIC, LEGENDARY
    val unlockCondition: String, // DEFAULT, LEVEL, XP, ACHIEVEMENT, EVENT, PURCHASE
    val unlockRequirement: Int, // Nivel, XP, o ID de logro
    val coinCost: Int = 0,
    val description: String?,
    val iconUrl: String?,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long?
)

/**
 * Entidad para inventario de outfits equipados
 */
@Entity(tableName = "mascot_inventory")
data class MascotInventoryEntity(
    @PrimaryKey val id: String = "current_user",
    val mascotId: String,
    val ownedOutfitIds: String, // Lista separada por |
    val equippedOutfitId: String?,
    val lastUpdated: Long = System.currentTimeMillis()
)

// ==================== 3. CUSTOM MILESTONES - MEJORA PARA RELATIONSHIPTREE ====================

/**
 * Entidad para hitos personalizables con foto
 * Permite crear hitos personalizados con multimedia
 */
@Entity(tableName = "custom_milestones")
data class CustomMilestoneEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val date: Long,
    val photoUri: String?, // URI de la foto principal
    val audioNoteUri: String?, // URI de nota de audio opcional
    val tags: String, // Lista separada por |
    val isCustom: Boolean = true,
    val createdBy: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false, // Para sincronización con Matrix
    val syncedAt: Long?
)

// ==================== 4. CUSTOM COUPONS - MEJORA PARA LOVECOUPONS ====================

/**
 * Entidad para cupones personalizables
 * Permite crear cupones con condiciones y fecha de expiración
 */
@Entity(tableName = "custom_coupons")
data class CustomCouponEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val conditions: String, // Lista separada por |
    val expirationDate: Long?, // Fecha de vencimiento opcional
    val mediaAttachments: String, // Lista de URIs separada por |
    val redemptionCount: Int = 1, // Cuántas veces se puede redimir
    val maxRedemptions: Int = 1,
    val isTransferable: Boolean = true,
    val createdBy: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isRedeemed: Boolean = false,
    val redeemedAt: Long?,
    val redeemedBy: String?
)

/**
 * Entidad para historial de redención de cupones
 */
@Entity(tableName = "coupon_redemption_history")
data class CouponRedemptionHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val couponId: String,
    val couponTitle: String,
    val redeemedBy: String,
    val redeemedAt: Long = System.currentTimeMillis(),
    val notes: String?,
    val photoUri: String? // Foto del momento de redención
)

// ==================== 5. LOCATION TRIGGER - MEJORA PARA LOVENOTES ====================

/**
 * Entidad para triggers de ubicación en notas de amor
 * Permite desbloquear notas al llegar a lugares especiales
 */
@Entity(tableName = "location_triggers")
data class LocationTriggerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val noteId: String,
    val latitude: Double,
    val longitude: Double,
    val radiusMeters: Float,
    val triggerOnce: Boolean = true,
    val isTriggered: Boolean = false,
    val triggeredAt: Long?,
    val locationName: String? // Nombre opcional del lugar
)

/**
 * Entidad para notas colaborativas
 * Permite que ambos miembros de la pareja escriban en la misma nota
 */
@Entity(tableName = "collaborative_notes")
data class CollaborativeNoteEntity(
    @PrimaryKey val id: String,
    val title: String,
    val isLocked: Boolean = false,
    val unlockCondition: String?, // Condición para desbloquear
    val createdAt: Long = System.currentTimeMillis(),
    val createdBy: String,
    val lastModified: Long = System.currentTimeMillis()
)

/**
 * Entidad para contribuciones a notas colaborativas
 */
@Entity(tableName = "note_contributions")
data class NoteContributionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val noteId: String,
    val authorId: String,
    val content: String,
    val mediaAttachments: String, // Lista de URIs separada por |
    val timestamp: Long = System.currentTimeMillis(),
    val order: Int = 0 // Orden de la contribución
)
