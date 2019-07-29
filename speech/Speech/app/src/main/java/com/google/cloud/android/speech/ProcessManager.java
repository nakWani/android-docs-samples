package com.google.cloud.android.speech;

import android.icu.util.IslamicCalendar;

import java.util.ArrayList;
import java.util.Arrays;

public class ProcessManager {

    private ArrayList<String> YourMenu;
    private String YourTime;
    private String YourName;
    private int mode;

    /* String Set */
    /* messageIsContain()と順番が連動しているので同期とってください*/
    final String[] START = {"スタート","開始","注文"};
    final String[] MENU = {"ピザ","ハンバーガ","ドリンク","ポテト","ホットドック"};
    final String[] YES = {"はい","そう","yes","うん","はーい"};
    final String[] NO = {"いいえ","違","だめ","no"};
    final String[] MENU_END = {"以上","全部","終"};


    public ProcessManager(){
       initPM();
    }

    private void initPM(){
        YourMenu = new ArrayList<String>();
        YourTime = "";
        YourTime = "";
        mode = 0;
    }

    public String getResMessage(String message){
        String resMessage = "";

        if(mode == 0) resMessage = StartTyuumon(message);
        else if (mode == 1) resMessage = MenuGet(message);
        else if (mode == 2) resMessage = TimeGet(message);
        else if (mode == 3) resMessage = NameGet(message);
        else if (mode == 9) resMessage = EndTyuumon(message);
        else resMessage = "[error]getResMessage()";

        return resMessage;
    }


    private String StartTyuumon(String mMes){
        String mRes = "";
        if(messageIsContain(mMes,1)){
            initPM();
            mRes = "注文を開始します\nご注文をどうぞ,\n「以上です」で注文受付を終わります\n"
                    + "Menu:" + Arrays.toString(MENU);
            mode = 1;
        } else {
            mRes = "注文を開始 または スタート というと注文を開始します";
        }
        return mRes;
    }

    private String EndTyuumon(String mMes) {
        String mRes = "";
        String totall_menu = "";

        if (messageIsContain(mMes, 3)) {
            //YES判定
            mRes += "注文受付は以上になります．ご来店をお待ちしております．";
            mode = 0;
        } else if(messageIsContain(mMes,4)){
            //NO判定
            mRes += "いまは いいえ の対応処理を作っておりません．\n";
            mRes += "もう一度お聞きします，はい と言ってください";
        }else {
            for (int i = 0; i < YourMenu.size(); i++) {
                totall_menu += YourMenu.get(i) + "\n";
            }
            mRes += "ご注文内容を再度述べさせていただきます．\n" +
                    "メニュー\n" + totall_menu +
                    "お時間:" + YourTime + "\n" +
                    "お名前:" + YourName + "\n";
            mRes += "以上の内容でお間違いないでしょうか？";
        }
        return mRes;
    }



    private String MenuGet(String mMes){
        String mRes = "";

        if(messageIsContain(mMes,5)){
            //end
            mRes = "注文内容を受け付けました．\n";
            mode = 2;
            mRes += getResMessage("");//mode=2の初回起動
        }else {
            String mYourMenu = returnMenu(mMes);

            if (mYourMenu.length() > 0) {
                YourMenu.add(mMes);
                mRes = "はい．" + mYourMenu + "ですね．続けてどうぞ．";
            } else {
                mRes = "メニューにない注文っぽいです．\n" +
                        "もう一度お願いいたします";
            }
        }
        return  mRes;
    }

    private String TimeGet (String mMes){
        String mRes = "";
        if(mMes.contains("時")){
            YourTime = mMes;
            mRes = "希望時間を受け付けました．\n";
            mode = 3;
            mRes += getResMessage("");//mode=3の初回起動
        } else {
            mRes = "希望受け取り時間は何時でしょうか？";
        }

        return mRes;
    }

    private String NameGet (String mMes){
        String mRes = "";
        if (mMes.length() == 0){
            mRes = "ご氏名をうかがいます．";
        }else {
            YourName = mMes;
            mRes = "注文者名を受け付けました\n";
            mode = 9;//ラスト
            mRes += getResMessage("");//mode=9の初回起動
        }
        return mRes;
    }


    private String returnMenu (String mMessage) {
        String menu = "";

        for (int i = 0; i < MENU.length; i++) {
            if (mMessage.contains(MENU[i])) {
                menu = MENU[i];
                break;
            }
        }
        return menu;
    }

    /**
     * ライブラリにある文字が含まれているかどうか
     * @param mMessage message
     * @param checkAryNum 1:Start,2:Menu,3:Yes,4:No,5:MENU_END
     * @return
     */
    private boolean messageIsContain(String mMessage, int checkAryNum){
        boolean IsContain = false;

        if(checkAryNum == 1){
            for (int i = 0; i < START.length; i++){
                if(mMessage.contains(START[i])) {
                    IsContain = true;
                    break;
                }
            }
        } else if(checkAryNum == 2){
            for (int i = 0; i < MENU.length; i++){
                if(mMessage.contains(MENU[i])){
                    IsContain = true;
                    break;
                }
            }
        } else if(checkAryNum == 3){
            for (int i = 0; i < YES.length; i++){
                if(mMessage.contains(YES[i])){
                    IsContain = true;
                    break;
                }
            }
        } else if(checkAryNum == 4){
            for (int i = 0; i < NO.length; i++){
                if(mMessage.contains(NO[i])){
                    IsContain = true;
                    break;
                }
            }
        } else if(checkAryNum == 5){
            for (int i = 0; i < MENU_END.length; i++){
                if(mMessage.contains(MENU_END[i])){
                    IsContain = true;
                    break;
                }
            }
        } else {
            IsContain = false;
        }
        return IsContain;
    }

}
