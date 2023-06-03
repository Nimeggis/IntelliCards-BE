import kotlin.math.abs

object ContentTransformations {

    @JvmStatic
    fun filterOutShortPages(contents: MutableMap<Int, List<String>>, minLength: Int = 100): MutableMap<Int, List<String>> {
        var keysToRemove = emptyList<Int>()
        contents.forEach{ entry ->
            if(entry.value.joinToString(" ").length < minLength) {
                keysToRemove = keysToRemove + entry.key
            }
        }

        keysToRemove.forEach { key ->
            contents.remove(key)
        }

        return contents
    }

    @JvmStatic
    fun filterOutRecurringText(contents: MutableMap<Int, List<String>>): MutableMap<Int, List<String>> {
        var keysToRemove = emptyList<Int>()

        for (i in 1..3) {
            val uniqueSentences = mutableSetOf<String>()
            contents.forEach{ entry ->
                if(entry.value.size > i) {
                    uniqueSentences.add(entry.value[i])
                }
            }

            if(uniqueSentences.size == 1) {
                keysToRemove = keysToRemove + i
            }
        }

        for (i in -1 downTo -3) {
            val uniqueSentences = mutableSetOf<String>()
            for(entry in contents) {
                if(entry.value.size <= 3 + abs(i)) {
                    break;
                }
                if(entry.value.size >= abs(i)) {
                    uniqueSentences.add(entry.value[i])
                }
            }

            if(uniqueSentences.size == 1) {
                keysToRemove = keysToRemove + i
            }
        }

        contents.forEach{ entry ->
            if(entry.value.joinToString(" ").length < minLength) {
                keysToRemove = keysToRemove + entry.key
            }
        }

        keysToRemove.forEach { key ->
            contents.remove(key)
        }

        return contents
    }

}