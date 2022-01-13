package com.chibisov.todoapplication.fragments.update

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chibisov.todoapplication.R
import com.chibisov.todoapplication.data.model.Priority
import com.chibisov.todoapplication.data.model.ToDoData
import com.chibisov.todoapplication.data.viewmodel.ToDoViewModel
import com.chibisov.todoapplication.fragments.SharedViewModel
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*


class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private val sToDoViewModel: SharedViewModel by viewModels()
    private val mToDoViewModel: ToDoViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view  = inflater.inflate(R.layout.fragment_update, container, false)

        //Set menu
        setHasOptionsMenu(true)

        view.currentTitleET.setText(args.currentItem.title)
        view.currentDescriptionET.setText(args.currentItem.description)
        view.currentprioritySP.setSelection(sToDoViewModel.parsePriorityToInt(args.currentItem.priority))
        view.currentprioritySP.onItemSelectedListener =  sToDoViewModel.listener


        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_save){
            updateTiem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateTiem() {
        val title = currentTitleET.text.toString()
        val description = currentDescriptionET.text.toString()
        val priority = currentprioritySP.selectedItem.toString()

        if (sToDoViewModel.verifyDataFromUser(title, description)){
            val data = ToDoData(
                args.currentItem.id,
                title,
                sToDoViewModel.parsePriority(priority),
                description
            )
            mToDoViewModel.updateData(data)
            Toast.makeText(requireContext(), "Successfully updated", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }

    //Move To SharedModel
//    private fun parsePriority(priority: Priority): Int{
//        return when(priority){
//            Priority.HIGH -> 0
//            Priority.MEDIUM -> 1
//            Priority.LOW -> 2
//        }

}
