package com.willow.todolist.Models

data class Mission(
    val id: Int,
    val content: String,
    val time: String,
    val date: String,
    val status: Int?
)
