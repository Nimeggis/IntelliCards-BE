package api.service

import org.springframework.stereotype.Service
import kotlin.math.abs

@Service
class ContentTransformationsService {

    fun filterOutShortPages(contents: Map<Int, List<String>>, minLength: Int = 100): Map<Int, List<String>> {
        var keysToRemove = emptyList<Int>()
        contents.forEach{ entry ->
            if(entry.value.joinToString(" ").length < minLength) {
                keysToRemove = keysToRemove + entry.key
            }
        }

        return contents.filterKeys { it !in keysToRemove }
    }

    fun filterOutRecurringText(contents: Map<Int, List<String>>,  minLength: Int = 100): Map<Int, List<String>> {
        val keysToRemove = mutableListOf<Int>()

        repeat(3) {len ->
            val firstDistinct = contents.values.filter { it.size > len}.map {
                it[len]
            }.toSet().size == 1
            if (firstDistinct) {
                keysToRemove += len
            }
            val lastDistinct = contents.values.filter { it.size > len}.map {
                it.reversed()[len]
            }.toSet().size == 1
            if (lastDistinct) {
                keysToRemove += -len - 1
            }
        }

        return contents.mapValues { (_, page) ->
            page.filterIndexed { i, _ ->
                !keysToRemove.contains(i) && !keysToRemove.contains(-(page.size - i))
            }
        }
    }

}