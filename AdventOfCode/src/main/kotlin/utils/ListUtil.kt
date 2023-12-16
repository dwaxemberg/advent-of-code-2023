package utils

fun List<List<*>>.print(): List<List<*>> {
    forEach {
        it.forEach {
            print(it)
        }
        println()
    }
    return this
}
