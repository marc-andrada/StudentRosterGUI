package sample;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Controller {

    public TextField idNum;
    public TextField lastName;
    public TextField firstName;
    public ComboBox major;
    public ToggleGroup grades;
    public ComboBox gradeOptions;
    public CheckBox hsStatus;
    public CheckBox hStatus;
    public ImageView studentImage;
    public String imageFileName;
    public TextArea studentNotes;
    public TableColumn<Student, String> columnId;
    public TableColumn<Student, String> columnLastName;
    public TableColumn<Student, String> columnFirstName;
    public TableColumn<Student, String> columnMajor;
    public TableColumn<Student, String> columnGrade;
    public TableColumn<Student, String> columnGradeOptions;
    public TableColumn<Student, CheckBox> columnHonorsStatus;
    public TableColumn<Student, ImageView> columnImageFileName;
    public TableColumn<Student, String> columnStudentNotes;
    //Update these next 2 values as necessary
    public String imagesDirectory = "\\245_4.2\\images";
    public String rosterDirectory = "\\245_4.2\\roster";
    public LinkedList<File> allStudentFiles;
    public ObservableList<Student> listStudents = FXCollections.observableArrayList(); //we need to update this list with current amt of students
    public StatsHandler statsHandler = new StatsHandler();
    public ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    public PieChart pieChart = new PieChart(pieChartData);
    public CategoryAxis xAxis;
    public NumberAxis yAxis;
    public BarChart barChart;
    public XYChart.Series barChartData = new XYChart.Series();
    public Circle circle1;
    public Circle circle2;
    public Circle circle3;
    public Text logoText;
    public StackPane logoSquare;
    public int currentStudent;
    public Stage ourStage;
    public TableView<Student> studentTable;


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~[METHODS]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    //import image method
    public void importImage(){
        // create a File chooser
        FileChooser file_chooser = new FileChooser();
        File myImages = new File(imagesDirectory);
        file_chooser.setInitialDirectory(myImages);

        // get the file selected
        File file = file_chooser.showOpenDialog(new Stage());

        if (file != null) {
            imageFileName = file.toURI().toString();
            studentImage.setImage(new Image(imageFileName));
        }
    }

    //new student method
    public void newStudent(){

        idNum.clear();
        lastName.clear();
        firstName.clear();
        major.setValue("");
        for(Toggle radioButton : grades.getToggles()){
            if(radioButton.isSelected()){
                radioButton.setSelected(false);
            }
        }
        gradeOptions.setValue("");

        if(hsStatus.isSelected()){
            hsStatus.setSelected(false);
        }

        studentImage.setImage(null);

        studentNotes.clear();

    }

    //save student method
    public void saveStudent(){

        String fileName = rosterDirectory
                + "\\"
                + idNum.getText()
                + "_"
                + lastName.getText()
                + "_"
                + firstName.getText()
                + ".txt";

        File file = new File(fileName);
        ArrayList<String> info = new ArrayList<>();

        //save last name, first name, and major from text fields
        info.add(fileName);
        info.add(idNum.getText());
        info.add(lastName.getText());
        info.add(firstName.getText());
        info.add(major.getValue().toString());

        //get grade from radio button options
        for(Toggle radioButton : grades.getToggles()){
            if(radioButton.isSelected()){
                RadioButton selectedGrade = (RadioButton) radioButton;
                String studentGrade = selectedGrade.getText();
                info.add(studentGrade);
            }
        }

        //get grading style from drop down menu

        info.add(gradeOptions.getValue().toString());

        //get honors status (later if it is yes, must check box)

        if(hsStatus.isSelected()){
            info.add("yes"); }
        else{
            info.add("no");
        }

        //get student image file to show that file name is not corrupt
        //System.out.println("412: " + imageFileName);

        info.add(imageFileName);

        //get student notes

        info.add(studentNotes.getText());

        //create new file

        //if this file already exists
        if(allStudentFiles.contains(file)){
            System.out.println("This file already exists, will update it");
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                for (String str : info){
                    writer.write(str + "\n");
                    writer.flush();
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //otherwise add new file
        }else{
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                for (String str : info){
                    writer.write(str + "\n");
                    writer.flush();
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            allStudentFiles.add(new File(fileName));
            System.out.println("Current student before: " + currentStudent);
            currentStudent = allStudentFiles.size()-1;
            System.out.println("Current student after: " + currentStudent);

        }

        try {
            showCurrentStudent(currentStudent, ourStage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            studentTable.getItems().clear();
            populateStudentTable();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            statsHandler.clearAllValues();
            populateStatistics();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    //previous student method
    public void previousStudent(){
        try {
            if(allStudentFiles.isEmpty()){
                System.out.println("Student roster is currently empty");
            }
            else if(currentStudent - 1 >= 0){
                currentStudent--;
                showCurrentStudent(currentStudent, ourStage);
            }
            else{
                System.out.println("Beginning of roster");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //next student method
    public void nextStudent(){
        try {
            if(allStudentFiles.isEmpty()){
                System.out.println("Student roster is currently empty");
            }
            else if(currentStudent + 1 != allStudentFiles.size()){
                currentStudent++;
                showCurrentStudent(currentStudent, ourStage);
            }
            else{
                System.out.println("End of roster");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    //delete student method

    public void deleteStudent(){
        String fileName = rosterDirectory
                + "\\"
                + idNum.getText()
                + "_"
                + lastName.getText()
                + "_"
                + firstName.getText()
                + ".txt";

        File file = new File(fileName);

        if(file.exists()){
            System.out.println("--> " + file + " exists, will remove now...\n");
            boolean deleted = file.delete();
            System.out.println("File deleted? " + deleted);
            System.out.println("Id = " + idNum.getText());
            allStudentFiles.remove(file);
            currentStudent = 0;

        }
        else{
            System.out.println("File does not exist");
        }

        try {
            if(!allStudentFiles.isEmpty()){
                showCurrentStudent(currentStudent, ourStage);
            }else{
                System.out.println("Student roster is currently empty");
            }
            try {
                studentTable.getItems().clear();
                populateStudentTable();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                statsHandler.clearAllValues();
                populateStatistics();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    public void openStudentFile(){
        FileChooser fc = new FileChooser();
        File myStudents = new File(rosterDirectory);
        fc.setInitialDirectory(myStudents);
        File file = fc.showOpenDialog(ourStage);
        try {
            currentStudent = allStudentFiles.indexOf(file);
            showCurrentStudent(currentStudent, ourStage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveStudentFileAs(){
        FileChooser fc = new FileChooser();
        File myStudents = new File(rosterDirectory);
        fc.setInitialDirectory(myStudents);
        File file = fc.showSaveDialog(ourStage);
        ArrayList<String> info = new ArrayList<>();


        //save last name, first name, and major from text fields
        info.add(file.toURI().toString());
        info.add(idNum.getText());
        info.add(lastName.getText());
        info.add(firstName.getText());
        info.add(major.getValue().toString());

        //get grade from radio button options
        for(Toggle radioButton : grades.getToggles()){
            if(radioButton.isSelected()){
                RadioButton selectedGrade = (RadioButton) radioButton;
                String studentGrade = selectedGrade.getText();
                info.add(studentGrade);
            }
        }

        //get grading style from drop down menu

        info.add(gradeOptions.getValue().toString());
        //get honors status (later if it is yes, must check box)

        if(hsStatus.isSelected()){
            info.add("yes"); }
        else{
            info.add("no"); }

        //get student image file
        System.out.println("412: " + imageFileName);

        info.add(imageFileName);
        //get student notes

        info.add(studentNotes.getText());
        //create new file

        //if this file already exists
        if(allStudentFiles.contains(file)){
            System.out.println("\nThis file already exists, will update it");
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for (String str : info){
                    writer.write(str + "\n");
                    writer.flush();
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //otherwise add new file
        }else{
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for (String str : info){
                    writer.write(str + "\n");
                    writer.flush();
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            allStudentFiles.add(file);
            System.out.println("Current student before: " + currentStudent);
            currentStudent = allStudentFiles.size()-1;
            System.out.println("Current student after: " + currentStudent);
        }

        try {
            showCurrentStudent(currentStudent, ourStage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            studentTable.getItems().clear();
            populateStudentTable();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            statsHandler.clearAllValues();
            populateStatistics();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void exitProgram(){
        System.exit(0);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~[EDIT TABLE METHODS]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public void editColumnID(TableColumn.CellEditEvent<Student,String> t){
            t.getRowValue().setId(t.getNewValue()); //~~~ maybe bind here with values in our roster!
            idNum.setText(t.getNewValue());
    }

    public void editColumnLastName(TableColumn.CellEditEvent<Student,String> t){
            t.getRowValue().setLastName(t.getNewValue()); //~~~ maybe bind here with values in our roster!
            lastName.setText(t.getNewValue());
    }

    public void editColumnFirstName(TableColumn.CellEditEvent<Student,String> t){
            t.getRowValue().setFirstName(t.getNewValue()); //~~~ maybe bind here with values in our roster!
            firstName.setText(t.getNewValue());
    }

    public void editColumnMajor(TableColumn.CellEditEvent<Student,String> t){
            t.getRowValue().setMajor(t.getNewValue()); //~~~ maybe bind here with values in our roster!
            major.setValue(t.getNewValue());
    }

    public void editColumnGrade(TableColumn.CellEditEvent<Student,String> t){
            t.getRowValue().setGrade(t.getNewValue()); //~~~ maybe bind here with values in our roster!
            for(Toggle radioButton : grades.getToggles()){
                RadioButton selectedGrade = (RadioButton) radioButton;
                String studentGrade = selectedGrade.getText();
                if(studentGrade.equals(t.getNewValue())){
                    selectedGrade.setSelected(true);
                }
            }
    }

    public void editColumnGradeOptions(TableColumn.CellEditEvent<Student,String> t){
            t.getRowValue().setGradeOptions(t.getNewValue()); //~~~ maybe bind here with values in our roster!
            gradeOptions.setValue(t.getNewValue());
    }

    public void editColumnHonorsStatus(TableColumn.CellEditEvent<Student,CheckBox> t){

            t.getRowValue().setHonorsStatus(t.getNewValue()); //~~~ maybe bind here with values in our roster!
            System.out.println("VALUE IN: " + t.getNewValue().isSelected());
            if(t.getNewValue().equals(true)){
                hsStatus.setSelected(true);
            }
            else{
                hsStatus.setSelected(false);
            }

    }

    public void editColumnImageFileName(TableColumn.CellEditEvent<Student,ImageView> t){
            t.getRowValue().setImageFileName(t.getNewValue()); //~~~ maybe bind here with values in our roster!
    }

    public void editColumnStudentNotes(TableColumn.CellEditEvent<Student,String> t){
            t.getRowValue().setStudentNotes(t.getNewValue()); //~~~ maybe bind here with values in our roster!
            studentNotes.setText(t.getNewValue());
    }



    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~[SHOW CURRENT STUDENT]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    //method to show current student from our directory
    public void showCurrentStudent(int currentStudent, Stage stage) throws FileNotFoundException {

        //Read shown file info
        File currentFile = allStudentFiles.get(currentStudent);
        ArrayList<String> fileInfo = new ArrayList<>();
        Scanner scanner = new Scanner(currentFile);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            fileInfo.add(line);
        }
        scanner.close();

        //test to show what our file read:
        System.out.println("\nCurrent student info:");
        for(int i = 0; i < fileInfo.size(); i++ ){
            System.out.println(i + " " + fileInfo.get(i));
        }

        idNum.setText(fileInfo.get(1));
        lastName.setText(fileInfo.get(2));
        firstName.setText(fileInfo.get(3));
        major.setValue(fileInfo.get(4));
        for(Toggle radioButton : grades.getToggles()){
            RadioButton currentButton = (RadioButton) radioButton;
            if(currentButton.getText().equals(fileInfo.get(5))){
                radioButton.setSelected(true);
            }
        }
        gradeOptions.setValue(fileInfo.get(6));
        if(fileInfo.get(7).equals("yes")){
            hsStatus.setSelected(true);
        }
        else{
            hsStatus.setSelected(false);
        }

        if(fileInfo.get(8) == null){
            studentImage.setImage(new Image("file:/C:/Users/Marc/Desktop/245_4.2/images/inf.jpg"));
        }
        else{
            studentImage.setImage(new Image(fileInfo.get(8)));
        }
        imageFileName = fileInfo.get(8);

        String notes = "";
        for(int i = 9; i < fileInfo.size(); i++){
            notes += fileInfo.get(i) + " ";
        }
        studentNotes.setText(notes);

        //set title
        String title = fileInfo.get(2) + ", " + fileInfo.get(3);
        stage.setTitle(title);

    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~[LOAD STUDENT ROSTER]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    //method to load student roster from our file directory
    public void loadStudentRoster(){
        System.out.println("Loading Student Roster...");
        File directory = new File(rosterDirectory);
        File[] allFiles = directory.listFiles();
        allStudentFiles = new LinkedList<>();
        System.out.println("Current files in roster: ");
        for (File student : allFiles){
            allStudentFiles.add(student);
            System.out.println(student);
        }
        //studentFileIterator = allStudentFiles.listIterator();
        currentStudent = 0;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~[POPULATE STUDENT TABLE]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    //method to populate student table
    public void populateStudentTable() throws FileNotFoundException {
        for (int i = 0; i < allStudentFiles.size(); i++){
            //Read shown file info
            File currentFile = allStudentFiles.get(i);
            ArrayList<String> fileInfo = new ArrayList<>();
            Scanner scanner = new Scanner(currentFile);
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                fileInfo.add(line);
            }
            scanner.close();

            //test to show what our file read: turned off because i want to see if file exists (SAVE)
            /*System.out.println("\nCurrent student info:");
            for(int j = 0; j < fileInfo.size(); j++ ){
                System.out.println(j + " " + fileInfo.get(j));
            }*/

            //get all info from file
            String id = fileInfo.get(1);
            String last = fileInfo.get(2);
            String first = fileInfo.get(3);
            String major = fileInfo.get(4);
            String grade = fileInfo.get(5);
            String gradeOption = fileInfo.get(6);
            //String honorsStatus = fileInfo.get(7);
            if(fileInfo.get(7).equals("yes")){
                hStatus = new CheckBox();
                hStatus.setSelected(true);
            }
            else{
                hStatus = new CheckBox();
                hStatus.setSelected(false);
            }
            //String imgFileName = fileInfo.get(8);
            ImageView imgView = new ImageView(new Image(fileInfo.get(8)));
            imgView.setFitWidth(80);
            imgView.setFitHeight(80);

            String myNotes = "";
            for(int k = 9; k < fileInfo.size(); k++){
                myNotes += fileInfo.get(k);
            }

            //create students and add to our table
            Student student = new Student(id, last, first, major, grade, gradeOption, hStatus, imgView, myNotes);
            listStudents.add(student);
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~[POPULATE STATISTICS]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    public void populateStatistics() throws FileNotFoundException {
        for (int i = 0; i < allStudentFiles.size(); i++){
            //Read shown file info
            File currentFile = allStudentFiles.get(i);
            ArrayList<String> fileInfo = new ArrayList<>();
            Scanner scanner = new Scanner(currentFile);
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                fileInfo.add(line);
            }
            scanner.close();

            //get all info from file

            String major = fileInfo.get(4);
            if(major.equals("Biology")){
                statsHandler.incNumBiology();
                //System.out.println("Added biology");
            }
            else if(major.equals("Software Engineering")){
                statsHandler.incNumSoftwareEngineering();
                //System.out.println("Added SWE");
            }
            else if(major.equals("Mathematics")){
                statsHandler.incNumMathematics();
                //System.out.println("Added Math");
            }
            else if(major.equals("Art")){
                statsHandler.incNumArt();
                //System.out.println("Added Art");
            }
            else if(major.equals("Underwater Basket Weaving")){
                statsHandler.incNumUnderwaterBasketWeaving();
                //System.out.println("Added UBW");
            }
            else{
                System.out.println("Major: " + major + " is not acceptable");
            }

            String grade = fileInfo.get(5);

            if(grade.equals("A")){
                statsHandler.incNumA();
                //System.out.println("Added A");
            }
            else if(grade.equals("B")){
                statsHandler.incNumB();
                //System.out.println("Added B");
            }
            else if(grade.equals("C")){
                statsHandler.incNumC();
                //System.out.println("Added C");
            }
            else if(grade.equals("D")){
                statsHandler.incNumD();
                //System.out.println("Added D");
            }
            else if(grade.equals("F")){
                statsHandler.incNumF();
                //System.out.println("Added F");
            }
            else{
                System.out.println("Grade: " + grade + " is not acceptable");
            }

        }

        pieChartData.clear();

        //method 1 formatting
        /*pieChartData.addAll(new PieChart.Data(String.format("Biology %.2f", statsHandler.getPercentBiology()), statsHandler.getNumBiology()),
                new PieChart.Data(String.format("Software Engineering %.2f", statsHandler.getPercentSoftwareEngineering()), statsHandler.getNumSoftwareEngineering()),
                new PieChart.Data(String.format("Mathematics %.2f", statsHandler.getPercentMathematics()), statsHandler.getNumMathematics()),
                new PieChart.Data(String.format("Art %.2f", statsHandler.getPercentArt()), statsHandler.getNumArt()),
                new PieChart.Data(String.format("Underwater Basket Weaving %.2f", statsHandler.getPercentUnderwaterBasketWeaving()), statsHandler.getNumUnderwaterBasketWeaving())
        );*/

        //method 2 formatting
        pieChartData.addAll(new PieChart.Data(statsHandler.getPercentBiology(), statsHandler.getNumBiology()),
                new PieChart.Data(statsHandler.getPercentSoftwareEngineering(), statsHandler.getNumSoftwareEngineering()),
                new PieChart.Data(statsHandler.getPercentMathematics(), statsHandler.getNumMathematics()),
                new PieChart.Data(statsHandler.getPercentArt(), statsHandler.getNumArt()),
                new PieChart.Data(statsHandler.getPercentUnderwaterBasketWeaving(), statsHandler.getNumUnderwaterBasketWeaving())
        );

        pieChart.setData(pieChartData);


        barChartData.getData().clear();
        barChartData.setName("Current Student Roster");
        barChartData.getData().add(new XYChart.Data("A", statsHandler.getNumA()));
        barChartData.getData().add(new XYChart.Data("B", statsHandler.getNumB()));
        barChartData.getData().add(new XYChart.Data("C", statsHandler.getNumC()));
        barChartData.getData().add(new XYChart.Data("D", statsHandler.getNumD()));
        barChartData.getData().add(new XYChart.Data("F", statsHandler.getNumF()));

    }

    public void initialize(Stage stage){
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~[TABLE]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        //make column, set onClick listener, allow to be editable for all Student data:

        //studentId
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        //set onClickListener
        columnId.setCellFactory(TextFieldTableCell.forTableColumn());

        //lastName
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        //set onClickListener
        columnLastName.setCellFactory(TextFieldTableCell.forTableColumn());

        //firstName
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        //set onClickListener
        columnFirstName.setCellFactory(TextFieldTableCell.forTableColumn());

        //major
        columnMajor.setCellValueFactory(new PropertyValueFactory<>("major"));
        //set onClickListener
        columnMajor.setCellFactory(ComboBoxTableCell.forTableColumn("Biology", "Software Engineering", "Mathematics", "Art", "Underwater Basket Weaving"));

        //grades
        columnGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        //set onClickListener
        columnGrade.setCellFactory(ComboBoxTableCell.forTableColumn("A", "B", "C", "D", "F"));

        //gradeOptions
        columnGradeOptions.setCellValueFactory(new PropertyValueFactory<>("gradeOptions"));
        //set onClickListener
        columnGradeOptions.setCellFactory(ComboBoxTableCell.forTableColumn("Pass/Not Pass", "Letter Grade"));

        //honorsStatus
        columnHonorsStatus.setCellValueFactory(new PropertyValueFactory<Student, CheckBox>("honorsStatus"));

        //imageFileName
        columnImageFileName.setCellValueFactory(new PropertyValueFactory<Student, ImageView> ("imageFileName"));
        //set onClickListener
        //columnImageFileName.setCellFactory(TextFieldTableCell.forTableColumn());
        //allow it to be editable
        /*columnImageFileName.setOnEditCommit((TableColumn.CellEditEvent<Student,ImageView> t)->{
            t.getRowValue().setImageFileName(t.getNewValue()); //~~~ maybe bind here with values in our roster!
        });*/

        //studentNotes
        columnStudentNotes.setCellValueFactory(new PropertyValueFactory<>("studentNotes"));
        //set onClickListener
        columnStudentNotes.setCellFactory(TextFieldTableCell.forTableColumn());

        //create our table and add all columns

        studentTable.setItems(listStudents);
        /*studentTable.getColumns().addAll(columnId, columnLastName, columnFirstName,
                columnMajor, columnGrade, columnGradeOptions, columnHonorsStatus, columnImageFileName, columnStudentNotes);*/

        studentTable.setFixedCellSize(100);
        //set column widths
        columnId.prefWidthProperty().bind(studentTable.widthProperty().divide(9));
        columnLastName.prefWidthProperty().bind(studentTable.widthProperty().divide(9));
        columnFirstName.prefWidthProperty().bind(studentTable.widthProperty().divide(9));
        columnMajor.prefWidthProperty().bind(studentTable.widthProperty().divide(9));
        columnGrade.prefWidthProperty().bind(studentTable.widthProperty().divide(9));
        columnGradeOptions.prefWidthProperty().bind(studentTable.widthProperty().divide(9));
        columnHonorsStatus.prefWidthProperty().bind(studentTable.widthProperty().divide(9));
        columnImageFileName.prefWidthProperty().bind(studentTable.widthProperty().divide(9));
        columnStudentNotes.prefWidthProperty().bind(studentTable.widthProperty().divide(9));

        //set table to be editable
        studentTable.setEditable(true);

        //pie chart
        pieChart.setTitle("Major Distribution");
        pieChart.setLegendSide(Side.LEFT);
        //bar chart
        barChart.getData().add(barChartData);
        barChart.setTitle("Grade Distribution");
        xAxis.setLabel("Grades");
        yAxis.setLabel("Number of Students");

        //shadow effect on text
        logoText.setEffect(new DropShadow());
        logoText.setFont(Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, 22));

        //reflection
        Reflection reflection = new Reflection();
        reflection.setFraction(0.5);
        logoSquare.setEffect(reflection);
        logoSquare.setAlignment(Pos.CENTER);

        //light effects for text
        Lighting logoLighting = new Lighting();
        Light.Distant distLight = new Light.Distant();
        distLight.setAzimuth(0);
        logoLighting.setLight(distLight);
        logoText.setEffect(logoLighting);



        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~[ROW SELECTOR METHOD]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        studentTable.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Student>() {
            @Override
            public void onChanged(Change<? extends Student> change) {

                for (Student student : change.getList()){
                    String studentFile = rosterDirectory
                            + "\\"
                            + student.getId()
                            + "_"
                            + student.getLastName()
                            + "_"
                            + student.getFirstName()
                            +".txt";

                    File file = new File(studentFile);

                    if(file.exists()){
                        currentStudent = allStudentFiles.indexOf(file);
                    }
                    else{
                        System.out.println("File does not exist!!!");
                    }

                    try {
                        showCurrentStudent(currentStudent, ourStage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        ourStage = stage;
        loadStudentRoster();
        try {
            populateStudentTable();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            populateStatistics();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(!allStudentFiles.isEmpty()){
            System.out.println("Roster filled prior to new session");
            try {
                showCurrentStudent(currentStudent, stage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("Roster is empty");
        }



    }



}
