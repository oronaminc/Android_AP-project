package org.techtown.ap_project;

public class ListItem {
    String Subject;
    String location;

    //Constructor 정의
    public ListItem(String subject, String location) {
        this.Subject = subject;
        this.location = location;
    }

    //Getter & Setter 정의
    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    //toString 정의
    @Override
    public String toString() {
        return "ListItem{" +
                "Subject='" + Subject + '\'' +
                ", location='" + location + '\'' +
                '}';
    }



}
