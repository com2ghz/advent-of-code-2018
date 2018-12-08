package day2

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    var twoCharacters = 0
    var threeCharacters = 0

    Files.readAllLines(Paths.get("resources/input/day2.txt"))
        .map { createCharacterCountMap(it) }
        .forEach {letterMap ->
            var twoFound = false
            var threeFound = false
            letterMap.values.forEach{ count ->
                if (!twoFound && count == 2) {
                    twoCharacters++
                    twoFound = true
                } else if (!threeFound && count == 3) {
                    threeCharacters++
                    threeFound = true
                }

            }
        }
    println(twoCharacters * threeCharacters)

}

fun createCharacterCountMap(code: String) : Map<Char, Int> {
    val letterMap = HashMap<Char, Int>()
    code.toCharArray().forEach { letter ->
        if (letterMap.containsKey(letter)) {
            val count = letterMap[letter]
            letterMap[letter] = count!!.inc()
        } else {
            letterMap[letter] = 1
        }
    }
    return letterMap
}