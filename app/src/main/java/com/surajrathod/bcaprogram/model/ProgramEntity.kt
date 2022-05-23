package com.surajrathod.bcaprogram.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ProgramEntity (
    @PrimaryKey(autoGenerate = false)
    val id : Int,
    val title : String,
    val content : String = "",
    val sem : String = "",
    val sub : String = "",
    val unit : String = ""
):Serializable