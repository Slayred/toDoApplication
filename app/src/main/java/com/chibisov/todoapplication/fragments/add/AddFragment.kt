package com.chibisov.todoapplication.fragments.add

import android.app.ActivityManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chibisov.todoapplication.R
import com.chibisov.todoapplication.data.model.Priority
import com.chibisov.todoapplication.data.model.ToDoData
import com.chibisov.todoapplication.data.viewmodel.ToDoViewModel
import com.chibisov.todoapplication.fragments.SharedViewModel


class AddFragment : Fragment() {

    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val sToDoViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout from fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        //Set menu
        setHasOptionsMenu(true)

        view.findViewById<Spinner>(R.id.currentprioritySP).onItemSelectedListener = sToDoViewModel.listener

        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add){
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val aTitle = activity?.findViewById<EditText>(R.id.currentTitleET)?.text.toString()
        val aPriority = activity?.findViewById<Spinner>(R.id.currentprioritySP)?.selectedItem.toString()
        val aDescription = activity?.findViewById<EditText>(R.id.currentDescriptionET)?.text.toString()

        val validation = sToDoViewModel.verifyDataFromUser(aTitle, aDescription)
        if(validation){
            //Insert Data to Database
            val newData = ToDoData(
                0,
                aTitle,
                sToDoViewModel.parsePriority(aPriority),
                aDescription
            )
            mToDoViewModel.insertData(newData)
            Toast.makeText(requireContext(), "Successfully add", Toast.LENGTH_SHORT).show()
            //Navigate Back
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }
// Move to SharedViewModel
//    private fun verifyDataFromUser(title: String, description: String): Boolean {
//        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(description))
//    }
//
//    private fun parsePriority(priority: String): Priority{
//        return when(priority){
//            "High Priority" -> {Priority.HIGH}
//            "Medium Priority" -> {Priority.MEDIUM}
//            "Low Priority" -> {Priority.LOW}
//            else -> Priority.LOW
//        }
//    }
}