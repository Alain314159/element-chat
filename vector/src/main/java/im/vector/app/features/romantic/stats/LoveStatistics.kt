/*
 * Copyright 2024 Cerdita App
 *
 * Estadísticas de Amor Avanzadas para Parejas
 * Implementación con Room para persistencia y Jetpack Compose para UI
 */

package im.vector.app.features.romantic.stats

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.*
import im.vector.app.features.romantic.RomanticWordDetector
import im.vector.app.features.romantic.data.dao.RomanticDao
import im.vector.app.features.romantic.data.database.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

// ==================== TIPOS DE DATOS ====================

/**
 * Tipos de mensaje romántico
 */
enum class MessageType {
    TEXT, IMAGE, AUDIO, VIDEO, STICKER, HUG, KISS, NOTE
}

/**
 * Tipos de fechas especiales
 */
enum class SpecialDateType {
    FIRST_MESSAGE, FIRST_DATE, ANNIVERSARY, CUSTOM
}

/**
 * Fecha especial de la pareja
 */
data class SpecialDate(
    val title: String,
    val date: LocalDate,
    val type: SpecialDateType
)

/**
 * Estadísticas completas de amor
 */
data class LoveStatistics(
    val totalMessagesSent: Int = 0,
    val totalMessagesReceived: Int = 0,
    val totalHugsSent: Int = 0,
    val totalKissesSent: Int = 0,
    val totalNotesCreated: Int = 0,
    val totalPhotosShared: Int = 0,
    val totalAudiosSent: Int = 0,
    val totalVideosSent: Int = 0,
    val totalLinks: Int = 0,
    val totalStickers: Int = 0,
    val averageMessagesPerDay: Double = 0.0,
    val mostUsedRomanticWord: String = "",
    val favoriteMessageType: MessageType = MessageType.TEXT,
    val longestStreak: Int = 0,
    val currentStreak: Int = 0,
    val relationshipDays: Int = 0,
    val specialDates: List<SpecialDate> = emptyList()
)

/**
 * Estadísticas diarias para gráfico
 */
data class DailyStats(
    val date: LocalDate,
    val messagesSent: Int = 0,
    val messagesReceived: Int = 0,
    val hugsSent: Int = 0,
    val kissesSent: Int = 0
)

/**
 * Logro desbloqueable
 */
data class LoveAchievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean = false,
    val progress: Int = 0,
    val target: Int
)

// ==================== ENTIDADES DE BASE DE DATOS ====================

/**
 * Entidad para estadísticas de amor
 */
@Entity(tableName = "love_statistics")
data class LoveStatisticsEntity(
    @PrimaryKey val id: String = "stats",
    val totalMessagesSent: Int = 0,
    val totalMessagesReceived: Int = 0,
    val totalHugsSent: Int = 0,
    val totalKissesSent: Int = 0,
    val totalNotesCreated: Int = 0,
    val totalPhotosShared: Int = 0,
    val totalAudiosSent: Int = 0,
    val totalVideosSent: Int = 0,
    val totalLinks: Int = 0,
    val totalStickers: Int = 0,
    val longestStreak: Int = 0,
    val currentStreak: Int = 0,
    val streakLastUpdated: Long? = null,
    val relationshipStartDate: Long? = null,
    val lastUpdated: Long = System.currentTimeMillis()
)

/**
 * Entidad para mensajes románticos (para tracking)
 */
@Entity(tableName = "romantic_message_stats")
data class RomanticMessageStatsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val messageType: String,
    val senderId: String,
    val recipientId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val romanticWordsDetected: String = "" // Palabras románticas encontradas
)

/**
 * Entidad para fechas especiales
 */
@Entity(tableName = "special_dates")
data class SpecialDateEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val dateTimestamp: Long,
    val type: String, // FIRST_MESSAGE, FIRST_DATE, ANNIVERSARY, CUSTOM
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Entidad para estadísticas diarias
 */
@Entity(tableName = "daily_love_stats")
data class DailyLoveStatsEntity(
    @PrimaryKey val date: String, // Formato: yyyy-MM-dd
    val messagesSent: Int = 0,
    val messagesReceived: Int = 0,
    val hugsSent: Int = 0,
    val kissesSent: Int = 0,
    val notesCreated: Int = 0,
    val photosShared: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
)

