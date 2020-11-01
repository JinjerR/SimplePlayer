package com.jinjer.simpleplayer.domain.utils

abstract class Mapper<in T,E> {
    abstract suspend fun from(element: T): E

    open suspend fun fromList(list: List<T>): List<E> = list.map { from(it) }
}