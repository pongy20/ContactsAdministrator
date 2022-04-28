package de.coerdevelopment.gui;

import de.coerdevelopment.contacts.VCardReader;
import ezvcard.VCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ContactFrame {

    public static JFrame createFrame(String filepath) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Kontakte");

        List<VCard> cards = VCardReader.readContacts(new File(filepath));
        ContactFrame cFrame = new ContactFrame(cards);
        frame.setContentPane(cFrame.contentPanel);

        frame.pack();
        frame.setMinimumSize(new Dimension(300, 450));
        frame.setLocationRelativeTo(null);
        return frame;
    }

    private JPanel contentPanel;
    private JPanel sidePanel;
    private JPanel infoPanel;
    private JPanel searchPanel;
    private JScrollPane contactsPanel;
    private JList contactsList;
    private JButton searchButton;
    private JTextField txtSearch;
    private JLabel lblName;
    private JLabel lblPrename;
    private JLabel lblLastname;
    private JLabel lblMail;
    private JTextField txtPrename;
    private JTextField txtLastname;
    private JTextField txtMail;
    private JLabel lblTel;
    private JTextField txtTel;
    private JButton sendMailButton;
    private JLabel lblNote;
    private JTextField txtNote;
    private JPanel centerPanel;
    private JLabel lblFullname;

    private List<VCard> contacts;
    private DefaultListModel listModel;

    private ContactFrame(List<VCard> contacts) {
        this.contacts = contacts;
        listModel = new DefaultListModel();
        updateContactList();
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateContactList();
            }
        });
        sendMailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mail = getMail(getCurrentSelectedCard());
                if (mail.isBlank()) {
                    return;
                }
                try {
                    Desktop.getDesktop().mail(new URI("mailto:" + mail));
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        });
        contactsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateCenterPanel();
            }
        });
        updateCenterPanel();
    }

    public void updateContactList() {
        String searchText = txtSearch.getText();

        listModel.clear();
        for (VCard card : contacts) {
            if (!searchText.isBlank()) {
                if (!card.getFormattedName().getValue().toLowerCase().contains(searchText.toLowerCase())) {
                    continue;
                }
            }
            listModel.addElement(card.getFormattedName().getValue());
        }
        contactsList.setModel(listModel);
    }

    public void updateCenterPanel() {
        VCard card = getCurrentSelectedCard();
        if (card == null) {
            if (contacts.get(0) == null) {
                return;
            } else {
                card = contacts.get(0);
            }
        }
        lblFullname.setText(getFullName(card));
        txtPrename.setText(getFirstName(card));
        txtLastname.setText(getLastName(card));
        txtMail.setText(getMail(card));
        txtNote.setText(getNote(card));
        txtTel.setText(getTel(card));

        if (getMail(card).isBlank()) {
            sendMailButton.setEnabled(false);
        } else {
            sendMailButton.setEnabled(true);
        }
    }

    private VCard getCurrentSelectedCard() {
        if (contactsList.getSelectedValue() == null) {
            return null;
        }
        String name = contactsList.getSelectedValue().toString();
        for (VCard card : contacts) {
            if (card.getFormattedName().getValue().equals(name)) {
                return card;
            }
        }
        return null;
    }

    private String getFullName(VCard card) {
        return card.getFormattedName().getValue() != null ? card.getFormattedName().getValue() : "";
    }
    private String getFirstName(VCard card) {
        return card.getStructuredName().getGiven() != null ? card.getStructuredName().getGiven() : "";
    }
    private String getLastName(VCard card) {
        return card.getStructuredName().getFamily() != null ? card.getStructuredName().getFamily() : "";
    }
    private String getMail(VCard card) {
        return card.getEmails() != null && !card.getEmails().isEmpty() ? card.getEmails().get(0).getValue() : "";
    }
    private String getTel(VCard card) {
        return card.getTelephoneNumbers() != null && !card.getTelephoneNumbers().isEmpty() ? card.getTelephoneNumbers().get(0).getText() : "";
    }
    private String getNote(VCard card) {
        return card.getNotes() != null && !card.getNotes().isEmpty() ? card.getNotes().get(0).getValue() : "";
    }

}
