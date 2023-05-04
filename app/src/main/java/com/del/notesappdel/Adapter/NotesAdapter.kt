package com.del.notesappdel.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.del.notesappdel.Models.Note
import com.del.notesappdel.R
import kotlin.random.Random

class NotesAdapter (private val context : Context, val listener: NotesClickListener) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>(){

    private val NotesList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item,parent, false)
        )
    }

    override fun getItemCount(): Int {
        return NotesList.size
    }

    fun updateList(newList : List<Note>){
        fullList.clear()

        fullList.addAll(newList)

        NotesList.clear()

        NotesList.addAll(fullList)

        notifyDataSetChanged()
    }

    fun filterList(search : String){
        NotesList.clear()

        for(item in fullList){
            if(item.title?.lowercase()?.contains(search.lowercase()) == true ||
                    item.note?.lowercase()?.contains(search.lowercase())==true){

                NotesList.add(item)

            }

            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = NotesList[position]
        holder.title.text = currentNote.title
        holder.title.isSelected = true

        holder.Note_tv.text = currentNote.note
        holder.date.text = currentNote.date
        holder.date.isSelected = true

        holder.notes_layout.setCardBackgroundColor(holder.itemView.resources.getColor(RandomColor(), null))

        holder.notes_layout.setOnClickListener{
            listener.onItemClicked(NotesList[holder.adapterPosition])
        }

        holder.notes_layout.setOnLongClickListener{
            listener.onLongitemClicked(NotesList[holder.adapterPosition],holder.notes_layout)
            true
        }
    }

    //Funcion para color random para las notas
    fun RandomColor() : Int{
        val list = ArrayList<Int>()
        list.add(R.color.NoteColor1)
        list.add(R.color.NoteColor2)
        list.add(R.color.NoteColor3)
        list.add(R.color.NoteColor4)
        list.add(R.color.NoteColor5)
        list.add(R.color.NoteColor6)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]
    }

    inner class NoteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val notes_layout = itemView.findViewById<CardView>(R.id.card_layout)

        val title = itemView.findViewById<TextView>(R.id.tv_titulo)

        val Note_tv = itemView.findViewById<TextView>(R.id.tv_nota)

        val date = itemView.findViewById<TextView>(R.id.tv_fecha)

    }


    interface NotesClickListener{
        fun onItemClicked(note : Note)

        fun onLongitemClicked(note : Note, cardView: CardView)


    }
}