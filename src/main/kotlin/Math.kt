import java.math.BigInteger

fun Int.pow(exponent: Int): Int = toBigInteger().pow(exponent).toInt()
fun Long.pow(exponent: Int): Long = toBigInteger().pow(exponent).toLong()

fun gcd(a: Int, b: Int): Int {
    var mutA = a
    var mutB = b
    while (mutB != 0) {
        val temp = mutA % mutB
        mutA = mutB
        mutB = temp
    }
    return mutA
}

fun lcm(a: Int, b: Int): Int {
    return (a * b) / gcd(a, b)
}

fun lcm(numbers: List<Int>): Int {
    return numbers.reduce { acc, i -> lcm(acc, i) }
}

fun gcd(a: Long, b: Long): Long {
    var mutA = a
    var mutB = b
    while (mutB != 0L) {
        val temp = mutA % mutB
        mutA = mutB
        mutB = temp
    }
    return mutA
}

fun lcm(a: Long, b: Long): Long {
    return (a * b) / gcd(a, b)
}

fun lcm(numbers: List<Long>): Long {
    return numbers.reduce { acc, i -> lcm(acc, i) }
}

fun Iterable<Long>.multiplied(): Long {
    return this.reduce { acc, i -> acc * i }
}
fun Iterable<Int>.multiplied(): Int {
    return this.reduce { acc, i -> acc * i }
}

fun Int.factorial(): BigInteger {
    return (2..this).fold(BigInteger.ONE) { acc, i -> acc.multiply(i.toBigInteger()) }
}

fun Int.properDivisors(): List<Int> {
    return (1..this / 2).filter { this % it == 0 }
}

fun Int.digits(): List<Int> {
    /*if (this == 0) return listOf()
    return (this / 10).digits() + (this % 10)*/
    return toString().toCharArray().map { it.digitToInt() }
}

fun Long.digits(): List<Int> {
    /*if (this == 0) return listOf()
    return (this / 10).digits() + (this % 10)*/
    return toString().toCharArray().map { it.digitToInt() }
}