package vlnny.base.ext


fun <E> List<E>.updateFrom(
    list: List<E>,
    ruleBlock: (E, E) -> Boolean
): List<E> {
    val tmpList = mutableListOf<E>()

    forEach { item1 ->
        val item = list.find { item2 ->
            ruleBlock(item1, item2)
        } ?: item1

        tmpList.add(item)
    }
    return tmpList
}

inline fun <P, reified S : P> List<P>.findAllBySubType(): List<S> {
    val found = mutableListOf<S>()
    this.forEach { item ->
        if (item is S)
            found.add(item)
    }
    return found
}