// ==================== DAO ====================

/**
 * DAO para operaciones de estadísticas de amor
 */
@Dao
interface LoveStatisticsDao {

    // ==================== ESTADÍSTICAS PRINCIPALES ====================

    @Query("SELECT * FROM love_statistics WHERE id = 'stats' LIMIT 1")
    suspend fun getStatistics(): LoveStatisticsEntity?

    @Query("SELECT * FROM love_statistics WHERE id = 'stats' LIMIT 1")
    fun getStatisticsFlow(): Flow<LoveStatisticsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatistics(stats: LoveStatisticsEntity)

    @Update
    suspend fun updateStatistics(stats: LoveStatisticsEntity)

    // ==================== MENSAJES ====================

    @Query("SELECT * FROM romantic_message_stats ORDER BY timestamp DESC")
    fun getAllMessageStats(): Flow<List<RomanticMessageStatsEntity>>

    @Query("SELECT COUNT(*) FROM romantic_message_stats WHERE senderId = :userId")
    suspend fun getTotalMessagesSent(userId: String): Int

    @Query("SELECT COUNT(*) FROM romantic_message_stats WHERE recipientId = :userId")
    suspend fun getTotalMessagesReceived(userId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessageStats(message: RomanticMessageStatsEntity)

    // ==================== FECHAS ESPECIALES ====================

    @Query("SELECT * FROM special_dates ORDER BY dateTimestamp ASC")
    fun getAllSpecialDates(): Flow<List<SpecialDateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpecialDate(date: SpecialDateEntity)

    @Delete
    suspend fun deleteSpecialDate(date: SpecialDateEntity)

    // ==================== ESTADÍSTICAS DIARIAS ====================

    @Query("SELECT * FROM daily_love_stats ORDER BY date DESC LIMIT 30")
    fun getRecentDailyStats(): Flow<List<DailyLoveStatsEntity>>

    @Query("SELECT * FROM daily_love_stats WHERE date = :date LIMIT 1")
    suspend fun getDailyStats(date: String): DailyLoveStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyStats(stats: DailyLoveStatsEntity)

    @Update
    suspend fun updateDailyStats(stats: DailyLoveStatsEntity)

    // ==================== CONSULTAS ESPECIALES ====================

    @Query("""
        SELECT date, SUM(messagesSent) as totalSent, SUM(messagesReceived) as totalReceived
        FROM daily_love_stats 
        GROUP BY strftime('%Y-%m', date)
        ORDER BY date DESC 
        LIMIT 12
    """)
    fun getMonthlyStats(): Flow<List<MonthlyAggregatedStats>>

    @Query("""
        SELECT COUNT(*) FROM romantic_message_stats 
        WHERE romanticWordsDetected LIKE '%' || :word || '%'
    """)
    suspend fun getWordUsageCount(word: String): Int
}

/**
 * Clase auxiliar para estadísticas mensuales agregadas
 */
data class MonthlyAggregatedStats(
    val date: String,
    val totalSent: Int,
    val totalReceived: Int
)

// ==================== MANAGER PRINCIPAL ====================

/**
 * Manager principal para estadísticas de amor
 * Gestiona persistencia, cálculos y tracking de estadísticas
 */
@Singleton
class LoveStatisticsManager @Inject constructor(
    private val romanticDao: RomanticDao,
    private val context: Context
) {

    private val userId: String = "current_user" // En producción, usar autenticación real

    // ==================== OBTENCIÓN DE ESTADÍSTICAS ====================

    /**
     * Obtiene las estadísticas completas de amor
     */
    suspend fun getLoveStatistics(): LoveStatistics {
        val stats = romanticDao.getLoveStatistics() ?: LoveStatisticsEntity()
        val relationshipDays = getRelationshipDays()

        // Calcular promedio de mensajes por día
        val totalMessages = stats.totalMessagesSent + stats.totalMessagesReceived
        val avgPerDay = if (relationshipDays > 0) {
            totalMessages.toDouble() / relationshipDays
        } else 0.0

        // Obtener palabra romántica más usada
        val mostUsedWord = getMostUsedRomanticWord()

        // Obtener tipo de mensaje favorito
        val favoriteType = getFavoriteMessageType()

        // Obtener fechas especiales
        val specialDates = getSpecialDates()

        // Obtener rachas actualizadas
        val streaks = calculateStreaks()

        return LoveStatistics(
            totalMessagesSent = stats.totalMessagesSent,
            totalMessagesReceived = stats.totalMessagesReceived,
            totalHugsSent = stats.totalHugsSent,
            totalKissesSent = stats.totalKissesSent,
            totalNotesCreated = stats.totalNotesCreated,
            totalPhotosShared = stats.totalPhotosShared,
            totalAudiosSent = stats.totalAudiosSent,
            totalVideosSent = stats.totalVideosSent,
            totalLinks = stats.totalLinks,
            totalStickers = stats.totalStickers,
            averageMessagesPerDay = avgPerDay,
            mostUsedRomanticWord = mostUsedWord,
            favoriteMessageType = favoriteType,
            longestStreak = stats.longestStreak,
            currentStreak = streaks.second,
            relationshipDays = relationshipDays,
            specialDates = specialDates
        )
    }

    /**
     * Obtiene las estadísticas como Flow
     */
    fun getLoveStatisticsFlow(): Flow<LoveStatistics> {
        return romanticDao.getLoveStatisticsFlow()
    }

    // ==================== INCREMENTO DE CONTADORES ====================

    /**
     * Incrementa el contador de mensajes enviados
     */
    suspend fun incrementMessagesSent(content: String = "") {
        val stats = romanticDao.getLoveStatistics() ?: LoveStatisticsEntity()
        romanticDao.insertLoveStatistics(
            stats.copy(
                totalMessagesSent = stats.totalMessagesSent + 1,
                lastUpdated = System.currentTimeMillis()
            )
        )

        // Registrar mensaje para tracking
        romanticDao.insertRomanticMessageStats(
            RomanticMessageStatsEntity(
                content = content,
                messageType = "TEXT",
                senderId = userId,
                recipientId = "partner",
                romanticWordsDetected = detectRomanticWords(content)
            )
        )

        // Actualizar estadísticas diarias
        updateDailyStats { it.copy(messagesSent = it.messagesSent + 1) }

        // Actualizar racha
        updateStreak()
    }

    /**
     * Incrementa el contador de mensajes recibidos
     */
    suspend fun incrementMessagesReceived() {
        val stats = romanticDao.getLoveStatistics() ?: LoveStatisticsEntity()
        romanticDao.insertLoveStatistics(
            stats.copy(
                totalMessagesReceived = stats.totalMessagesReceived + 1,
                lastUpdated = System.currentTimeMillis()
            )
        )
        updateDailyStats { it.copy(messagesReceived = it.messagesReceived + 1) }
    }

    /**
     * Incrementa el contador de abrazos enviados
     */
    suspend fun incrementHugsSent() {
        val stats = romanticDao.getLoveStatistics() ?: LoveStatisticsEntity()
        romanticDao.insertLoveStatistics(
            stats.copy(
                totalHugsSent = stats.totalHugsSent + 1,
                lastUpdated = System.currentTimeMillis()
            )
        )
        updateDailyStats { it.copy(hugsSent = it.hugsSent + 1) }
    }

    /**
     * Incrementa el contador de besos enviados
     */
    suspend fun incrementKissesSent() {
        val stats = romanticDao.getLoveStatistics() ?: LoveStatisticsEntity()
        romanticDao.insertLoveStatistics(
            stats.copy(
                totalKissesSent = stats.totalKissesSent + 1,
                lastUpdated = System.currentTimeMillis()
            )
        )
        updateDailyStats { it.copy(kissesSent = it.kissesSent + 1) }
    }

    /**
     * Incrementa el contador de notas creadas
     */
    suspend fun incrementNotesCreated() {
        val stats = romanticDao.getLoveStatistics() ?: LoveStatisticsEntity()
        romanticDao.insertLoveStatistics(
            stats.copy(
                totalNotesCreated = stats.totalNotesCreated + 1,
                lastUpdated = System.currentTimeMillis()
            )
        )
        updateDailyStats { it.copy(notesCreated = it.notesCreated + 1) }
    }

    /**
     * Incrementa el contador de fotos compartidas
     */
    suspend fun incrementPhotosShared() {
        val stats = romanticDao.getLoveStatistics() ?: LoveStatisticsEntity()
        romanticDao.insertLoveStatistics(
            stats.copy(
                totalPhotosShared = stats.totalPhotosShared + 1,
                lastUpdated = System.currentTimeMillis()
            )
        )
        updateDailyStats { it.copy(photosShared = it.photosShared + 1) }
    }

    /**
     * Incrementa el contador de audios enviados
     */
    suspend fun incrementAudiosSent() {
        val stats = romanticDao.getLoveStatistics() ?: LoveStatisticsEntity()
        romanticDao.insertLoveStatistics(
            stats.copy(
                totalAudiosSent = stats.totalAudiosSent + 1,
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    /**
     * Incrementa el contador de videos enviados
     */
    suspend fun incrementVideosSent() {
        val stats = romanticDao.getLoveStatistics() ?: LoveStatisticsEntity()
        romanticDao.insertLoveStatistics(
            stats.copy(
                totalVideosSent = stats.totalVideosSent + 1,
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    /**
     * Incrementa el contador de enlaces compartidos
     */
    suspend fun incrementLinks() {
        val stats = romanticDao.getLoveStatistics() ?: LoveStatisticsEntity()
        romanticDao.insertLoveStatistics(
            stats.copy(
                totalLinks = stats.totalLinks + 1,
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    /**
     * Incrementa el contador de stickers enviados
     */
    suspend fun incrementStickers() {
        val stats = romanticDao.getLoveStatistics() ?: LoveStatisticsEntity()
        romanticDao.insertLoveStatistics(
            stats.copy(
                totalStickers = stats.totalStickers + 1,
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    // ==================== CÁLCULOS ====================

    /**
     * Obtiene la palabra romántica más usada
     */
    private suspend fun getMostUsedRomanticWord(): String {
        var mostUsed = ""
        var maxCount = 0

        for (category in RomanticWordDetector.categories) {
            for (word in category.words) {
                val count = romanticDao.getWordUsageCount(word)
                if (count > maxCount) {
                    maxCount = count
                    mostUsed = word
                }
            }
        }

        return mostUsed
    }

    /**
     * Detecta palabras románticas en un mensaje
     */
    private fun detectRomanticWords(content: String): String {
        val detected = mutableListOf<String>()
        for (category in RomanticWordDetector.categories) {
            for (word in category.words) {
                if (content.contains(word, ignoreCase = true)) {
                    detected.add(word)
                }
            }
        }
        return detected.joinToString("|")
    }

    /**
     * Obtiene el tipo de mensaje más enviado
     */
    private suspend fun getFavoriteMessageType(): MessageType {
        val stats = romanticDao.getLoveStatistics() ?: return MessageType.TEXT

        val counts = mapOf(
            MessageType.TEXT to stats.totalMessagesSent,
            MessageType.HUG to stats.totalHugsSent,
            MessageType.KISS to stats.totalKissesSent,
            MessageType.NOTE to stats.totalNotesCreated,
            MessageType.IMAGE to stats.totalPhotosShared,
            MessageType.AUDIO to stats.totalAudiosSent,
            MessageType.VIDEO to stats.totalVideosSent,
            MessageType.STICKER to stats.totalStickers
        )

        return counts.maxByOrNull { it.value }?.key ?: MessageType.TEXT
    }

    /**
     * Calcula las rachas de conversación
     */
    private suspend fun calculateStreaks(): Pair<Int, Int> {
        val dailyStats = romanticDao.getRecentDailyStats().first()
        val today = LocalDate.now()

        var currentStreak = 0
        var longestStreak = 0
        var tempStreak = 0

        // Verificar racha actual
        for (i in 0 until 365) {
            val date = today.minusDays(i.toLong())
            val dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            val dayStats = romanticDao.getDailyStats(dateStr)

            val hasActivity = dayStats?.let {
                it.messagesSent > 0 || it.messagesReceived > 0 ||
                it.hugsSent > 0 || it.kissesSent > 0
            } ?: false

            if (hasActivity) {
                if (i == 0) currentStreak = 1
                tempStreak++
            } else {
                if (i > 0) break // Racha rota
            }
        }

        longestStreak = maxOf(tempStreak, currentStreak)

        return Pair(longestStreak, currentStreak)
    }

    /**
     * Actualiza la racha de conversación
     */
    private suspend fun updateStreak() {
        val stats = romanticDao.getLoveStatistics() ?: return
        val streaks = calculateStreaks()

        romanticDao.insertLoveStatistics(
            stats.copy(
                currentStreak = streaks.second,
                longestStreak = maxOf(stats.longestStreak, streaks.first),
                streakLastUpdated = System.currentTimeMillis()
            )
        )
    }

    /**
     * Obtiene los días de relación
     */
    private suspend fun getRelationshipDays(): Int {
        val stats = romanticDao.getLoveStatistics() ?: return 0
        val startDate = stats.relationshipStartDate ?: return 0

        val start = LocalDate.ofEpochDay(startDate / (24 * 60 * 60 * 1000))
        val days = ChronoUnit.DAYS.between(start, LocalDate.now())
        return days.toInt().coerceAtLeast(0)
    }

    /**
     * Obtiene las fechas especiales
     */
    private suspend fun getSpecialDates(): List<SpecialDate> {
        return romanticDao.getAllSpecialDates().first().map { entity ->
            SpecialDate(
                title = entity.title,
                date = LocalDate.ofEpochDay(entity.dateTimestamp / (24 * 60 * 60 * 1000)),
                type = SpecialDateType.valueOf(entity.type)
            )
        }
    }

    /**
     * Agrega una fecha especial
     */
    suspend fun addSpecialDate(title: String, date: LocalDate, type: SpecialDateType) {
        romanticDao.insertSpecialDate(
            SpecialDateEntity(
                title = title,
                dateTimestamp = date.toEpochDay() * 24 * 60 * 60 * 1000,
                type = type.name
            )
        )
    }

    /**
     * Establece la fecha de inicio de la relación
     */
    suspend fun setRelationshipStartDate(date: LocalDate) {
        val stats = romanticDao.getLoveStatistics() ?: LoveStatisticsEntity()
        romanticDao.insertLoveStatistics(
            stats.copy(
                relationshipStartDate = date.toEpochDay() * 24 * 60 * 60 * 1000,
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    // ==================== ESTADÍSTICAS DIARIAS ====================

    /**
     * Actualiza las estadísticas diarias
     */
    private suspend fun updateDailyStats(update: (DailyLoveStatsEntity) -> DailyLoveStatsEntity) {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        val todayStats = romanticDao.getDailyStats(today)

        if (todayStats == null) {
            romanticDao.insertDailyStats(
                update(
                    DailyLoveStatsEntity(date = today)
                )
            )
        } else {
            romanticDao.updateDailyStats(update(todayStats))
        }
    }

    /**
     * Obtiene estadísticas diarias para gráfico
     */
    suspend fun getDailyStatsForChart(days: Int = 30): List<DailyStats> {
        val today = LocalDate.now()
        val result = mutableListOf<DailyStats>()

        for (i in 0 until days) {
            val date = today.minusDays(i.toLong())
            val dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            val stats = romanticDao.getDailyStats(dateStr)

            result.add(
                DailyStats(
                    date = date,
                    messagesSent = stats?.messagesSent ?: 0,
                    messagesReceived = stats?.messagesReceived ?: 0,
                    hugsSent = stats?.hugsSent ?: 0,
                    kissesSent = stats?.kissesSent ?: 0
                )
            )
        }

        return result.reversed()
    }

    // ==================== LOGROS ====================

    /**
     * Obtiene la lista de logros disponibles
     */
    suspend fun getAchievements(): List<LoveAchievement> {
        val stats = getLoveStatistics()

        return listOf(
            LoveAchievement(
                id = "first_message",
                title = "Primer Mensaje",
                description = "Envía tu primer mensaje",
                icon = "💌",
                isUnlocked = stats.totalMessagesSent >= 1,
                progress = stats.totalMessagesSent,
                target = 1
            ),
            LoveAchievement(
                id = "century",
                title = "Siglo de Amor",
                description = "Envía 100 mensajes",
                icon = "💯",
                isUnlocked = stats.totalMessagesSent >= 100,
                progress = stats.totalMessagesSent,
                target = 100
            ),
            LoveAchievement(
                id = "hug_master",
                title = "Maestro de Abrazos",
                description = "Envía 50 abrazos",
                icon = "🤗",
                isUnlocked = stats.totalHugsSent >= 50,
                progress = stats.totalHugsSent,
                target = 50
            ),
            LoveAchievement(
                id = "kiss_expert",
                title = "Experto en Besos",
                description = "Envía 50 besos",
                icon = "💋",
                isUnlocked = stats.totalKissesSent >= 50,
                progress = stats.totalKissesSent,
                target = 50
            ),
            LoveAchievement(
                id = "note_writer",
                title = "Escritor de Amor",
                description = "Crea 10 notas de amor",
                icon = "📝",
                isUnlocked = stats.totalNotesCreated >= 10,
                progress = stats.totalNotesCreated,
                target = 10
            ),
            LoveAchievement(
                id = "streak_week",
                title = "Semana Consecutiva",
                description = "Mantén una racha de 7 días",
                icon = "🔥",
                isUnlocked = stats.longestStreak >= 7,
                progress = stats.longestStreak,
                target = 7
            ),
            LoveAchievement(
                id = "streak_month",
                title = "Mes Consecutivo",
                description = "Mantén una racha de 30 días",
                icon = "🏆",
                isUnlocked = stats.longestStreak >= 30,
                progress = stats.longestStreak,
                target = 30
            ),
            LoveAchievement(
                id = "anniversary",
                title = "Primer Aniversario",
                description = "Celebra 365 días juntos",
                icon = "🎉",
                isUnlocked = stats.relationshipDays >= 365,
                progress = stats.relationshipDays,
                target = 365
            )
        )
    }
}

// ==================== UI COMPOSABLE ====================

/**
 * Pantalla principal de estadísticas de amor
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoveStatisticsScreen(
    statisticsManager: LoveStatisticsManager,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var statistics by remember { mutableStateOf<LoveStatistics?>(null) }
    var dailyStats by remember { mutableStateOf<List<DailyStats>>(emptyList()) }
    var achievements by remember { mutableStateOf<List<LoveAchievement>>(emptyList()) }
    var showExportDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        statistics = statisticsManager.getLoveStatistics()
        dailyStats = statisticsManager.getDailyStatsForChart()
        achievements = statisticsManager.getAchievements()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "💕 Estadísticas de Amor",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFF0F5),
                    titleContentColor = Color(0xFF333333)
                ),
                actions = {
                    IconButton(onClick = { showExportDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Exportar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Días juntos
            statistics?.let { stats ->
                DaysTogetherCard(
                    days = stats.relationshipDays,
                    modifier = Modifier.fillMaxWidth()
                )

                // Tarjetas principales
                MainStatsGrid(
                    statistics = stats,
                    modifier = Modifier.fillMaxWidth()
                )

                // Gráfico de mensajes
                MessagesChartCard(
                    dailyStats = dailyStats,
                    modifier = Modifier.fillMaxWidth()
                )

                // Logros
                AchievementsList(
                    achievements = achievements,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    if (showExportDialog && statistics != null) {
        ExportStatsDialog(
            statistics = statistics!!,
            onDismiss = { showExportDialog = false },
            onExport = { /* Implementar exportación */ }
        )
    }
}

/**
 * Tarjeta de días juntos
 */
@Composable
fun DaysTogetherCard(
    days: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFF6B6B)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "💕",
                    fontSize = 48.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$days",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "días juntos",
                    fontSize = 18.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

/**
 * Grid de estadísticas principales
 */
@Composable
fun MainStatsGrid(
    statistics: LoveStatistics,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Fila 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Mensajes",
                value = "${statistics.totalMessagesSent + statistics.totalMessagesReceived}",
                icon = Icons.Default.Message,
                gradient = listOf(Color(0xFF4FC3F7), Color(0xFF0288D1)),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Abrazos",
                value = "${statistics.totalHugsSent}",
                icon = Icons.Default.Favorite,
                gradient = listOf(Color(0xFFFFB74D), Color(0xFFF57C00)),
                modifier = Modifier.weight(1f)
            )
        }

        // Fila 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Besos",
                value = "${statistics.totalKissesSent}",
                icon = Icons.Default.Favorite,
                gradient = listOf(Color(0xFFF06292), Color(0xFFE91E63)),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Racha",
                value = "${statistics.currentStreak}🔥",
                icon = Icons.Default.LocalFireDepartment,
                gradient = listOf(Color(0xFFFF8A65), Color(0xFFFF5722)),
                modifier = Modifier.weight(1f)
            )
        }

        // Fila 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Notas",
                value = "${statistics.totalNotesCreated}",
                icon = Icons.Default.Edit,
                gradient = listOf(Color(0xFFBA68C8), Color(0xFF9C27B0)),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Fotos",
                value = "${statistics.totalPhotosShared}",
                icon = Icons.Default.Photo,
                gradient = listOf(Color(0xFF81C784), Color(0xFF4CAF50)),
                modifier = Modifier.weight(1f)
            )
        }

        // Estadísticas adicionales
        AdditionalStatsRow(statistics = statistics)
    }
}

/**
 * Tarjeta de estadística individual
 */
@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradient: List<Color>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(1.2f)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(colors = gradient)
                )
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = Color.White.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Column {
                    Text(
                        text = value,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}

/**
 * Fila de estadísticas adicionales
 */
@Composable
fun AdditionalStatsRow(
    statistics: LoveStatistics,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "📊 Estadísticas Detalladas",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            Divider(color = Color(0xFFE0E0E0))

            StatRowItem("Promedio mensajes/día", String.format("%.1f", statistics.averageMessagesPerDay))
            StatRowItem("Palabra romántica", statistics.mostUsedRomanticWord.ifEmpty { "—" })
            StatRowItem("Tipo favorito", statistics.favoriteMessageType.name)
            StatRowItem("Racha más larga", "${statistics.longestStreak} días")
            StatRowItem("Audios enviados", "${statistics.totalAudiosSent}")
            StatRowItem("Videos enviados", "${statistics.totalVideosSent}")
            StatRowItem("Stickers", "${statistics.totalStickers}")
            StatRowItem("Enlaces", "${statistics.totalLinks}")
        }
    }
}

@Composable
fun StatRowItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF666666)
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF333333)
        )
    }
}

