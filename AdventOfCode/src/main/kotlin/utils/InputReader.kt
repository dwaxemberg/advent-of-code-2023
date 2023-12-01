package utils

object InputReader {

    fun readFileAsList(name: String): List<String> {
        return javaClass.getResource("/input/$name")?.readText()?.split("\n")
            ?: throw IllegalArgumentException("Unable to find input file: $name")
    }
}
