package com.del.notesappdel.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.del.notesappdel.Database.NoteDatabase
import com.del.notesappdel.Database.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application){
    private val repository : NotesRepository

    val allnotes : LiveData<List<Note>>


    //Modelos de la base de datos, crud - create, read, update and delete
    init{
        val dao = NoteDatabase.getDatabase(application).getNoteDao()
        repository = NotesRepository(dao)
        allnotes = repository.allNotes
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(note)
    }

    fun insertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    fun updateNote(note:Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }


}