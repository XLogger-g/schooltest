package com.example.schoolteacher.Model;

public class AssignClass {

    private String asId;
    private String asTitle;
    private String assignment;
    private String shortDesc;

    public AssignClass() {
        //Required Empty Constructor for Firebase
    }

    public AssignClass(String asId, String asTitle, String assignment) {

        this.asId = asId;
        this.asTitle = asTitle;
        this.assignment = assignment;
    }

    public AssignClass(String asId, String assignment) {

        this.asId = asId;
        this.assignment = assignment;
    }


    public String getAsId() { return asId; }

    public void setAsId(String asId) { this.asId = asId; }

    public String getAsTitle() { return asTitle; }

    public void setAsTitle(String asTitle) { this.asTitle = asTitle; }

    public String getAssignment() { return assignment; }

    public void setAssignment(String assignment) { this.assignment = assignment; }

    public String getShortDesc() { return shortDesc; }

    public void setShortDesc(String shortDesc) { this.shortDesc = shortDesc; }
}


