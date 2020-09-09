package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;


import java.io.File;

public class Student {
    private String fileName;
    private final StringProperty id = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty major = new SimpleStringProperty();
    private final StringProperty grade = new SimpleStringProperty();
    private final StringProperty gradeOptions = new SimpleStringProperty();
    private CheckBox honorsStatus;
    private ImageView imageFileName;
    private final StringProperty studentNotes = new SimpleStringProperty();


    Student(String id, String lastName, String firstName, String major, String grade, String gradeOptions, CheckBox honorsStatus, ImageView imageFileName, String studentNotes){
        this.id.set(id);
        this.lastName.set(lastName);
        this.firstName.set(firstName);
        this.major.set(major);
        this.grade.set(grade);
        this.gradeOptions.set(gradeOptions);
        this.honorsStatus = honorsStatus;
        this.imageFileName = imageFileName;
        this.studentNotes.set(studentNotes);
    }

    //getter, setter, property access methods for each variable:

    //id
    public String getId(){
        return id.get();
    }
    public void setId(String id){
        this.id.set(id);
    }
    public StringProperty idProperty(){
        return this.id;
    }

    //lastName
    public String getLastName(){
        return lastName.get();
    }
    public void setLastName(String lastName){
        this.lastName.set(lastName);
    }
    public StringProperty lastNameProperty(){
        return this.lastName;
    }

    //firstName
    public String getFirstName(){
        return firstName.get();
    }
    public void setFirstName(String firstName){
        this.firstName.set(firstName);
    }
    public StringProperty firstNameProperty(){
        return this.firstName;
    }

    //major
    public String getMajor(){
        return major.get();
    }
    public void setMajor(String major){
        this.major.set(major);
    }
    public StringProperty majorProperty(){
        return this.major;
    }

    //grade
    public String getGrade(){
        return grade.get();
    }
    public void setGrade(String grade){
        this.grade.set(grade);
    }
    public StringProperty gradeProperty(){
        return this.grade;
    }

    //gradeOptions
    public String getGradeOptions(){
        return gradeOptions.get();
    }
    public void setGradeOptions(String gradeOptions){
        this.gradeOptions.set(gradeOptions);
    }
    public StringProperty gradeOptionsProperty(){
        return this.gradeOptions;
    }

    //honorsStatus
    public CheckBox getHonorsStatus(){
        return honorsStatus;
    }
    public void setHonorsStatus(CheckBox honorsStatus){
        this.honorsStatus = honorsStatus;
    }
    /*public Boolean honorsStatusProperty(){
        return this.honorsStatus;
    }*/

    //imageFileName
    public ImageView getImageFileName(){
        return imageFileName;
    }
    public void setImageFileName(ImageView imageFileName){
        this.imageFileName = imageFileName;
    }
    /*public ImageView imageFileNameProperty(){
        return this.imageFileName;
    }*/

    //notes
    public String getStudentNotes(){
        return studentNotes.get();
    }
    public void setStudentNotes(String notes){
        this.studentNotes.set(notes);
    }
    public StringProperty studentNotesProperty(){
        return this.studentNotes;
    }




}

