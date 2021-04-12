package com.intersiot.ohmybank.model

data class TransactDTO (var id: String ?= null,
                        var account: String ?= null,
                        var payment: Int = 0,
                        var cache: Int = 0,
                        var timestamp: Long = 0)
