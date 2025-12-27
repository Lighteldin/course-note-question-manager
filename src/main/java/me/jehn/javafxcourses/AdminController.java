package me.jehn.javafxcourses;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static me.jehn.javafxcourses.DBMethods.*;
import static me.jehn.javafxcourses.Frame.switchScene;

public class AdminController implements Initializable {

    //DECLARATION
    @FXML
    private TreeView<String> coursesTreeView;
    @FXML
    private AnchorPane paneNewCourse;
    @FXML
    private AnchorPane paneNewChapter;
    @FXML
    private AnchorPane paneNewNoteContent;
    @FXML
    private AnchorPane paneNewNoteTitle;
    @FXML
    private FlowPane paneChaptersList;
    @FXML
    private FlowPane paneSelectedChapter;
    @FXML
    private FlowPane paneCoursesList;
    @FXML
    private Label labelChapter;
    @FXML
    private Label labelCourse;
    @FXML
    private VBox paneNotes;
    @FXML
    private TextField textfieldCourse;
    @FXML
    private TextField textfieldChapter;
    @FXML
    private TextField textfieldNoteTitle;
    @FXML
    private TextArea textfieldNoteContent;
    @FXML
    private Button backButton;
    @FXML
    private Button newNoteButton;
    @FXML
    private Button submitNewNoteButton;
    @FXML
    private Button editNoteButton;

    private TreeItem<String> root = new TreeItem<String>("Courses");

    private String selectedCourse;
    private String selectedChapter;

    private int currentPage;
    private final int PAGE_MAIN = 0;
    private final int PAGE_COURSE = 1;
    private final int PAGE_CHAPTER = 2;
    private final int PAGE_NOTES = 3;
    private final int PAGE_NEW_NOTE = 4;

