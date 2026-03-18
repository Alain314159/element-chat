/*
 * Copyright 2020-2024 New Vector Ltd.
 * Copyright 2024 Cerdita App
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.app.features.home.room.detail

import im.vector.app.features.romantic.RomanticWordDetector
import im.vector.app.features.romantic.ui.RomanticEffectType
import im.vector.lib.core.utils.timer.Clock
import org.matrix.android.sdk.api.session.events.model.toModel
import org.matrix.android.sdk.api.session.room.model.message.MessageContent
import org.matrix.android.sdk.api.session.room.model.message.MessageType
import org.matrix.android.sdk.api.session.room.timeline.TimelineEvent
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

enum class ChatEffect {
    CONFETTI,
    SNOWFALL
}

fun ChatEffect.toMessageType(): String {
    return when (this) {
        ChatEffect.CONFETTI -> MessageType.MSGTYPE_CONFETTI
        ChatEffect.SNOWFALL -> MessageType.MSGTYPE_SNOWFALL
    }
}

/**
 * A simple chat effect manager helper class
 * Used by the view model to know if an event that become visible should trigger a chat effect.
 * It also manages effect duration and some cool down, for example if an effect is currently playing,
 * any other trigger will be ignored
 * For now it uses visibility callback to check for an effect (that means that a fail to decrypt event - more
 * precisely an event decrypted with a few delay won't trigger an effect; it's acceptable)
 * Events that are more that 10s old won't trigger effects
 * 
 * Extended to support romantic word detection for special visual effects
 */
