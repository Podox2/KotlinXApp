package com.podorozhniak.kotlinx.theory.variances.messanger;

public class Main {
    public static void main(String[] args) {
        Message message = new Message();
        EmailMessage emailMessage = new EmailMessage();
    }

    //в котлін варіанті це працює
    private void changeMessengerToEmail(Messenger<EmailMessage> obj) {
        //Messenger<Message> messenger = obj;
    }
}
