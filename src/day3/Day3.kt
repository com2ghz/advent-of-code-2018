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

    val overlappingClaims = ArrayList<Claim>()
    for (y in canvas.indices) {
        for (x in canvas[y].indices) {
            if (canvas[y][x] != null && canvas[y][x]!!.size > 1) {
                canvas[y][x]?.forEach {
                    overlappingClaims.add(it)
                }
            }
        }
    }

    println(overlappingClaims)

}

fun createCanvas(claims: List<Claim>): Array<Array<ArrayList<Claim>?>> {
    var maxWidth = 0
    var maxHeight = 0


    claims.forEach { claim: Claim ->
        val width = claim.leftMargin + claim.width + 1
        val height = claim.topMargin + claim.topMargin + 1
        maxWidth = maxOf(maxWidth, width)
        maxHeight = maxOf(maxHeight, height)
    }

    val canvas = Array(maxHeight) { i -> arrayOfNulls<ArrayList<Claim>>(maxWidth) }
    return canvas
}


fun fillCanvas(canvas: Array<Array<ArrayList<Claim>?>>, claim: Claim): Array<Array<ArrayList<Claim>?>> {
    for (y in claim.topMargin..claim.topMargin + claim.topMargin) {
        for (x in claim.leftMargin..claim.leftMargin + claim.width) {
            val sector = canvas[y][x]
            if (sector == null) {
                canvas[y][x] = arrayListOf(claim)
            } else {
                canvas[y][x]?.add(claim)
            }
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