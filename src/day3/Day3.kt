package day3

import java.lang.IllegalArgumentException
import java.nio.file.Files
import java.nio.file.Paths

val CLAIM_PATTERN = """#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""".toRegex()
//#1 @ 335,861: 14x10
fun main() {

    val claims = Files.readAllLines(Paths.get("resources/input/day3.txt"))
        .map { parseClaim(it) }

    val canvas = createCanvas(claims)
    claims.forEach { claim: Claim ->
        fillCanvas(canvas, claim)
    }

    var overlappingInches = 0
    for (y in canvas.indices) {
        for (x in canvas[y].indices) {
            if (canvas[y][x] > 1) {
                overlappingInches++
            }
        }
    }

    println(overlappingInches)

}

fun createCanvas(claims: List<Claim>): Array<Array<Int>> {
    var maxWidth = 0
    var maxHeight = 0


    claims.forEach { claim: Claim ->
        val width = claim.leftMargin + claim.width
        val height = claim.topMargin + claim.topMargin
        maxWidth = maxOf(maxWidth, width)
        maxHeight = maxOf(maxHeight, height)
    }

    val canvas = Array(maxHeight+1) { i -> Array(maxWidth+1) { j -> 0} }
    return canvas
}


fun fillCanvas(canvas: Array<Array<Int>>, claim: Claim): Array<Array<Int>> {
    for (y in claim.topMargin+1..(claim.topMargin + claim.height)) {
        for (x in claim.leftMargin+1..(claim.leftMargin + claim.width)) {
            canvas[y][x]++
        }
    }

    return canvas
}

fun parseClaim(rawClaim: String): Claim {
    CLAIM_PATTERN.findAll(rawClaim).forEach { matchResult ->
        return Claim(id = Integer.parseInt(matchResult.groups[1]?.value),
            leftMargin = Integer.parseInt(matchResult.groups[2]?.value),
            topMargin = Integer.parseInt(matchResult.groups[3]?.value),
            width = Integer.parseInt(matchResult.groups[4]?.value),
            height = Integer.parseInt(matchResult.groups[5]?.value)
            )
    }
    throw IllegalArgumentException("Could not parse claim")
}

data class Claim(val id: Int, val leftMargin: Int, val topMargin: Int, val width: Int, val height: Int)