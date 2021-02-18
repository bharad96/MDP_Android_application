package com.example.mdp_group15_2021s2.entity

interface Protocol {
    companion object {
        const val MESSAGE_RECEIVE = 0
        const val MESSAGE_ERROR = 1
        const val CONNECTION_ERROR = 2
    }
}