package umc.link.zip.util.extension

inline fun CharSequence.takeWhileIndexed(predicate: (Int, Char) -> Boolean): String {
    val sb = StringBuilder()
    for ((index, element) in this.withIndex()) {
        if (!predicate(index, element)) break
        sb.append(element)
    }
    return sb.toString()
}