package com.surajrathod.bcaprogram.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ProgramEntity(

    @PrimaryKey(autoGenerate = false)
    val id : Int,
    val title : String,
    val content : String = "",
    val sem : String = "",
    val sub : String = "",
    val unit : String = ""
)
