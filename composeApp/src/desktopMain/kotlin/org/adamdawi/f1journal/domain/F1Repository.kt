package org.adamdawi.f1journal.domain

import org.adamdawi.f1journal.domain.util.DataError
import org.adamdawi.f1journal.domain.util.Result

interface F1Repository {
    fun sendF1Data(): Result<String, DataError.Network>
}