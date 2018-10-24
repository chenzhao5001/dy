package com.guidesound.models;

public class ArticleInfo {

    int id;
    String head;
    String head_pic1;
    String head_pic2;
    String head_pic3;


    int collection_count;
    int chat_count;
    int priase_count;

    int create_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getHead_pic1() {
        return head_pic1;
    }

    public void setHead_pic1(String head_pic1) {
        this.head_pic1 = head_pic1;
    }

    public String getHead_pic2() {
        return head_pic2;
    }

    public void setHead_pic2(String head_pic2) {
        this.head_pic2 = head_pic2;
    }

    public String getHead_pic3() {
        return head_pic3;
    }

    public void setHead_pic3(String head_pic3) {
        this.head_pic3 = head_pic3;
    }

    public int getCollection_count() {
        return collection_count;
    }

    public void setCollection_count(int collection_count) {
        this.collection_count = collection_count;
    }

    public int getChat_count() {
        return chat_count;
    }

    public void setChat_count(int chat_count) {
        this.chat_count = chat_count;
    }

    public int getPriase_count() {
        return priase_count;
    }

    public void setPriase_count(int priase_count) {
        this.priase_count = priase_count;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

}
