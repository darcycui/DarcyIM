package com.darcy.kotlin.server.demowebsocket.exception.user

import java.util.function.Supplier

class UserExceptionSupplier(
    private val exception: UserException
) : Supplier<UserException> {
    override fun get(): UserException {
        return exception
    }
}