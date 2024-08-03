package com.example.mytodo

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private var notes: List<Note>, private val onEditClick: (String) -> Unit) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleView: TextView = itemView.findViewById(R.id.itemtitleview)
        val contentView: TextView = itemView.findViewById(R.id.itemcontentview)
        //val editbutton: TextView = itemView.findViewById(R.id.editbutton)

        fun bind(note: Note) {
            titleView.text = note.title
            contentView.text = note.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    /*fun updatenote(updatednote: Note) {
        val position = notes.indexOfFirst { it.id ==updatednote.id }
        if (position  != -1){
            notes[position] = updatednote
            notifyItemChanged(position)
        }
    }*/

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)

    }

    fun refreshData(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}