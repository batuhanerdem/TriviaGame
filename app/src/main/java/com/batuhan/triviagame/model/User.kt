package com.batuhan.triviagame.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @ColumnInfo(name = "userName")
    var name: String,

    @PrimaryKey
    @ColumnInfo(name = "UserEmail")
    var eMail: String,

    @ColumnInfo(name = "userAnsweredQuestion")
    var answeredQuestion: Int = 0,

    @ColumnInfo(name = "userTrueAnsweredQuestion")
    var trueAnswerNumber: Int = 0
)

