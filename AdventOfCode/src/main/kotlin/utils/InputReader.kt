package utils

object InputReader {

    fun readFileAsList(name: String): List<String> = readFile(name).split("\n").filter { it.isNotBlank() }

    fun readFileAsListWithBlanks(name: String): List<String> = readFile(name).split("\n")

    fun readFileAsMatrix(name: String): Array<Array<Char>> =
        readFileAsList(name).map { it.toCharArray().toTypedArray() }.toTypedArray()

    fun readFileAsString(name: String): String = readFile(name).trim()

    private fun readFile(name: String) = javaClass.getResource("/input/$name")?.readText()
        ?: throw IllegalArgumentException("Unable to find input file: $name")

}
