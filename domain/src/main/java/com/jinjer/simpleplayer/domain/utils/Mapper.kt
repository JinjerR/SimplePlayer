package com.jinjer.simpleplayer.domain.utils

abstract class Mapper<in T,E> {
    abstract fun from(element: T): E

    open fun fromList(list: List<T>): List<E> = list.map { from(it) }
}