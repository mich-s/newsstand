package com.example.newsstand.database.entity

import com.example.newsstand.domain.Source


data class SourceDb(val sourceId: String?,
                    val name: String?)

fun SourceDb.asDomainModel(): Source{
    return Source(sourceId, name)
}