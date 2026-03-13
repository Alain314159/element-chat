/*
 * Copyright 2024 Cerdita App
 * 
 * Contador de Días Juntos
 * Muestra el tiempo que llevan como pareja
 */

package im.vector.app.features.romantic

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import java.time.Duration
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

/**
 * Manager para el Contador de Días Juntos
 * 
 * FUNCIONALIDAD:
 * - Guarda la fecha de inicio de la relación
 * - Calcula días, meses y años juntos
 * - Muestra mensajes especiales en aniversarios
 * - Persistencia local con SharedPreferences
 */
class RelationshipDaysManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )
    
    companion object {
        private const val PREFS_NAME = "cerdita_relationship"
        private const val KEY_START_DATE = "relationship_start_date"
        private const val KEY_ANNIVERSARY_REMINDER = "anniversary_reminder_enabled"
        
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }
    
    /**
     * Establece la fecha de inicio de la relación
     * @param date La fecha cuando comenzaron
     */
    fun setStartDate(date: LocalDate) {
        prefs.edit {
            putString(KEY_START_DATE, date.format(DATE_FORMATTER))
        }
    }
    
    /**
     * Obtiene la fecha de inicio de la relación
     * @return La fecha de inicio o null si no está configurada
     */
    fun getStartDate(): LocalDate? {
        val dateString = prefs.getString(KEY_START_DATE, null)
        return dateString?.let {
            try {
                LocalDate.parse(it, DATE_FORMATTER)
            } catch (e: Exception) {
                null
            }
        }
    }
    
    /**
     * Calcula los días juntos
     * @return Número de días completos juntos
     */
    fun getDaysTogether(): Long {
        val startDate = getStartDate() ?: return 0
        return Duration.between(
            startDate.atStartOfDay(),
            LocalDate.now().atStartOfDay()
        ).toDays()
    }
    
    /**
     * Obtiene el tiempo juntos detallado
     * @return Period con años, meses y días
     */
    fun getTimeTogether(): Period {
        val startDate = getStartDate() ?: return Period.ZERO
        return Period.between(startDate, LocalDate.now())
    }
    
    /**
     * Formatea el tiempo juntos como string bonito
     * @return String formateado ej: "1 año, 2 meses y 3 días juntos 💕"
     */
    fun getFormattedTimeTogether(): String {
        val period = getTimeTogether()
        val days = getDaysTogether()
        
        return buildString {
            if (period.years > 0) {
                append("${period.years} año")
                if (period.years > 1) append("s")
                append(", ")
            }
            if (period.months > 0) {
                append("${period.months} mes")
                if (period.months > 1) append("es")
                append(", ")
            }
            append("${period.days} día")
            if (period.days != 1) append("s")
            append(" juntos 💕")
        }.let {
            // Si es menos de un mes, mostrar solo días
            if (period.years == 0 && period.months == 0) {
                "$days días juntos 💕"
            } else {
                it
            }
        }
    }
    
    /**
     * Verifica si hoy es aniversario
     * @return true si hoy es el mismo día y mes que la fecha de inicio
     */
    fun isAnniversary(): Boolean {
        val startDate = getStartDate() ?: return false
        val today = LocalDate.now()
        return startDate.dayOfMonth == today.dayOfMonth && 
               startDate.month == today.month
    }
    
    /**
     * Obtiene el número de aniversario (años cumplidos)
     * @return Número de años de aniversario, 0 si no es aniversario
     */
    fun getAnniversaryYears(): Int {
        if (!isAnniversary()) return 0
        val period = getTimeTogether()
        return period.years
    }
    
    /**
     * Obtiene mensaje especial para el aniversario
     * @return Mensaje de felicitación
     */
    fun getAnniversaryMessage(): String {
        val years = getAnniversaryYears()
        return when {
            years == 0 -> "¡Hoy celebramos nuestro primer mes! 💕"
            years == 1 -> "¡Feliz primer aniversario! 🎉💖"
            else -> "¡Feliz aniversario #$years! Te amo más cada día 💕🎊"
        }
    }
    
    /**
     * Habilita o deshabilita recordatorio de aniversario
     */
    fun setAnniversaryReminder(enabled: Boolean) {
        prefs.edit {
            putBoolean(KEY_ANNIVERSARY_REMINDER, enabled)
        }
    }
    
    /**
     * Verifica si el recordatorio de aniversario está habilitado
     */
    fun isAnniversaryReminderEnabled(): Boolean {
        return prefs.getBoolean(KEY_ANNIVERSARY_REMINDER, true)
    }
    
    /**
     * Borra la fecha de inicio (para resetear)
     */
    fun clearStartDate() {
        prefs.edit {
            remove(KEY_START_DATE)
        }
    }
}
