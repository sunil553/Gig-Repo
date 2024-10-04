package com.llyod.domain.model

data class ErrorResponseData(
    val message: Message,
    val status: Int
)