    private Pane[] panes;
    private TextInputControl[] textfields;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        panes = new Pane[] {paneNotes, paneNewCourse, paneNewChapter, paneCoursesList, paneChaptersList, paneSelectedChapter, paneNewNoteTitle, paneNewNoteContent};
        textfields = new TextInputControl[] {textfieldNoteContent, textfieldNoteTitle, textfieldCourse, textfieldChapter};
        currentPage = PAGE_MAIN;
        setupRoot();
        showMainPage();
    }
    //EVENT HANDLERS
    @FXML
    void homeClick(ActionEvent event) throws IOException {
        switchScene("home");
    }
    @FXML
    void backClick(ActionEvent event) throws IOException {
        switch (currentPage){
            case PAGE_MAIN:
                break;
            case PAGE_COURSE:
                showMainPage();
                break;
            case PAGE_CHAPTER:
                showCoursePage(selectedCourse);
                break;
            case PAGE_NOTES:
                newNoteButton.setVisible(false);
                showChapterPage(selectedChapter);
                break;
            case PAGE_NEW_NOTE:
                showNotesPage();
                break;
        }
    }
    @FXML
    void addCourseClick(ActionEvent event) {
        String courseName = textfieldCourse.getText();
        DBnewCourse(courseName);
        createButton(paneCoursesList, courseName);
        textfieldCourse.setText("");
    }
    @FXML
    void addChapterClick(ActionEvent event) {
        String chapterName = textfieldChapter.getText();
        DBnewChapter(selectedCourse, chapterName);
        createButton(paneChaptersList, chapterName);
        textfieldChapter.setText("");
    }
    @FXML
    void notesClick(ActionEvent event) {
        showNotesPage();
    }
    @FXML
    void questionsClick(ActionEvent event) {

    }
    @FXML
    void newNoteClick(ActionEvent event) {
        showNewNotePage();
    }
    @FXML
    void EditNoteClick(ActionEvent event) {

    }
    @FXML
    void cancelNewNoteClick(ActionEvent event) {
        showNotesPage();
    }
    @FXML
    void submitNewNoteClick(ActionEvent event) {
        String noteTitle = textfieldNoteTitle.getText();
        String noteContent = textfieldNoteContent.getText();
        DBnewNote(selectedCourse, selectedChapter, noteTitle, noteContent);
        showNotesPage();
    }
    //METHODS
    private void setupRoot(){
        root.setExpanded(true);
        coursesTreeView.setRoot(root);
    }
    private void initCoursesFromDB(){
        paneCoursesList.getChildren().clear();
        try {
            String[] coursesNames = DBgetCoursesNames();
            for (String x : coursesNames) {
                createButton(paneCoursesList, x);
            }
        } catch(NullPointerException npe){
            System.out.println("No courses yet!");
        }
    }
    private void initChaptersFromDB(){
        paneChaptersList.getChildren().clear();
        try {
            String[] chaptersNames = DBgetChaptersNames(selectedCourse);
            for (String x : chaptersNames){
                createButton(paneChaptersList, x);
            }
        }catch(NullPointerException npe){
            System.out.println(selectedCourse + " has no chapters yet!");
        }
    }
    private void initNotesFromDB(){
        paneNotes.getChildren().clear();
        try {
            String[] notesTitles = DBgetNotesTitles(selectedCourse, selectedChapter);
            for (String x : notesTitles){
                createButton(paneNotes, x);
            }
        }catch(NullPointerException npe){
            System.out.println(selectedCourse + " has no chapters yet!");
        }
    }
    private void createButton(Pane pane, String name){
        Button button = new Button(name.replaceAll(".txt",""));
        if(pane==paneNotes)
            button.setId("noteButtons");

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //clicked on a course button
                if (pane==paneCoursesList) {
                    selectedCourse = name;
                    showCoursePage(selectedCourse);
                }
                //clicked on a chapter button
                else if (pane==paneChaptersList) {
                    selectedChapter = name;
                    showChapterPage(selectedChapter);
                }
                //clicked on note button
                else if(pane==paneNotes){
                    showSelectedNotePage(name);
                }
            }
        });
        pane.getChildren().add(button);
    }
    private void showMainPage(){
        currentPage = PAGE_MAIN;
        labelChapter.setVisible(false);
        labelCourse.setText("AVAILABLE COURSES:");
        backButton.setVisible(false);

        initCoursesFromDB();

        setPanesOff();
        paneNewCourse.setVisible(true);
        paneCoursesList.setVisible(true);
    }
    private void showCoursePage(String selectedCourse){
        currentPage = PAGE_COURSE;
        labelChapter.setVisible(true);
        labelCourse.setText("VIEWING COURSE: \""+selectedCourse+"\"");
        labelChapter.setText("AVAILABLE CHAPTERS:");
        backButton.setVisible(true);

        initChaptersFromDB();

        setPanesOff();
        paneNewChapter.setVisible(true);
        paneChaptersList.setVisible(true);

    }
    private void showChapterPage(String selectedChapter){
        currentPage = PAGE_CHAPTER;
        labelChapter.setText("VIEWING CHAPTER: \""+selectedChapter+"\"");
        setPanesOff();
        paneSelectedChapter.setVisible(true);
    }
    private void showNotesPage(){
        currentPage = PAGE_NOTES;
        initNotesFromDB();

        setPanesOff();
        newNoteButton.setVisible(true);
        paneNotes.setVisible(true);
    }
    private void showNewNotePage(){
        currentPage = PAGE_NEW_NOTE;
        setPanesOff();
        newNoteButton.setVisible(false);
        editNoteButton.setVisible(false);

        submitNewNoteButton.setVisible(true);
        paneNewNoteTitle.setVisible(true);
        paneNewNoteContent.setVisible(true);
    }
    private void showSelectedNotePage(String noteTitle){
        showNewNotePage();
        submitNewNoteButton.setVisible(false);

        editNoteButton.setVisible(true);

        textfieldNoteTitle.setText(noteTitle);
        String noteContent = DBgetNoteContent(selectedCourse, selectedChapter, noteTitle);
        textfieldNoteContent.setText(noteContent);
    }
    private void setPanesOff(){
        for (Pane pane : panes){
            pane.setVisible(false);
        }
        for(TextInputControl textfield : textfields){
            textfield.setText("");
        }
    }
}