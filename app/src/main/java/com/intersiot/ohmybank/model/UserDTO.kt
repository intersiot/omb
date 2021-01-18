package com.intersiot.ohmybank.model

data class UserDTO (
    var id: String ?= null,
    var uid: String ?= null,
    var name: String ?= null,
    var phone: String ?= null,
    var account: String ?= null,
    var cache: Int ?= null,
    var promotion: Boolean ?= null
)
