package com.example.timezoneaware

import com.example.timezoneaware.concurrency.IAppDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
object TestDispatchers : IAppDispatchers {
    override val main: CoroutineDispatcher
        get() = Dispatchers.Unconfined

    override val io: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}