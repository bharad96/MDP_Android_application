package com.example.mdp_group15_2021s2.entity

class MessageLog {
    private val messageLog = ArrayList<Message>()

    fun addMessage(type: String, message: String) {
        val objMessage = Message(type, message)
        messageLog.add(objMessage)
    }

    fun getLog(): String {
        var log = ""
        messageLog.forEach { log += "(${it.time}) ${it.role} : ${it.message}\n\n" }
        return log
    }
}