package com.batuhan.triviagame.model

data class Questions(
    var text: String,
    var answers: List<String>,
    var trueAnswer: String,
    var uuid: Int = 1
)


