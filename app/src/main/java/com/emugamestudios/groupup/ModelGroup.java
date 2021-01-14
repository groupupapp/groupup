package com.emugamestudios.groupup;


// made by Murat Dogan
public class ModelGroup {
    String groupdId;
    String groupTitle;
    String groupDescription;
    String groupPhoto;
    String name;
    String email;
    String uni;
    String department;
    String uid;
    String photo;
    public ModelGroup(){

    }

    public ModelGroup(String uid, String groupdId, String groupTitle, String groupDescription, String groupPhoto, String name, String email, String uni, String department, String photo) {
        this.uid = uid;
        this.groupdId = groupdId;
        this.groupTitle = groupTitle;
        this.groupDescription = groupDescription;
        this.groupPhoto = groupPhoto;
        this.name = name;
        this.email = email;
        this.uni = uni;
        this.department = department;
        this.photo = photo;
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

    public String getGroupdId() {
        return groupdId;
    }

    public void setGroupdId(String groupdId) {
        this.groupdId = groupdId;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getGroupPhoto() {
        return groupPhoto;
    }

    public void setGroupPhoto(String groupPhoto) {
        this.groupPhoto = groupPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUni() {
        return uni;
    }

    public void setUni(String uni) {
        this.uni = uni;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
