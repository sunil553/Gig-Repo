package com.llyod.data.common

interface Mapper<F, T> {
    fun mapFrom(from : F) : T

    fun mapTo(from : T) : F
}