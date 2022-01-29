package com.example.collegeinhouse.Models;

public class Users {
    String profilepic, userName, mail, password, userId, lastMessage,job,college;

    public Users(String profilepic, String userName, String mail, String password, String userId, String lastMessage,String job,String college) {
        this.profilepic = profilepic;
        this.userName = userName;
        this.mail = mail;
        this.password = password;
        this.userId = userId;
        this.lastMessage = lastMessage;
        this.job=job;
        this.college=college;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String jobn) {
        this.job = jobn;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String collegen) {
        this.college = collegen;
    }

    public Users(){}

    //Sign up constructor
    public Users(String userName, String mail, String password) {

        this.userName = userName;
        this.mail = mail;
        this.password = password;

    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }


}
