package com.google.cloud.android.speech;

import android.content.Context;

public class T2SThread extends Thread {
    MyTextToSpeech mT2S;
    String SpeekMessage;

    public T2SThread(Context mC){
        mT2S = new MyTextToSpeech(mC);
        initMessage();
    }

    public void setMessage(String message){
        this.SpeekMessage = message;
    }
    public void initMessage(){
        this.SpeekMessage = "";
    }

    @Override
    public void run(){
        if(this.SpeekMessage.length() > 0) {
            mT2S.speechText(SpeekMessage);
        }

    }

}
