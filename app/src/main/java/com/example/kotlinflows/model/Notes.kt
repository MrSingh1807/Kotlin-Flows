package com.example.kotlinflows.model

data class Notes(
    val id: Int,
    val isActive: Boolean,
    val title : String,
    val description: String
)


data class FormattedNote(
    val isActive: Boolean,
    val title : String,
    val description: String
    )
