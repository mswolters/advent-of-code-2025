import kotlin.math.sqrt

/**
 * Returns a list of all prime factors and exponents of this.
 */
fun Long.primeFactors(): List<Pair<Long, Int>> {
    //Unoptimized prime factorization
    //optimizations could include only checking odd numbers
    return sequence {
        var current = this@primeFactors
        var divisor = 2L
        while (current > 1 && divisor * divisor <= current) {
            var numberOfTimes = 0
            while (current % divisor == 0L) {
                numberOfTimes++
                current /= divisor
            }
            if (numberOfTimes > 0) {
                yield(divisor to numberOfTimes)
            }
            divisor++
        }
        // If current is prime, yield it
        if (current > 1) {
            yield(current to 1)
        }
    }.toList()
}

fun Int.primeFactors(): List<Pair<Int, Int>> {
    return (this.toLong()).primeFactors().map { (factor, exponent) -> factor.toInt() to exponent }
}

fun Long.divisors(): List<Long> {
    if (this == 1L) return listOf(1L)
    return this.primeFactors()
        .map { (factor, exponent) -> (0..exponent).map { factor.pow(it) } }
        .cartesianProduct()
        .map { it.reduce { a, b -> a * b } }
        .sorted()
}

fun Int.divisors(): List<Int> {
    return (this.toLong()).divisors().map { it.toInt() }
}

fun Long.numberOfDivisors(): Int {
    if (this == 1L) return 1
    return this.primeFactors().map { (_, exponent) -> exponent + 1 }.multiplied()
}

fun Int.numberOfDivisors(): Int {
    return (this.toLong()).numberOfDivisors()
}

// Return all primes up to limit
fun primes(limit: Int): List<Int> {
    // Simple prime sieve
    val sieve = BooleanArray(limit + 1)
    val primes = mutableListOf<Int>()
    for (i in 2..limit) {
        if (!sieve[i]) {
            primes.add(i)
            for (j in i..limit step i) {
                sieve[j] = true
            }
        }
    }
    return primes
}

fun isPrime(n: Int): Boolean {
    if (n < 2) return false
    if (n < 4) return true
    if (n % 2 == 0) return false
    for (i in 3..sqrt(n.toFloat()).toInt() step 2) {
        if (n % i == 0) return false
    }
    return true
}
