package io.github.mattpvaughn.pixelly.data.local.model

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pokemon(
    @PrimaryKey val id: Int,
    val name: String = "",
    val thumb: String = "",
    val weight: Int = 0,
    val type: String = ""
) : DiffUtil.ItemCallback<Pokemon>() {
    override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
        return oldItem == newItem
    }

}

val EMPTY_MODEL = Pokemon(0)
