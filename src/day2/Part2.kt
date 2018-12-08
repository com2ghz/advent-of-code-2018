package day2

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val boxIds = Files.readAllLines(Paths.get("resources/input/day2.txt"))

    for (i in boxIds.indices) {
        for (j in boxIds.indices) {
            var result = checkIfSameChar(boxIds[i], boxIds[j])
            if (result != null) {
                println(result)
            }
        }
    }
}

fun checkIfSameChar(firstId: String, secondId: String): String? {
    if (firstId.equals(secondId)) {
        return null
    }

    var res = ""
    var unmatchedChar = false

    for (i in firstId.toCharArray().indices) {
        if (firstId[i] != secondId[i]) {
            if (unmatchedChar) {
                return null
            }
            unmatchedChar = true
        } else {
            res+=firstId[i]
        }

    }
    return res
}
