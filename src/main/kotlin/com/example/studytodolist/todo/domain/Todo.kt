package com.example.studytodolist.todo.domain

class Todo(
    val id: Long,
    var title: String,
    var content: String,
    var progress: Progress
)