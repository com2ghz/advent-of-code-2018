package day5

import java.lang.StringBuilder
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val polymer = Files.readAllLines(Paths.get("resources/input/day5.txt"))[0]

    val result = check(polymer)

    println(result.length)

    val resultMap = mutableMapOf<Char, Int>()
    polymer.toLowerCase()
        .toSet()
        .forEach { letter ->
            val result = polymer.replace(letter.toString(), "", true)
            resultMap[letter] = check(result).length
        }

    val entry = resultMap.minBy { it.value }
    println(entry)
}

fun check(polymer: String): String {
    var newPolymer: String?
    for (i in 0..(polymer.length-2)) {
        val char1 = polymer[i]
        val char2 = polymer[i+1]

        val sameLetter = char1.equals(char2, true)
        val bothUpperCase = char1.isUpperCase() && char2.isUpperCase()
        val bothLowerCase = char1.isLowerCase() && char2.isLowerCase()
        if (!sameLetter || bothUpperCase || bothLowerCase) {
            continue
        }
        newPolymer = StringBuilder(polymer).delete(i, i+2).toString()
        return check(newPolymer)
    }
    return polymer
}