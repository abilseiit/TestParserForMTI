package formes;

import classes.StartTest;
import sun.plugin2.message.JavaObjectOpMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by abilseiit on 21.12.2014.
 */
public class FrmMain extends JFrame {
    private JTextField txtLogin;
    private JPasswordField txtPass;
    private JTextField txtLink;
    private JButton btnStart;
    private JButton btnCancel;
    private static JTextArea txtAreaStatus;
    private JLabel lblLogin;
    private JLabel lblPass;
    private JLabel lblLink;
    private String login;
    private String pass;
    private String link;
    private Thread thread;
    private JScrollPane scrollPane;
    private JLabel lblStatus;
    //Конструктор
    public FrmMain() {
        JFrame frame = new JFrame("TestParserForMTI");
        frame.setMinimumSize(new Dimension(530, 400));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridBagLayout());

        //Дизайн
        lblLogin = new JLabel("Логин");
        txtLogin = new JTextField(15);
        lblPass = new JLabel("Пароль");
        txtPass = new JPasswordField(15);
        lblLink = new JLabel("Ссылка на тест");
        txtLink = new JTextField(15);
        btnStart = new JButton("Начать");
        btnStart.addActionListener(new StartTestButtonListener());
        btnCancel = new JButton("Отмена");

        lblStatus = new JLabel("Статус");
        txtAreaStatus = new JTextArea(10, 50);
        txtAreaStatus.setMinimumSize(new Dimension(150, 50));
        scrollPane = new JScrollPane(txtAreaStatus, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //txtAreaStatus.setEditable(false);


        frame.add(lblLogin, new GridBagConstraints(0, 0, 1, 1, 1, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));

        frame.add(lblPass, new GridBagConstraints(1, 0, 1, 1, 1, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));

        frame.add(txtLogin, new GridBagConstraints(0, 1, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));

        frame.add(txtPass, new GridBagConstraints(1, 1, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));

        frame.add(lblLink, new GridBagConstraints(0, 2, 2, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));

        frame.add(txtLink, new GridBagConstraints(0, 3, 2, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));

        frame.add(btnStart, new GridBagConstraints(0, 4, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));

        frame.add(btnCancel, new GridBagConstraints(1, 4, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));

        frame.add(lblStatus, new GridBagConstraints(0, 5, 2, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));

        frame.add(scrollPane, new GridBagConstraints(0, 6, 2, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0));

        frame.setVisible(true);
        frame.pack();
    }

    public static void setStatus(String status){
        txtAreaStatus.append(status+"\n");
    }

    public class StartTestButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Runnable startTest = new StartTest(txtLogin.getText(), txtPass.getText(), txtLink.getText());
                thread = new Thread(startTest);
                thread.start();
            }catch (Exception ex){
                JOptionPane.showMessageDialog(null, "Error, didn't run thread: "+ex);
            }
        }
    }

}