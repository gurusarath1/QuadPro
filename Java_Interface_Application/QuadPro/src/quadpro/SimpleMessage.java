/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadpro;

import javax.swing.*;

/**
 *
 * @author gsgur
 */
public class SimpleMessage implements GLOBAL_CONSTANTS {

    public JProgressBar CAL_Progress = new JProgressBar();
    public int Progress = 0;
    public JFrame msgBox;

    SimpleMessage(String Title, String Message) {

        msgBox = new JFrame();
        msgBox.setTitle(Title);
        msgBox.setSize(300, 100);

        JLabel message = new JLabel(Message);
        message.setFont(GENERAL_FONT);
        msgBox.add(message);
        msgBox.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        msgBox.setVisible(true);

    }

    SimpleMessage(String Title, String Post_Message, Boolean ProgressBar, int Min, int Max) {

        msgBox = new JFrame();
        msgBox.setTitle(Title);
        msgBox.setSize(300, 100);

        JLabel message = new JLabel(Post_Message);
        message.setFont(GENERAL_FONT);
        msgBox.add(message);
        msgBox.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        msgBox.add(CAL_Progress);
        CAL_Progress.setMinimum(Min);
        CAL_Progress.setMaximum(Max);

        msgBox.setVisible(true);

    }

}
