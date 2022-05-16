package com.example.todos

class Todos{
    var id:String? = null
    var content:String? = null
    var createdAt: String? = null
    var isDone:Boolean? = false
    constructor(){
    }

    constructor(id: String?, content: String?, createdAt: String?, isDone: Boolean?)
    {
        this.id = id
        this.content = content
        this.createdAt = createdAt
        this.isDone = isDone
    }


}