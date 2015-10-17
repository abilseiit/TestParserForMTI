package classes;

import formes.FrmMain;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import javax.swing.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


public class Parser {
    private static String login, pass, link;
    Connection connection;
    private static WebDriver webDriver = new HtmlUnitDriver();

    //авторизация на сайте (логин, пароль и ссылка на тест)
    protected boolean logIn(String login, String pass, String testUrl){
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);

        webDriver.navigate().to(testUrl);

        if (webDriver.getCurrentUrl().equals("https://lms.mti.edu.ru/local/login.php")){
            try{
                WebElement loginInputElement = webDriver.findElement(By.name("username"));
                loginInputElement.sendKeys(login);
                WebElement passInputElement = webDriver.findElement(By.name("password"));
                passInputElement.sendKeys(pass);
                WebElement submitBtn = webDriver.findElement(By.id("loginbtn"));
                submitBtn.click();
                if (webDriver.getTitle().equals("http://lms.mti.edu.ru/local/login.php")){
                    JOptionPane.showMessageDialog(null, "Incorrect login or password!");
                    return false;
                }else{
                    System.out.println("LogIN! "+webDriver.getTitle());
                    return true;
                }
            } catch (Exception ex){
                JOptionPane.showMessageDialog(null, "LogIn error: "+ex);
                return false;
            }
        }else{
            System.out.println("Else LogIN"+webDriver.getTitle()+" "+webDriver.getCurrentUrl());
            return true;
        }
    }

    //стартуем тест
    protected boolean startTest(){
        try {
            System.out.println("До нажатия на кнопку чести "+webDriver.getTitle());
            WebElement postMethodAgree = webDriver.findElement(By.xpath("//form[@method=\"post\"]"));
            if (postMethodAgree.getText().equals("Продолжить последнюю попытку")){
                postMethodAgree.findElement(By.xpath("//input")).click();
                return true;
            }
            postMethodAgree.findElement(By.xpath("//input")).click();
            System.out.println("После нажатия на кнопку чести "+webDriver.getTitle());
            WebElement postMethodStart = webDriver.findElement(By.xpath("//form[@method=\"post\"]"));
            postMethodStart.findElement(By.xpath("//input")).click();
            return true;
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Error: "+ex);
            return false;
        }
    }

    //проверка инета
    public boolean checkInternetConnection(){
        Socket socket = new Socket();
        InetSocketAddress address = new InetSocketAddress("www.google.com", 80);
        try {
            return true;
        } catch (Exception ex){
            javax.swing.JOptionPane.showMessageDialog(null, "Check internet connection: "+ex);
            return false;
        } finally {
            try{socket.close();}catch (Exception ex){}
        }
    }

    //сверка с бд, если ли данная таблица в бд (название предмета в качества имени)
    public boolean checkTableName(String tableName){
        try {
            Connection conn = DBConnection.dbConnector();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet rs = dbm.getTables(null, null, tableName, null);
            if (rs.next()){
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //получаем правильный ответ с бд (вопрос, название предмета)
    protected List<String> getAnswer(String question, String tableName) throws SQLException {
        Connection conn = DBConnection.dbConnector();
        Statement st = conn.createStatement();
        ResultSet rs = null;
        List<String> answerList = new ArrayList<String>();
        String sql = "SELECT answer FROM '"+tableName+"' WHERE question='"+question+"'";
        //System.out.println("REQUEST "+sql);
        if (conn != null){
            try{
                rs = st.executeQuery(sql);
                while (rs.next()){
                    System.out.println("ANSWER "+rs.getString(1));
                    answerList.add(rs.getString(1));
                }
                return answerList;
            } catch (SQLException ex){
                JOptionPane.showMessageDialog(null, "Не удалось выпольнить запрос "+ex);
                return null;
            }
            finally {
                if (rs != null)
                    rs.close();
                if (st != null)
                    st.close();
                if (conn != null)
                    conn.close();
            }
        }else{
            System.out.println("Отсуствует соединение с БД!");
            return null;
        }
    }

    //ишем на страничке правильный ответ
    protected boolean checkAnswer(String answer, String questionID){
        WebElement content = webDriver.findElement(By.xpath("//div[@id=\"" + questionID + "\"]"));
        List<WebElement> listDiv = content.findElement(By.className("answer")).findElements(By.tagName("div"));

        for (WebElement answerDiv : listDiv){
            String labelText = answerDiv.findElement(By.tagName("label")).getText();
            String labelID = answerDiv.findElement(By.tagName("label")).getAttribute("for");
            System.out.println("Text on the LABEL: "+labelText+" ID "+labelID);
            if (labelText.equalsIgnoreCase(answer)){
                if (!webDriver.findElement(By.id(labelID)).isSelected()){
                    webDriver.findElement(By.id(labelID)).click();
                    return true;
                }
            }
        }
        return false;
    }

    //получаем название предмета
    protected String getTableName(){
        return webDriver.findElement(By.xpath("//div[@class=\"breadcrumb\"]/ul/li[3]")).getText();
    }

    //заканчиваем тест
    protected void endTest(){
        WebElement btnEndTest = webDriver.findElement(By.name("next"));
        btnEndTest.click();
        System.out.println(webDriver.getCurrentUrl());
        FrmMain.setStatus(webDriver.getCurrentUrl());
    }

    //получаем вопрос
    public List<WebElement> getQuestions(){
        return webDriver.findElements(By.xpath("//div[@class='que multichoice deferredfeedback notyetanswered']"));
    }
}

