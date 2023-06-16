package edu.wit.mobileapp.checkmark;

public class course_Item {
    private long course_ID;
    private String courseName;
    private String courseID;

    public course_Item(long course_ID, String courseName, String courseID) {
        this.course_ID = course_ID;
        this.courseName = courseName;
        this.courseID = courseID;
    }
    public course_Item(String courseName, String courseID) {
        this.courseName = courseName;
        this.courseID = courseID;
    }

    public long getCourse_ID() {
        return course_ID;
    }

    public void setCourse_ID(long course_ID) {
        this.course_ID = course_ID;
    }
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }
}
