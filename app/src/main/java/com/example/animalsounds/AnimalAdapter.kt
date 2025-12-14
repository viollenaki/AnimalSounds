package com.example.animalsounds

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.animalsounds.R
import de.hdodenhof.circleimageview.CircleImageView

class AnimalAdapter(
    val animalNames: List<String>,
    val animalImages: List<Int>,
    context: Context,
) : BaseAdapter() {
    private val inflater = LayoutInflater.from(context)
    override fun getCount(): Int {
        return animalNames.size
    }

    override fun getItem(position: Int): Any? {
        return animalNames[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        if (parent == null) return null
        val view: View
        val holder: AnimalViewHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.custom_layout, parent, false)
            holder = AnimalViewHolder(
                animalName = view.findViewById(R.id.animalName),
                animalImage = view.findViewById(R.id.animalImage),
            )
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as AnimalViewHolder
        }

        holder.animalName.text = animalNames[position]
        holder.animalImage.setImageResource(animalImages[position])

        return view
    }

    private class AnimalViewHolder(
        val animalName: TextView,
        val animalImage: CircleImageView,
    )
}