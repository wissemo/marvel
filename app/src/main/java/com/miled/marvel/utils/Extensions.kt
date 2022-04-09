package com.miled.marvel.utils

import com.miled.marvel.R
import com.miled.marvel.core.exceptions.CashException
import com.miled.marvel.core.exceptions.NoConnectivityException
import com.miled.marvel.core.exceptions.ServerException

fun Exception.mapExceptionToStringId(): Int =
    when (this) {
        is ServerException -> R.string.errors_server_failed
        is CashException -> R.string.errors_cash_failed
        is NoConnectivityException -> R.string.errors_no_connexion
        else -> R.string.errors_general
    }