/**
 * Tarjeta con gráfico de mensajes
 */
@Composable
fun MessagesChartCard(
    dailyStats: List<DailyStats>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "📈 Actividad de Mensajes (Últimos 30 días)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Gráfico simple usando Canvas
            if (dailyStats.isNotEmpty()) {
                MessagesChart(
                    dailyStats = dailyStats,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Sin datos aún",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

/**
 * Gráfico de mensajes usando Canvas
 */
@Composable
fun MessagesChart(
    dailyStats: List<DailyStats>,
    modifier: Modifier = Modifier
) {
    val maxMessages = dailyStats.maxOfOrNull {
        it.messagesSent + it.messagesReceived
    } ?: 1

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val barWidth = width / dailyStats.size.coerceAtLeast(1)
        val spacing = 4.dp.toPx()

        dailyStats.forEachIndexed { index, stats ->
            val totalMessages = stats.messagesSent + stats.messagesReceived
            val barHeight = (totalMessages.toFloat() / maxMessages) * (height - 20.dp.toPx())

            val x = index * barWidth + spacing
            val y = height - barHeight

            // Dibujar barra
            drawRect(
                color = Color(0xFFFF6B6B),
                topLeft = androidx.compose.ui.geometry.Offset(x, y),
                size = androidx.compose.ui.geometry.Size(
                    barWidth - spacing * 2,
                    barHeight
                ),
                style = androidx.compose.ui.graphics.drawscope.Fill
            )
        }

        // Línea base
        drawLine(
            color = Color.Gray,
            start = androidx.compose.ui.geometry.Offset(0f, height - 2.dp.toPx()),
            end = androidx.compose.ui.geometry.Offset(width, height - 2.dp.toPx()),
            strokeWidth = 1.dp.toPx()
        )
    }
}

/**
 * Lista de logros
 */
@Composable
fun AchievementsList(
    achievements: List<LoveAchievement>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "🏆 Logros",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(12.dp))

            achievements.forEach { achievement ->
                AchievementItem(
                    achievement = achievement,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun AchievementItem(
    achievement: LoveAchievement,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = achievement.icon,
            fontSize = 28.sp,
            modifier = Modifier.size(40.dp),
            color = if (achievement.isUnlocked) Color.Unspecified else Color.Gray
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = achievement.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (achievement.isUnlocked) Color(0xFF333333) else Color.Gray
            )
            Text(
                text = achievement.description,
                fontSize = 11.sp,
                color = Color.Gray
            )

            // Barra de progreso
            if (!achievement.isUnlocked) {
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = (achievement.progress.toFloat() / achievement.target).coerceIn(0f, 1f),
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFFF6B6B),
                    trackColor = Color(0xFFFFE0E0)
                )
                Text(
                    text = "${achievement.progress}/${achievement.target}",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }

        if (achievement.isUnlocked) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Desbloqueado",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

/**
 * Diálogo de exportación
 */
@Composable
fun ExportStatsDialog(
    statistics: LoveStatistics,
    onDismiss: () -> Unit,
    onExport: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Exportar Estadísticas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text("¿Cómo quieres exportar tus estadísticas de amor?")
                Spacer(modifier = Modifier.height(16.dp))

                ExportOptionItem(
                    icon = Icons.Default.Image,
                    title = "Como imagen",
                    description = "Genera una imagen compartible"
                )
                ExportOptionItem(
                    icon = Icons.Default.Description,
                    title = "Como texto",
                    description = "Copia el resumen en texto"
                )
            }
        },
        confirmButton = {
            Button(onClick = onExport) {
                Text("Exportar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun ExportOptionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFFFF6B6B),
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}

/**
 * Genera un Bitmap con las estadísticas para exportar
 */
fun generateStatisticsBitmap(
    statistics: LoveStatistics,
    context: Context
): Bitmap {
    val width = 1080
    val height = 1920

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // Colores
    val backgroundColor = Color(0xFFFFF0F5).toArgb()
    val primaryColor = Color(0xFFFF6B6B).toArgb()
    val textColor = Color(0xFF333333).toArgb()

    // Pintar fondo
    canvas.drawColor(backgroundColor)

    // Paint para texto
    val titlePaint = Paint().apply {
        color = primaryColor
        textSize = 72f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
    }

    val textPaint = Paint().apply {
        color = textColor
        textSize = 48f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textAlign = Paint.Align.CENTER
    }

    val labelPaint = Paint().apply {
        color = textColor
        textSize = 36f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textAlign = Paint.Align.LEFT
    }

    // Título
    canvas.drawText("💕 Estadísticas de Amor", width / 2f, 150f, titlePaint)

    // Días juntos
    canvas.drawText(
        "${statistics.relationshipDays} días juntos",
        width / 2f,
        280f,
        textPaint
    )

    // Estadísticas
    var yPosition = 450f
    val statsList = listOf(
        "Mensajes enviados" to statistics.totalMessagesSent.toString(),
        "Mensajes recibidos" to statistics.totalMessagesReceived.toString(),
        "Abrazos enviados" to statistics.totalHugsSent.toString(),
        "Besos enviados" to statistics.totalKissesSent.toString(),
        "Notas creadas" to statistics.totalNotesCreated.toString(),
        "Fotos compartidas" to statistics.totalPhotosShared.toString(),
        "Racha actual" to "${statistics.currentStreak} días",
        "Racha más larga" to "${statistics.longestStreak} días"
    )

    statsList.forEach { (label, value) ->
        canvas.drawText(label, 100f, yPosition, labelPaint)
        canvas.drawText(value, width - 100f, yPosition, textPaint)
        yPosition += 100f
    }

    // Fecha
    val datePaint = Paint().apply {
        color = Color.Gray.toArgb()
        textSize = 32f
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText(
        "Generado el ${LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
        width / 2f,
        height - 100f,
        datePaint
    )

    return bitmap
}
