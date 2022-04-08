package com.example.taex;

import javafx.event.ActionEvent;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.*;

public class HelloController {

    static String temp;
    private static Stage examStage;
    private Scene examS;
    private FXMLLoader exam;

    @FXML
    private Label welcomeText;
    @FXML
    private TextField id;
    @FXML
    private PasswordField pass;
    @FXML
    private Label error;
    @FXML
    private Button login;
    @FXML
    private Label llll;

    @FXML
    private TextField Eic;

    @FXML
    private Button ok;

    @FXML
    private VBox vbox;


    @FXML
    protected void onHelloButtonClick(ActionEvent event) {
        try {
            int q = 0;
            String cAnswere = "";

            Statement stmt = HelloApplication.con.createStatement();
            HelloApplication.stu_id=id.getText().trim();
            ResultSet rs = stmt.executeQuery("select stu_name from students where stu_id='" + id.getText().trim() + "' and stu_pass='" + pass.getText().trim() + "';");
/*
            while(rs.next())
                System.out.println(rs.getString(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
*/
            if (!rs.next()) {
                System.out.println("wrong username or password");
                error.setText("wrong username or password");
            } else {
                System.out.println("Kaam kre h");
                examStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                exam = new FXMLLoader(HelloApplication.class.getResource("exam.fxml"));

                examS = new Scene(exam.load(), 320, 400);
                HelloController e = exam.getController();
                e.shareToExam();
                examStage.setScene(examS);
                //examStage.setFullScreen(true);

                examStage.show();


//                System.out.println("Hello "+rs.getString(1));
//            PreparedStatement ps=HelloApplication.con.prepareStatement("select exam from exam where EIC=?");
//            ps.setString(1,"first");
//            ResultSet i=ps.executeQuery();
//            i.next();
//            Blob x = i.getBlob(1);
//            Scanner sca=new Scanner(x.getBinaryStream());
//            System.out.println("Here is your exam");
//            double noQ=0;
//            Scanner in=new Scanner(System.in);
//            while(sca.hasNextLine())
//
//            {   q++;
//                temp=sca.nextLine().trim();
//                if(Pattern.matches("[0-9]+",temp)){
//                    noQ=Double.parseDouble(temp.trim());
//                    break;}
//                System.out.println(temp);
//                if(q%5==0)
//                {
//                    System.out.println("type your ans");
//                    cAnswere=cAnswere+in.next()+" ";
//                }
//            }
//            System.out.println("Your resut is ");
//            Scanner chAns=new Scanner(cAnswere);
//            double per=0;
//            while(chAns.hasNext()){
//                if(sca.next().trim().equals(chAns.next().trim()))
//                    per++;
//            }
//            double result= (per/noQ)*100;
//            System.out.println((per/noQ)*100+"%");
//            stmt.executeUpdate("update students set result="+result+" where stu_id='"+id.getText().trim()+"'");
//            HelloApplication.con.close();
            }

        } catch (Exception e) {
            System.out.println(e);
        }


    }

    private void shareToExam() {
        Screen[] a = Screen.getScreens().toArray(new Screen[0]);
        if (a.length > 1) {
            llll.setText("Sorry you are connected to more than one monitor");
            vbox.getChildren().remove(Eic);
            System.out.println(a[1].hashCode());
        } else {
            llll.setText("please enter your EIC \n (Exam invitation code)");
            ok.setText("ok");
            ok.setOnAction(e -> {
                goAhead(e);
            });
        }
        System.out.println(a[0].hashCode());
    }

    @FXML
    protected void enter() {
        login.fire();
    }

    @FXML
    protected void test() {

        HelloApplication.setEic(Eic.getText());
        System.out.println(Eic.getText());
        System.exit(1);
    }

    @FXML
    public void goAhead(ActionEvent event) {

        try {
            Statement stmt = HelloApplication.con.createStatement();
            ResultSet exam = stmt.executeQuery("select exam from exam where EIC='" + Eic.getText().trim() + "';");
            if (!exam.next()) {
                System.out.println("wrong EIC");
                error.setText("wrong EIC");
            } else {
                HelloApplication.setExam(exam.getString(1));
               // System.out.println(exam.getString(1));
                FXMLLoader fExam = new FXMLLoader(HelloApplication.class.getResource("fExam.fxml"));
                HelloApplication.setEic(Eic.getText());
                Scene fexamS=new Scene(fExam.load());

                examStage.setScene(fexamS);
                examStage.show();
                examStage.setMaximized(true);
                examStage.setResizable(false);
//                examStage.setFullScreen(true);

                examStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent windowEvent) {
                        windowEvent.consume();
                    }
                });

            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }


    }
}