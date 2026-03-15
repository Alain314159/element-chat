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

package im.vector.app.features.romantic.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import im.vector.app.R
import im.vector.app.features.romantic.settings.RomanticSettingsActivity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Servicio de notificaciones para eventos románticos.
 * Envía notificaciones cuando tu pareja envía besos, abrazos u otros gestos románticos.
 */
@Singleton
class RomanticNotificationService @Inject constructor(
    private val context: Context
) {
    companion object {
        private const val CHANNEL_ID = "romantic_notifications"
        private const val CHANNEL_NAME = "Notificaciones Románticas"
        private const val CHANNEL_DESCRIPTION = "Notificaciones de besos, abrazos y eventos románticos"
        private const val NOTIFICATION_ID_BASE = 1000
        
        const val EXTRA_ROMANTIC_EVENT_TYPE = "romantic_event_type"
        const val EXTRA_SENDER_NAME = "sender_name"
        
        const val EVENT_KISS = "kiss"
        const val EVENT_HUG = "hug"
        const val EVENT_GIFT = "gift"
        const val EVENT_NOTE = "note"
        const val EVENT_ANNIVERSARY = "anniversary"
    }

    init {
        createNotificationChannel()
    }

    /**
     * Crea el canal de notificaciones para Android O y superior.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
                enableLights(true)
                lightColor = android.graphics.Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 100, 50, 100, 50, 100)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Envía una notificación de beso recibido.
     */
    fun sendKissNotification(senderName: String) {
        sendRomanticNotification(
            eventType = EVENT_KISS,
            title = "💋 ¡${senderName} te envió un beso!",
            message = "Toca para responder con un beso",
            icon = android.R.drawable.btn_star_big_on
        )
    }

    /**
     * Envía una notificación de abrazo recibido.
     */
    fun sendHugNotification(senderName: String) {
        sendRomanticNotification(
            eventType = EVENT_HUG,
            title = "🤗 ¡${senderName} te envió un abrazo!",
            message = "Toca para responder con un abrazo",
            icon = android.R.drawable.btn_star_big_on
        )
    }

    /**
     * Envía una notificación de regalo recibido.
     */
    fun sendGiftNotification(senderName: String, giftName: String) {
        sendRomanticNotification(
            eventType = EVENT_GIFT,
            title = "🎁 ¡${senderName} te envió un regalo!",
            message = "Has recibido: $giftName",
            icon = android.R.drawable.ic_dialog_info
        )
    }

    /**
     * Envía una notificación de nota de amor recibida.
     */
    fun sendNoteNotification(senderName: String) {
        sendRomanticNotification(
            eventType = EVENT_NOTE,
            title = "💌 ¡${senderName} te envió una nota de amor!",
            message = "Toca para leer tu nota",
            icon = android.R.drawable.ic_dialog_email
        )
    }

    /**
     * Envía una notificación de aniversario.
     */
    fun sendAnniversaryNotification(daysTogether: Long) {
        val message = when {
            daysTogether == 1 -> "¡Feliz 1 día juntos!"
            daysTogether < 30 -> "¡Felices $daysTogether días juntos!"
            daysTogether == 30 -> "¡Feliz 1 mes juntos!"
            daysTogether < 365 -> "¡Felices ${daysTogether / 30} meses juntos!"
            daysTogether == 365 -> "¡Feliz 1 año juntos!"
            else -> "¡Felices ${daysTogether / 365} años juntos!"
        }

        sendRomanticNotification(
            eventType = EVENT_ANNIVERSARY,
            title = "💕 ¡Feliz Aniversario!",
            message = message,
            icon = android.R.drawable.btn_star_big_on
        )
    }

    /**
     * Envía una notificación romántica genérica.
     */
    private fun sendRomanticNotification(
        eventType: String,
        title: String,
        message: String,
        icon: Int
    ) {
        // Crear Intent para abrir la actividad romántica
        val intent = Intent(context, RomanticSettingsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EXTRA_ROMANTIC_EVENT_TYPE, eventType)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Construir notificación
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        // Enviar notificación
        NotificationManagerCompat.from(context).notify(
            NOTIFICATION_ID_BASE + eventType.hashCode(),
            notification
        )
    }

    /**
     * Cancela una notificación romántica específica.
     */
    fun cancelNotification(eventType: String) {
        NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID_BASE + eventType.hashCode())
    }

    /**
     * Cancela todas las notificaciones románticas.
     */
    fun cancelAllNotifications() {
        NotificationManagerCompat.from(context).cancelAll()
    }
}
