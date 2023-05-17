package com.del.notesappdel

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.del.notesappdel.Adapter.NotesAdapter
import com.del.notesappdel.Database.NoteDatabase
import com.del.notesappdel.Models.Note
import com.del.notesappdel.Models.NoteViewModel
import com.del.notesappdel.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), NotesAdapter.NotesClickListener, PopupMenu.OnMenuItemClickListener{
    private lateinit var binding: ActivityMainBinding

    //Base de datos
    private  lateinit var database: NoteDatabase

    //Ventana
    lateinit var viewModel: NoteViewModel

    //Adaptador
    lateinit var adapter: NotesAdapter

    //Notas seleccionadas
    lateinit var selectedNote : Note


    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val note = result.data?.getSerializableExtra("note") as? Note
            if(note != null){
                viewModel.updateNote(note)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Iniciar la UI
        initUI()

        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)

        viewModel.allnotes.observe(this){list ->
            list?.let{
                adapter.updateList(list)
            }

        }

        database = NoteDatabase.getDatabase(this)


    }

    private fun initUI() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = NotesAdapter(this, this)
        binding.recyclerView.adapter = adapter


        //Obtener notas
        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                val note = result.data?.getSerializableExtra("note") as? Note
                if (note != null) {
                    viewModel.insertNote(note)
                }

            }
        }

        //Añadir nota
        binding.fbAddNote.setOnClickListener{
            val intent = Intent(this, AddNote::class.java)
            getContent.launch(intent)
        }

        //Buscar notas
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if(newText != null){
                    adapter.filterList(newText)
                }
                return true
            }
        })

    }

    //Metodo para entrar y actualizar nota
    override fun onItemClicked(note: Note) {
        val intent = Intent(this@MainActivity, AddNote::class.java)
        intent.putExtra("current_note", note)
        updateNote.launch(intent)
    }

    //Metodo para aparecer boton eliminar
    override fun onLongItemClicked(note: Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)

    }


    private fun popUpDisplay(cardView: CardView) {
        val popup = PopupMenu(this, cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()
    }


    //Eliminar la nota
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.delete_note){
            viewModel.deleteNote(selectedNote)
            return true
        }
        return false
    }
}





