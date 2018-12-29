package com.xuhe.codebook20;

/**
 *
 * Created by Administrator on 2017/3/31.
 */
class Data {

    private String mTitle;
    private String mContent;
    private String mTime;

    Data(String title, String content, String time){
        this.mTitle = title;
        this.mContent = content;
        this.mTime = time;
    }

    String getmTitle() {
        return mTitle;
    }

    String getmContent() {return mContent;}

    String getmTime() {
        return mTime;
    }
}
