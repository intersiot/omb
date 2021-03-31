package com.intersiot.ohmybank.model

data class TransactDTO (
    var transfer: Map<String, DepositAndWithdrawal> = HashMap())
{
    data class DepositAndWithdrawal (
        var account: String? = null,
        var deposit: Int? = 0,
        var withdrawal: Int? = 0,
        var timestamp: Long? = 0,
        var cache: Int? = 0)
}
