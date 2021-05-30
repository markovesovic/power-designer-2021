package classEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CollaborationStage extends JFrame {

    private CanvasArea canvas1;
    private CanvasArea canvas2;
    private CanvasArea canvas3;
    private JButton btn1;
    private JButton btn2;
    private JButton btn3;

    public CollaborationStage (Env env)
    {
        setBounds(0, 0, 2000, 600);
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);
        setTitle("Shape Editor");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if(confirm("Do you want close graphic editor?"))
                    dispose();
            }
        });

        canvas1 = new CanvasArea(env);
        canvas2 = new CanvasArea(env);
        canvas3 = new CanvasArea(env);
        btn1 = new JButton("Choose");
        btn2 = new JButton("Choose");
        btn3 = new JButton("Choose");

        Container pane = getContentPane();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();



        constraints.insets = new Insets(2, 2, 2, 2);
        constraints.fill = GridBagConstraints.BOTH;

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridheight = 2;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        pane.add(canvas1, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridheight = 2;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        pane.add(canvas2, constraints);

        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.gridheight = 2;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        pane.add(canvas3, constraints);


        add(btn1);

       add(btn2);

        add(btn3);

        setVisible(true);
    }

    public boolean confirm(String message, String title) {
        return JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this, message, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    public boolean confirm(String message) {
        return confirm(message, null);
    }
}
