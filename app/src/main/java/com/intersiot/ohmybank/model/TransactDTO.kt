package com.intersiot.ohmybank.model

data class TransactDTO (
        var users: Map<String, String> = HashMap(),
        var name: Map<String, String> = HashMap(),
        var transacts: Map<String, Transact> = HashMap())
{
    data class Transact (
            var id: String? = null,
            var account: String? = null,
            var deposit: Int? = 0,
            var withdrawal: Int? = 0,
            var timestamp: Long ?= 0
    )
}
