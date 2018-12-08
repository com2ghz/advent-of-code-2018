package day1

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val result = Files.readAllLines(Paths.get("resources/day1/day1.txt")).stream()
        .mapToInt(Integer::parseInt)
        .reduce { left, right -> left + right }
        .asInt
    println(result)
}