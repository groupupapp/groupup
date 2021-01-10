package com.emugamestudios.groupup;

public class ModelUser {
    String bio, department, email, name, photo, uid, uni;

    public ModelUser() {

    }

    public ModelUser(String bio, String department, String email, String name, String photo, String uid, String uni) {
        this.bio = bio;
        this.department = department;
        this.email = email;
        this.name = name;
        this.photo = photo;
        this.uid = uid;
        this.uni = uni;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUni() {
        return uni;
    }

    public void setUni(String uni) {
        this.uni = uni;
    }
}
