package classes;

import formes.FrmMain;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StartTest implements Runnable {
    private String login, pass, link;

    public StartTest(String login, String pass, String link){
        this.login = login;
        this.pass = pass;
        this.link = link;
    }

    @Override
    public void run() {
        Parser parser = new Parser();

        //проверяем коннект с инетом
        if (parser.checkInternetConnection()){
            //авторизация на сайте
            if (parser.logIn(login, pass, link)){
                //начинаем тест
                if (parser.startTest()){
                    List<WebElement> listQuestions = parser.getQuestions();
                    System.out.println("Общее кол. вопросов: " + listQuestions.size());
                    FrmMain.setStatus("Общее кол. вопросов: " + listQuestions.size());
                    String tableName = parser.getTableName();
                    System.out.println("Table Name: "+tableName);
                    FrmMain.setStatus("Название предмета: " + tableName);
                    List<String> listAnswer = new ArrayList<String>();
                    String questionTxt = "";
                    String questionID = "";
                    int i = 1;
                    //получаем вопрос
                    lab: for (WebElement question : listQuestions){
                        i++;
                        questionID = question.getAttribute("id");
                        questionTxt = question.findElement(By.className("qtext")).getText();
                        //убираем лишние пробелы в начале вопроса
                        char firstChar = questionTxt.charAt(0);
                        if (firstChar == ' '){
                            String s = questionTxt.substring(1);
                            questionTxt = s;
                        }
                        //проверка есть ли в БД ответы по данному предмету
                        if (!parser.checkTableName(tableName)){
                            System.out.println("Отсуствует таблица с ответами для данного предмета!");
                            FrmMain.setStatus("Отсуствует таблица с ответами для данного предмета!");
                            break lab;
                        }
                        System.out.println("Вопрос №"+i+" "+questionTxt+" ID "+questionID);
                        FrmMain.setStatus("Вопрос №: "+i+"\n"+questionTxt);
                        //ищем ответ
                        try {
                            listAnswer = parser.getAnswer(questionTxt, tableName);
                            if (listAnswer != null){
                                for(String answer : listAnswer){
                                    if (parser.checkAnswer(answer, questionID) == true){
                                        System.out.println("Ответ: " + answer);
                                        FrmMain.setStatus("Ответ: " + answer);
                                        continue lab;
                                    }
                                }
                            }else{
                                System.out.println("Нету ответа на вопрос!");
                                FrmMain.setStatus("Не найден ответ на вопрос.");
                                continue lab;
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                }
                parser.endTest();
            }
        }
    }
}
