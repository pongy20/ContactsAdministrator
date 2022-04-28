package de.coerdevelopment.gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseFileGUI {

    public static JFrame createFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Datei auswählen");

        frame.setContentPane(new ChooseFileGUI(frame).contentPanel);

        frame.pack();
        frame.setMinimumSize(new Dimension(200, 200));
        frame.setLocationRelativeTo(null);
        return frame;
    }

    private JPanel contentPanel;
    private JButton btnContinue;
    private JPanel centerPanel;

    private JFileChooser fileChooser;
    private JFrame frame;

    private ChooseFileGUI(JFrame frame) {
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Kontakte", "vcf"));
        setCenterPanel();
        btnContinue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.getSelectedFile() == null) {
                    return;
                }
                frame.setVisible(false);
                ContactFrame.createFrame(fileChooser.getSelectedFile().getAbsolutePath()).setVisible(true);
            }
        });
    }

    private void setCenterPanel() {
        centerPanel.add(fileChooser, BorderLayout.CENTER);
    }

}
