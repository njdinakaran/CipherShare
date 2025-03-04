package com.example.ciphershare;

public class new_chat_data {


    private String senderName;
    private String senderEmail;
    private String senderPublickey;
    private String receiverpublicKey;
    private String receiverName;
    private String dateTime,note,receiveremail,status;

    public new_chat_data(String senderName, String senderEmail, String senderPublickey, String receiverpublicKey, String receiverName,String receiveremail, String dateTime, String note, String status) {
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.senderPublickey = senderPublickey;
        this.receiverpublicKey = receiverpublicKey;
        this.receiverName = receiverName;
        this.dateTime = dateTime;
        this.note = note;
        this.receiveremail = receiveremail;
        this.status = status;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderPublickey() {
        return senderPublickey;
    }

    public void setSenderPublickey(String senderPublickey) {
        this.senderPublickey = senderPublickey;
    }

    public String getReceiverpublicKey() {
        return receiverpublicKey;
    }

    public void setReceiverpublicKey(String receiverpublicKey) {
        this.receiverpublicKey = receiverpublicKey;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getReceiveremail() {
        return receiveremail;
    }

    public void setReceiveremail(String receiveremail) {
        this.receiveremail = receiveremail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
