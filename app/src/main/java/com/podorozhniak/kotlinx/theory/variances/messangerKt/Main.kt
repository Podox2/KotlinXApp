package com.podorozhniak.kotlinx.theory.variances.messangerKt

fun changeMessengerToEmail(obj: MessengerKt<EmailMessageKt>){
    val messenger: MessengerKt<MessageKt> = obj
}