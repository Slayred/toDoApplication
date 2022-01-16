package com.chibisov.todoapplication.data.repository

import androidx.lifecycle.LiveData
import com.chibisov.todoapplication.data.ToDoDao
import com.chibisov.todoapplication.data.model.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {

    val getAllData: LiveData<List<ToDoData>> = toDoDao.getAllData()

//    val sortByHighPriority: LiveData<List<ToDoData>> = toDoDao.sortByHighPriority()
//    val sortByLowPriority: LiveData<List<ToDoData>> = toDoDao.sortByLowPriority()


    suspend fun insertData(toDoData: ToDoData) {
        toDoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData) {
        toDoDao.updateData(toDoData)
    }

    suspend fun delete(data: ToDoData) {
        toDoDao.delete(data)
    }

    suspend fun deleteAll(){
        toDoDao.deleteAll()
    }

    fun searchItem(searchTitle: String): LiveData<List<ToDoData>>{
        return toDoDao.searchDatabase(searchTitle)
    }

    fun orderItems(order: String): LiveData<List<ToDoData>>{
         return when(order){
             "HIGH" -> toDoDao.sortByHighPriority()
             "LOW" -> toDoDao.sortByLowPriority()
             else -> getAllData
         }
    }

}