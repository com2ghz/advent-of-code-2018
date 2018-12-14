package day4

import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

val DATE_PATTERN = """\[(.*)] (.*)""".toRegex()
val GUARD_PATTERN = """Guard #(\d+) begins shift""".toRegex()
val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

fun main() {
    val rawLogEntries = Files.readAllLines(Paths.get("resources/input/day4.txt"))
    val sortedMap = createSortedMap(rawLogEntries)
    val logEntriesPerMinuteMap = createLogEntryMinuteMap(sortedMap)

    val mostSleepingGuardId = logEntriesPerMinuteMap.filter { it.status == Status.SLEEP }
        .groupBy { it.guardId }
        .maxBy { it.value.size }
        ?.key

    val sleptMostMinute = logEntriesPerMinuteMap
        .filter { it.status == Status.SLEEP }
        .filter { it.guardId == mostSleepingGuardId }
        .groupBy { it.date.minute }
        .maxBy { it.value.size }
        ?.key

    val mostMinuteTotalSlept = logEntriesPerMinuteMap
        .filter { it.status == Status.SLEEP }
        .groupBy { it.date.minute }
        .maxBy { it.value.size }
        ?.key

    val entryWhoSleptMostMin = logEntriesPerMinuteMap
        .filter { it.status == Status.SLEEP }
        .filter { it.date.minute == mostMinuteTotalSlept }
        .groupBy { it.guardId }
        .maxBy { it.value.size }


    println("Part one: Guard who most slept: $mostSleepingGuardId")
    println("Part one: Slept mostly on minute:  $sleptMostMinute")
    println(mostSleepingGuardId!! * sleptMostMinute!!)

    println("Part two: Guard most same minute: $mostSleepingGuardId")
    println("Part two: Slept mostly on minute:  $mostMinuteTotalSlept")
    println(entryWhoSleptMostMin!!.key * mostMinuteTotalSlept!!)
}

fun createSortedMap(rawLogEntries: List<String>): Map<LocalDateTime, String> {
    val dateEntryMap = HashMap<LocalDateTime, String>().toSortedMap()
    rawLogEntries.forEach {
        val dateTimeStr = DATE_PATTERN.find(it)?.groups!![1]?.value
        val localDateTime = LocalDateTime.parse(dateTimeStr, DATE_FORMATTER)
        dateEntryMap[localDateTime] = DATE_PATTERN.find(it)?.groups!![2]?.value
    }
    return dateEntryMap
}

fun createLogEntryMinuteMap(sortedMap: Map<LocalDateTime, String>): ArrayList<LogEntry> {

    var guardId: Int? = null
    val logEntries = sortedMap.map { entry ->
        val raw = entry.value

        var status: Status? = null

        when {
            GUARD_PATTERN.matches(raw) -> {
                guardId = GUARD_PATTERN.find(raw)!!.groups[1]!!.value.toInt()
                status = Status.BEGIN_SHIFT
            }
            "wakes up" == raw -> status = Status.WAKE_UP
            "falls asleep" == raw -> status = Status.SLEEP
        }

        LogEntry(entry.key, guardId!!, status!!)
    }

    var lastRecord: LogEntry? = null

    val logEntryMinuteMap = ArrayList<LogEntry>()
    logEntries.forEach { logEntry: LogEntry ->
        if (lastRecord != null) {
            val diffMinutes = lastRecord!!.date.until(logEntry.date, ChronoUnit.MINUTES)

            for (i in 0 until diffMinutes) {
                logEntryMinuteMap.add(LogEntry(lastRecord!!.date.plusMinutes(i), lastRecord!!.guardId, lastRecord!!.status))
            }
        }

        lastRecord = logEntry
    }

    return logEntryMinuteMap
}

data class LogEntry(val date: LocalDateTime, val guardId: Int, val status: Status)

enum class Status {
    BEGIN_SHIFT, SLEEP, WAKE_UP
}
