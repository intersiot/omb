package com.intersiot.ohmybank.model

data class TransactDTO (var id: String ?= null,
                        var account: String ?= null,
                        var cache: Int = 0,
                        var deposit: Int = 0,
                        var withdrawal: Int = 0,
                        var timestamp: Long = 0)
