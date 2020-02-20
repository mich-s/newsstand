package com.example.newsstand.network.dto

import com.example.newsstand.database.entity.SourceDb
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SourceNet(val id: String?,
                     val name: String?)

fun SourceNet.asDatabaseObject(): SourceDb{
    return SourceDb(id, name)
}