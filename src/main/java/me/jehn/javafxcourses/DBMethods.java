package me.jehn.javafxcourses;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DBMethods {
    private static final String DESKTOP_PATH = "C:\\Users\\jaxel\\OneDrive\\Desktop";
    private static final String DATABASE_PATH = DESKTOP_PATH + File.separator + "Database";
    private static final File DATABASE_FOLDER = new File(DATABASE_PATH);

    //create FOLDER on desktop
    public static void createFolder(String path, String folderName){
        File folder = new File(path, folderName);
        folder.mkdirs();
    }


    //add NEW COURSE to database
    public static void DBnewCourse(String courseName){
        createFolder(DATABASE_PATH, courseName);
    }
    //add NEW CHAPTER to COURSE
    public static void DBnewChapter(String selectedCourse, String chapterName){
        final String COURSE_PATH = DATABASE_PATH+File.separator+selectedCourse;
        final String CHAPTER_PATH = DATABASE_PATH+File.separator+selectedCourse+File.separator+chapterName;
        createFolder(COURSE_PATH, chapterName);
        createFolder(CHAPTER_PATH, "NOTES");
        createFolder(CHAPTER_PATH, "QUESTIONS");
    }
    //add NEW NOTE to CHAPTER in course
    public static void DBnewNote(String selectedCourse, String selectedChapter, String noteTitle, String noteContent){
        final String CHAPTER_PATH = DATABASE_PATH+File.separator+selectedCourse+File.separator+selectedChapter;
        final String NOTES_PATH = CHAPTER_PATH + File.separator + "NOTES";
        final String NEW_NOTE_PATH = NOTES_PATH + File.separator + noteTitle;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(NEW_NOTE_PATH+".txt"));
            bw.write(noteContent);
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //return LIST of COURSES
    public static String[] DBgetCoursesNames(){
        return DATABASE_FOLDER.list();
    }
    //return LIST of CHAPTERS in a course
    public static String[] DBgetChaptersNames(String selectedCourse){
        String coursePath = DATABASE_PATH + File.separator + selectedCourse;
        File courseFolder = new File(coursePath);
        return courseFolder.list();
    }
    //return LIST of NOTES of a chapter in a course
    public static String[] DBgetNotesTitles(String selectedCourse, String selectedChapter){
        String notesPath = DATABASE_PATH + File.separator + selectedCourse + File.separator + selectedChapter + File.separator + "NOTES";
        File notesFolder = new File(notesPath);
        return notesFolder.list();
    }
    //return NOTE CONTENT of a note in a chapter
    public static String DBgetNoteContent(String selectedCourse, String selectedChapter, String noteTitle){
        String notesPath = DATABASE_PATH + File.separator + selectedCourse + File.separator + selectedChapter + File.separator + "NOTES";
        String txtNotePath = notesPath + File.separator + noteTitle;
        try {
            String content = new String(Files.readAllBytes(Paths.get(txtNotePath)));
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }
}
