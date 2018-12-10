package day1

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val sequence = Files.readAllLines(Paths.get("resources/input/day1.txt")).stream()
        .mapToInt(Integer::parseInt)
        .toArray()

    val frequency = findFrequency(sequence)
    println(frequency)
}

fun findFrequency(sequence: IntArray): Int {
    val prev = mutableSetOf<Int>()
    var frequency = 0

    while (true) {
        for (i in sequence) {
            frequency += i
            if (prev.contains(frequency)) {
                return frequency
            }
            prev+= frequency
        }
    }
}
