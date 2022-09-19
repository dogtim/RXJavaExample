package com.example.rxjava.tictactoe

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.rxjava.tictactoe.data.SavedGame
import java.util.*


class SavedGamesListAdapter : ArrayAdapter<SavedGame?> {
    constructor(context: Context, resource: Int) : super(context, resource)
    constructor(context: Context, resource: Int, textViewResourceId: Int) : super(
        context,
        resource,
        textViewResourceId
    )

    constructor(context: Context, resource: Int, objects: Array<SavedGame?>) : super(
        context,
        resource,
        objects
    )

    constructor(
        context: Context,
        resource: Int,
        textViewResourceId: Int,
        objects: Array<SavedGame?>
    ) : super(context, resource, textViewResourceId, objects)

    constructor(context: Context, resource: Int, objects: List<SavedGame?>) : super(
        context,
        resource,
        objects
    )

    constructor(
        context: Context,
        resource: Int,
        textViewResourceId: Int,
        objects: List<SavedGame?>
    ) : super(context, resource, textViewResourceId, objects) {
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        convertView = super.getView(position, convertView, parent)
        val savedGame: SavedGame? = getItem(position)
        (convertView as TextView).setText(Date(savedGame!!.timestamp).toString())
        convertView.setTag(savedGame)
        return convertView
    }
}