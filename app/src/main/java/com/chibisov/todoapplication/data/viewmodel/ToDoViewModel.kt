package com.chibisov.todoapplication.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.chibisov.todoapplication.data.ToDoDatabase
import com.chibisov.todoapplication.data.model.ToDoData
import com.chibisov.todoapplication.data.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application): AndroidViewModel(application) {

    private val toDoDao = ToDoDatabase.getDatabase(application).toDoDao()
    private val toDoRepository: ToDoRepository = ToDoRepository(toDoDao)

    val getAllData: LiveData<List<ToDoData>> = toDoRepository.getAllData

    fun insertData(toDoData: ToDoData){
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.insertData(toDoData)
        }
    }

    fun updateData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.updateData(toDoData)
        }
    }

    fun delete(data: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.delete(data)
        }
    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.deleteAll()
        }
    }

    fun searchItem(query: String): LiveData<List<ToDoData>> {
         return toDoRepository.searchItem(query)
    }

    fun sortItems(order: String): LiveData<List<ToDoData>> {
        return toDoRepository.orderItems(order)
    }
}