package com.example.taex;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class fExamController {
    @FXML
    private Label Q;

    @FXML
    private VBox form;

    @FXML
    private FlowPane buttons;


    private String[] questions;

    private ToggleGroup op;
    private String[] answers;
    private String[] solutions;
    private int[] ansIndex;
    private float result;
    private int number;
    private Font font;

    @FXML
    public void initialize(){
        questions=HelloApplication.getExam().split("\\$@\\(q\\)\\?");
        Button[] buttonArr=new Button[questions.length-1];
        solutions=new String[questions.length-1];
        answers=new String[questions.length-1];
        ansIndex=new int[questions.length-1];
        number=0;
        result=0f;
        font=new Font(25);
        Matcher sol=Pattern.compile("\\$@\\(a\\)!(.*?)\\$@\\(q\\)\\?").matcher(HelloApplication.getExam());
        int j=0;
        System.out.println(HelloApplication.getExam());
        while(sol.find())
        {
            solutions[j]=sol.group(1).trim();
            j++;
        }
        solutions[solutions.length-1]=HelloApplication.getExam().substring(HelloApplication.getExam().lastIndexOf("$@(a)!")+6).trim();

        op=new ToggleGroup();

        for(int i=0;i<buttonArr.length;i++)
        {
            if(buttonArr[i]==null)
                buttonArr[i]=new Button();
            buttonArr[i].setText(String.valueOf(i+1));
            buttonArr[i].setFont(new Font(15));
            buttons.getChildren().add(buttonArr[i]);
            buttonArr[i].setOnAction(e->{ questionNo(e);           });
        }
        buttonArr[0].fire();

    }
    @FXML
    public void questionNo(ActionEvent event) {
        List childOfForm=form.getChildren();
        String fullQ;
        fullQ = questions[Integer.parseInt(((Button) event.getSource()).getText())];
        //System.out.println(fullQ);
        Matcher findQ = Pattern.compile("(.*?)(?:\\$@\\(o\\)!|\\$@\\(a\\)!)").matcher(fullQ);
        if (findQ.find()) {
           // System.out.println(findQ.group(1));
            Q.setText(findQ.group(1));
        }
        int numberOfOptions =0;
        Matcher noOp = Pattern.compile("\\$@\\(o\\)!").matcher(fullQ);
        int temp=childOfForm.size();
        // System.out.println(i);
        if (temp > 1) {
            childOfForm.subList(1, temp).clear();  //clearing the previous options
        }
        while(noOp.find())
        {
            numberOfOptions++;
        }
        RadioButton[] options=new RadioButton[numberOfOptions];
        String s=fullQ;
        int end=0;
        int i=0;
        Pattern p=Pattern.compile("\\$@\\(o\\)!(.*?)(\\$@\\(o\\)!|\\$@\\(a\\)!)");
        Matcher m;
        int opNum=0;
        while(end<s.length())
        {s=s.substring(end);
            m=p.matcher(s);
            if(m.find())
            {
                opNum++;
                if(options[i]==null)
                    options[i]=new RadioButton(m.group(1).trim());
                options[i].setId("q"+((Button) event.getSource()).getText()+"o"+opNum);
                options[i].setToggleGroup(op);
                options[i].setFont(font);
                options[i].setOnAction(e->{select(e); });
                childOfForm.add(options[i]);
               // System.out.println(m.group(1));
               // System.out.println(end);
                end=m.end()-7;
                i++;
            }
        }
        if(answers[Integer.parseInt(((Button) event.getSource()).getText())-1]!=null)
        {
            ((RadioButton)childOfForm.get(ansIndex[Integer.parseInt(((Button) event.getSource()).getText())-1])).setSelected(true);
        }
    }
    @FXML
    public void select(ActionEvent event){


        int qNumber=Integer.parseInt(((RadioButton) event.getSource()).getId().substring(1,((RadioButton) event.getSource()).getId().indexOf("o")));
        answers[qNumber - 1] = ((RadioButton) event.getSource()).getText().trim();
        ansIndex[qNumber - 1] = Integer.parseInt(((RadioButton) event.getSource()).getId().substring(((RadioButton) event.getSource()).getId().indexOf("o") + 1, ((RadioButton) event.getSource()).getId().length()));

    }

    @FXML
    public void submit() throws SQLException {
        for(int i=0;i<solutions.length;i++)
        {
            if(answers[i]==null)
                continue;
            if(answers[i].equals(solutions[i]))
            {
                number++;
            }
        }
        System.out.println(number);
        result=((float)number)/(float)solutions.length*100;
        Statement stmt = HelloApplication.con.createStatement();
        stmt.execute("insert into result values('"+HelloApplication.stu_id+"', '"+HelloApplication.getEic()+"', "+Float.parseFloat(String.format("%.2f",result))+");");
        System.out.println(String.format("%.2f",result));
        System.exit(0);

    }
}