class ChatEffectManager @Inject constructor(
        private val clock: Clock,
) {

    interface Delegate {
        fun stopEffects()
        fun shouldStartEffect(effect: ChatEffect)
    }

    /**
     * Delegate para efectos románticos
     * Permite notificar cuando se detecta una palabra romántica
     */
    interface RomanticDelegate {
        fun onRomanticWordDetected(effectType: RomanticEffectType, category: String, event: TimelineEvent)
    }

    var delegate: Delegate? = null
    var romanticDelegate: RomanticDelegate? = null

    private var stopTimer: Timer? = null
    private var romanticStopTimer: Timer? = null

    // an in memory store to avoid trigger twice for an event (quick close/open timeline)
    private val alreadyPlayed = mutableListOf<String>()
    private val alreadyPlayedRomantic = mutableListOf<String>()

    fun checkForEffect(event: TimelineEvent) {
        val age = event.root.ageLocalTs ?: 0
        val now = clock.epochMillis()
        // messages older than 10s should not trigger any effect
        if ((now - age) >= 10_000) return
        val content = event.root.getClearContent()?.toModel<MessageContent>() ?: return
        
        // Check for standard chat effects (confetti, snowfall)
        val effect = findEffect(content, event)
        if (effect != null) {
            synchronized(this) {
                if (hasAlreadyPlayed(event)) return
                markAsAlreadyPlayed(event)
                // there is already an effect playing, so ignore
                if (stopTimer != null) return
                delegate?.shouldStartEffect(effect)
                stopTimer = Timer().apply {
                    schedule(object : TimerTask() {
                        override fun run() {
                            stopEffect()
                        }
                    }, 6_000)
                }
            }
        }
        
        // Check for romantic word effects
        checkForRomanticEffect(event, content)
    }

    /**
     * Verifica si un mensaje contiene palabras románticas
     * y notifica al delegate romántico si es así
     */
    private fun checkForRomanticEffect(event: TimelineEvent, content: MessageContent) {
        // Solo procesar mensajes de texto
        if (content.type != MessageType.MSGTYPE_TEXT && content.type != MessageType.MSGTYPE_EMOTE) {
            return
        }
        
        val messageBody = content.body ?: return
        
        // Evitar procesar el mismo evento dos veces
        if (hasAlreadyPlayedRomantic(event)) return
        
        val romanticCategory = RomanticWordDetector.detectCategory(messageBody)
        
        if (romanticCategory != null) {
            synchronized(this) {
                markAsAlreadyPlayedRomantic(event)
                
                // Ignorar si ya hay un efecto romántico en progreso
                if (romanticStopTimer != null) return
                
                // Notificar al delegate romántico
                romanticDelegate?.onRomanticWordDetected(
                    romanticCategory.effectType,
                    romanticCategory.name,
                    event
                )
                
                // Configurar timer para permitir nuevos efectos después del cooldown
                romanticStopTimer = Timer().apply {
                    schedule(object : TimerTask() {
                        override fun run() {
                            stopRomanticEffect()
                        }
                    }, getRomanticEffectDuration(romanticCategory.effectType))
                }
            }
        }
    }

    /**
     * Obtiene la duración del efecto romántico en milisegundos
     */
    private fun getRomanticEffectDuration(effectType: RomanticEffectType): Long {
        return when (effectType) {
            RomanticEffectType.HEARTS -> 4000L
            RomanticEffectType.KISS -> 3000L
            RomanticEffectType.SUNRISE -> 3500L
            RomanticEffectType.MOON -> 3500L
            RomanticEffectType.CONFETTI -> 5000L
            RomanticEffectType.CLOUDS -> 4000L
            RomanticEffectType.FLOWERS -> 4000L
            RomanticEffectType.HUG -> 3000L
            RomanticEffectType.SPARKLES -> 3000L
            RomanticEffectType.NONE -> 0L
        }
    }

    fun dispose() {
        stopTimer?.cancel()
        stopTimer = null
        romanticStopTimer?.cancel()
        romanticStopTimer = null
        alreadyPlayed.clear()
        alreadyPlayedRomantic.clear()
    }

    @Synchronized
    private fun stopEffect() {
        stopTimer = null
        delegate?.stopEffects()
    }

    @Synchronized
    private fun stopRomanticEffect() {
        romanticStopTimer = null
    }

    private fun markAsAlreadyPlayed(event: TimelineEvent) {
        alreadyPlayed.add(event.eventId)
        // also put the tx id as fast way to deal with local echo
        event.root.unsignedData?.transactionId?.let {
            alreadyPlayed.add(it)
        }
    }

    private fun markAsAlreadyPlayedRomantic(event: TimelineEvent) {
        alreadyPlayedRomantic.add(event.eventId)
        // also put the tx id as fast way to deal with local echo
        event.root.unsignedData?.transactionId?.let {
            alreadyPlayedRomantic.add(it)
        }
    }

    private fun hasAlreadyPlayed(event: TimelineEvent): Boolean {
        return alreadyPlayed.contains(event.eventId) ||
                (event.root.unsignedData?.transactionId?.let { alreadyPlayed.contains(it) } ?: false)
    }

    private fun hasAlreadyPlayedRomantic(event: TimelineEvent): Boolean {
        return alreadyPlayedRomantic.contains(event.eventId) ||
                (event.root.unsignedData?.transactionId?.let { alreadyPlayedRomantic.contains(it) } ?: false)
    }

    private fun findEffect(content: MessageContent, event: TimelineEvent): ChatEffect? {
        return when (content.msgType) {
            MessageType.MSGTYPE_CONFETTI -> ChatEffect.CONFETTI
            MessageType.MSGTYPE_SNOWFALL -> ChatEffect.SNOWFALL
            MessageType.MSGTYPE_EMOTE,
            MessageType.MSGTYPE_TEXT -> {
                event.root.getClearContent().toModel<MessageContent>()?.body
                        ?.let { text ->
                            when {
                                EMOJIS_FOR_CONFETTI.any { text.contains(it) } -> ChatEffect.CONFETTI
                                EMOJIS_FOR_SNOWFALL.any { text.contains(it) } -> ChatEffect.SNOWFALL
                                else -> null
                            }
                        }
            }
            else -> null
        }
    }

    companion object {
        private val EMOJIS_FOR_CONFETTI = listOf(
                "🎉",
                "🎊"
        )
        private val EMOJIS_FOR_SNOWFALL = listOf(
                "⛄️",
                "☃️",
                "❄️"
        )
    }
}
