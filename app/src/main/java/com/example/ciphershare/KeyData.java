package com.example.ciphershare;

public class KeyData {
    private String mynote;
    private String mykey;

    private String mytime;

    private String id;

    public KeyData(String mynote, String mykey, String mytime,String id) {
        this.mynote = mynote;
        this.mykey = mykey;
        this.mytime = mytime;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMynote() {
        return mynote;
    }

    public void setMynote(String mynote) {
        this.mynote = mynote;
    }

    public String getMykey() {
        return mykey;
    }

    public void setMykey(String mykey) {
        this.mykey = mykey;
    }

    public String getMytime() {
        return mytime;
    }

    public void setMytime(String mytime) {
        this.mytime = mytime;
    }
}
