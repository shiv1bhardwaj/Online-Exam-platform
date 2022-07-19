package com.example.taex;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class HelloApplication extends Application {
    static Connection con;
    static boolean eOrNot=false;
    private static String eic;
    public static String title;
    private static String exam;
    static String stu_id;
    static{
        try{
        Class.forName("com.mysql.jdbc.Driver");
        con= DriverManager.getConnection(
                "jdbc:mysql://192.168.43.166:3306/project","shiv","bshiv577@");

    }
        catch(Exception e){
            eOrNot=true;
        }
    }

    public static void setEic(String eica) {
        eic = eica;
    }

    public static String getEic() {
        return eic;
    }

    public static void setExam(String exam) {
        HelloApplication.exam = exam;
    }

    public static String getExam() {
        return exam;
    }


    @Override
    public void start(Stage stage) throws IOException {
//        stage.setFullScreen(true);
        double outputScaleX = Screen.getPrimary().getOutputScaleX();
        double outputScaleY = Screen.getPrimary().getOutputScaleY();

        Scene scene;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        FXMLLoader fxmlLoader1 = new FXMLLoader(HelloApplication.class.getResource("exception-view.fxml"));
        if(eOrNot) {
            scene = new Scene(fxmlLoader1.load(), 320, 400);
        }
            else
            scene = new Scene(fxmlLoader.load(), 320, 400);
        stage.setTitle("Taex");
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.initStyle(StageStyle.UNDECORATED);
//        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent windowEvent) {
//                windowEvent.consume();
//            }
//        });
        stage.show();



    }

    public static void main(String[] args) {
        launch();

    }
}