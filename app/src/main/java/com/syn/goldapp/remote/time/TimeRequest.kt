package com.syn.goldapp.remote.time

import com.syn.goldapp.remote.dataModel.timeModel.DateModel

interface TimeRequest {

    fun onSuccess(data: DateModel)

    fun onNotSuccess(message: String)

    fun onError(error: Throwable)
}