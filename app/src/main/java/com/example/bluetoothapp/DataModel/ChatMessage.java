package com.example.bluetoothapp.DataModel;

/*
 *@author rag
 *@project BluetoothApp
 */public class ChatMessage {

     boolean isLeft;
     String message;

    public ChatMessage(boolean isLeft, String message) {
        this.isLeft = isLeft;
        this.message = message;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
