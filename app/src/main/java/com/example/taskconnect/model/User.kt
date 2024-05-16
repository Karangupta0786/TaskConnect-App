package com.example.taskconnect.model

class User {
    var userName = ""
    var userDesc = ""
    var usedId = 0
//    val imgURL = ""

    constructor(){}

    constructor(userName: String, userDesc: String, usedId: Int) {
        this.userName = userName
        this.userDesc = userDesc
        this.usedId = usedId
    }
}