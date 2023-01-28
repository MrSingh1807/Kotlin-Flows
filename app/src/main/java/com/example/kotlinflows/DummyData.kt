package com.example.kotlinflows

import com.example.kotlinflows.model.Notes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow

class DummyData {

    fun getNotes()= flow<Notes> {
        val list = listOf(
            Notes(1, true, "First", "First Description"),
            Notes(2, true, "Second", "Second Description"),
            Notes(3, true, "Third", "Third Description"),
            Notes(4, true, "Four", "Four Description"),
            Notes(5, true, "Five", "Five Description"),
            Notes(6, true, "Six", "Six Description"),
            Notes(7, true, "Seven", "Seven Description"),
            Notes(8, true, "Eight", "Eight Description")
        )

        list.forEach {
            delay(1000) // Producing time
            emit(it)
        }
    }
}