package com.chibisov.todoapplication.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chibisov.todoapplication.R
import com.chibisov.todoapplication.data.model.ToDoData
import com.chibisov.todoapplication.data.viewmodel.ToDoViewModel
import com.chibisov.todoapplication.fragments.SharedViewModel
import com.chibisov.todoapplication.fragments.list.adapter.ListAdapter
import com.chibisov.todoapplication.fragments.list.adapter.SwipeToDelete
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_list.view.*
import java.text.FieldPosition


class ListFragment : Fragment() {

    private val adapter: ListAdapter by lazy { ListAdapter() }
    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedToDoViewModel: SharedViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("TAG", "Fragment Create")
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_list, container, false)
        // Set adapter and manager for RV
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleViewList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        swipeToDelete(recyclerView)

        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer {
            data ->
            mSharedToDoViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })
        mSharedToDoViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer {
            showEmptyDatabaseViews(it)
        })


        val btn = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        //val listLayout = view.findViewById<ConstraintLayout>(R.id.listLayout)

        btn.setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

//        listLayout.setOnClickListener {
//            findNavController().navigate(R.id.action_listFragment_to_updateFragment)
//        }


        //Set menu
        setHasOptionsMenu(true)
        return view
    }

    private fun showEmptyDatabaseViews(emptyDatabase: Boolean) {
        if(emptyDatabase) {
            view?.noDataIView?.visibility = View.VISIBLE
            view?.noDataTView?.visibility = View.VISIBLE

        }   else   {
                view?.noDataIView?.visibility = View.INVISIBLE
                view?.noDataTView?.visibility = View.INVISIBLE
            }
        }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.dataList[viewHolder.adapterPosition]
                //Delete Item
                mToDoViewModel.delete(itemToDelete)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                //Toast.makeText(requireContext(), "Successfully delete", Toast.LENGTH_SHORT).show()
                //Restore deleted data
                restoreDeletedData(viewHolder.itemView,itemToDelete,viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem: ToDoData, position: Int){
        val snackBar = Snackbar.make(
            view,
            "Deleted '${deletedItem.title}'",
            Snackbar.LENGTH_SHORT
        )
        snackBar.setAction("Undo"){
            mToDoViewModel.insertData(deletedItem)
            adapter.notifyItemChanged(position)
        }
        snackBar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_delete_all -> confirmRemoveAll()
        }
        return super.onOptionsItemSelected(item)
    }

    //Show dialog  and delete all data
    private fun confirmRemoveAll(){
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Delete all data")
        alertDialog.setMessage("Are u sure to delete all?")
        alertDialog.setPositiveButton("Yes"){
                _, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(requireContext(),"Successfully deleted All", Toast.LENGTH_SHORT).show()
        }
        alertDialog.setNegativeButton("NO"){_, _ -> }
        alertDialog.show()

    }

}