package utils

fun List<Long>.lcm(): Long {
    var result = get(0)
    for (i in 1..<size) {
        result = lcm(result, get(i))
    }
    return result
}

fun lcm(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

fun Double.format(scale: Int) = "%.${scale}f".format(this)

fun Double.pct(decimals: Int = 1) = (this * 100).format(decimals)
