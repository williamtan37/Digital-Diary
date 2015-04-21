import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.text.*;
import java.io.*;
import javax.swing.text.DefaultCaret;

public class Diary {
    private LinkedList<User> userDatabase = new LinkedList();
    private LinkedList<Contact> contactDatabase = new LinkedList();
    private LinkedList<Reminder> reminderDatabase = new LinkedList();

    private final String USER_LIST_TITLE = "FIRST NAME | LAST NAME | AGE | GENDER | EMAIL | PASSWORD | CONTACT LIST | REMINDER LIST | NOTES LIST";
    private final String CONTACT_LIST_TITLE = "FIRST NAME | LAST NAME | EMAIL | AGE | GENDER | PHONE NUMBER | PICTURE DIRECTORY";
    private final String REMINDER_LIST_TITLE = "TITLE | MESSAGE | LOCATION | ALARM MESSAGE | START DATE | END DATE";
    private final File USER_LIST_PATH = new File("/Users/SuchenTan/Desktop/Digital Diary/TextFiles/UserList.txt");

    private BufferedReader textInputStream;
    private BufferedWriter textOutputStream;
    private final String TITLE = "Diary";
    private final ImageIcon LOGO = new ImageIcon("/Users/SuchenTan/Desktop/Digital Diary/Images/finaldiarylogo.png");
    private final int PORT = 6000;

    private final int WINDOW_X = 820;
    private final int WINDOW_Y = 600;

    private JFrame frame = new JFrame();

    private JLabel logolabel = new JLabel(LOGO);

    private JTextField emailLabel = new JTextField("Email", 25);
    private JPasswordField passwordLabel = new JPasswordField("Password", 25);
    private JButton signInButton = new JButton("Sign In");
    private JButton newAccountButton = new JButton("New Account");

    private char originalEchoChar = passwordLabel.getEchoChar();

    private final Color bluebackgroundColor = new Color(0, 175, 240),
    greyTextColor = new Color(160, 160, 200), 
    lightBlueFocusLostTextFieldColor = new Color(204, 239, 252);

    private Font font = new Font(emailLabel.getName(), Font.PLAIN, 14);

    private String email, password, firstName, lastName, comfirmPassword,dataReceived;
    private boolean accessGranted;

    Contact selectedContact;
    String contactImgDirectory, reminderImgDirectory;

    String notesInformation;

    Icon transparentImg = new ImageIcon("/Users/SuchenTan/Desktop/Digital Diary/Images/transparent");

    JScrollPane contactsScrollPane;
    JList contactsList;
    JButton contactsButton;
    JTextArea notesTextArea;
    JButton contactsOptionsButton;
    JPanel panel1,panel2;

    File CONTACT_LIST_PATH;
    File REMINDER_LIST_PATH;
    File NOTES_LIST_PATH;

    JButton saveNotesButton;
    JButton remindersButton;
    JButton remindersOptionsButton;

    JList reminderList;
    JScrollPane reminderScrollPane;
    Reminder selectedReminder;
    JScrollPane noteScrollPane;

    PrintWriter out;

    public Diary() {
        frame.getContentPane().setBackground(bluebackgroundColor);

        frame.setSize(WINDOW_X, WINDOW_Y);
        frame.setTitle(TITLE);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridBagLayout());

    }

    public void init() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
        | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(frame);

        updateUserDatabase();
        logIn();      
        frame.setVisible(true);
    }

    public void logIn() {
        emailLabel.setForeground(greyTextColor);
        passwordLabel.setForeground(greyTextColor);
        emailLabel.setBackground(lightBlueFocusLostTextFieldColor);
        passwordLabel.setBackground(lightBlueFocusLostTextFieldColor);

        emailLabel.setFont(font);
        passwordLabel.setFont(font);

        //***************************************************************************************************
        // Logo
        //***************************************************************************************************
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 40, 0);
        frame.add(logolabel, c);

        //***************************************************************************************************
        // Email Field
        //***************************************************************************************************
        c.gridy++;
        c.ipady = 15;
        c.insets = new Insets(0, 0, 12, 0);
        frame.add(emailLabel, c);
        emailLabel.addFocusListener(new FocusListener() {

                @Override
                public void focusLost(FocusEvent arg0) {
                    if (emailLabel.getText().equals("")) {
                        emailLabel.setForeground(greyTextColor);
                        emailLabel.setText("Email");
                    }
                    emailLabel.setBackground(lightBlueFocusLostTextFieldColor);
                }

                @Override
                public void focusGained(FocusEvent arg0) {

                    if (emailLabel.getText().equals("Email")) {
                        emailLabel.setText(null);
                        emailLabel.setForeground(Color.BLACK);
                    }
                    emailLabel.setBackground(Color.WHITE);

                }
            });

        //***************************************************************************************************
        // Password Field
        //***************************************************************************************************   
        passwordLabel.setEchoChar((char) 0);
        c.gridy++;
        frame.add(passwordLabel, c);
        passwordLabel.addFocusListener(new FocusListener() {
                public void focusLost(FocusEvent arg0) {
                    if (passwordLabel.getText().equals("")) {
                        passwordLabel.setEchoChar((char) 0);
                        passwordLabel.setForeground(greyTextColor);
                        passwordLabel.setText("Password");
                    }
                    passwordLabel.setBackground(lightBlueFocusLostTextFieldColor);
                }           

                public void focusGained(FocusEvent arg0) {
                    if (passwordLabel.getText().equals("Password")) {
                        passwordLabel.setEchoChar(originalEchoChar);
                        passwordLabel.setText("");
                        passwordLabel.setForeground(Color.BLACK);
                    }
                    passwordLabel.setBackground(Color.WHITE);
                }
            });

        //***************************************************************************************************
        // SignIn button
        //***************************************************************************************************
        c.gridy++;
        frame.add(signInButton, c);

        signInButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    email = emailLabel.getText();
                    password = passwordLabel.getText();

                    validateUser(email, password);
                }
            });        
    }

    public void validateUser(String email, String password)
    {
        User targetUser = null;
        boolean isFound = false;
        for(int i = 0; i < userDatabase.size(); i++)
        {
            if(email.equalsIgnoreCase(userDatabase.get(i).getEmail()) && password.equals(userDatabase.get(i).getPassword()))
            {
                targetUser = userDatabase.get(i);
                isFound = true;
                break;
            }                    
        }

        if(isFound == true)
        {
            userInterface(targetUser);
        }

        else
            JOptionPane.showMessageDialog(frame, "Invalid E-mail or Password!","Error", JOptionPane.ERROR_MESSAGE);
    }

    public void userInterface(User user)
    {
        CONTACT_LIST_PATH = new File("/Users/SuchenTan/Desktop/Digital Diary/TextFiles/" +user.getContactListLocation());
        REMINDER_LIST_PATH = new File("/Users/SuchenTan/Desktop/Digital Diary/TextFiles/" + user.getReminderListLocation());
        NOTES_LIST_PATH = new File("/Users/SuchenTan/Desktop/Digital Diary/TextFiles/" + user.getNotesListLocation());

        updateContactDatabase(CONTACT_LIST_PATH);
        updateReminderDatabase(REMINDER_LIST_PATH);
        updateNotesDatabase(NOTES_LIST_PATH);
        frame.remove(logolabel);
        frame.remove(emailLabel);
        frame.remove(passwordLabel);
        frame.remove(signInButton);

        loadingAnimation();

        Timer timer = new Timer();

        timer.schedule( new TimerTask(){
                public void run(){
                    frame.remove(loadingLabel);
                    loadUI();
                    checkReminders();
                }
            },1000);

    }

    public void checkReminders()
    {
        LinkedList<Reminder> remindersToday = new LinkedList();
        Date today = new Date();
        int numReminderToday = 0;

        for(int i = 0; i< reminderDatabase.size(); i++)
        {

            if(today.getDay() == reminderDatabase.get(i).getEndDate().getDay() && today.getYear() == reminderDatabase.get(i).getEndDate().getYear() 
            && today.getMonth() == reminderDatabase.get(i).getEndDate().getMonth())
            {
                numReminderToday++;
                remindersToday.add(reminderDatabase.get(i));
            }
        }

        if(numReminderToday > 0){
            String[] buttons = {"Ok", "View Events"};    
            int returnValue = JOptionPane.showOptionDialog(null, "You have " + numReminderToday + " event(s) scheduled today!", " Urgent Reminder",JOptionPane.DEFAULT_OPTION, 0, transparentImg, buttons, null);

            if(returnValue == 1){
                for(int i = 0; i< remindersToday.size(); i++)
                {
                    Icon img = new ImageIcon("/Users/SuchenTan/Desktop/Digital Diary/Images/" + remindersToday.get(i).getImgDirectory());
                    JOptionPane.showMessageDialog(frame,remindersToday.get(i).toStringGUI(),remindersToday.get(i).toString(),1,img);

                }

            }
        }
    }

    ImageIcon loadingIcon;
    JLabel loadingLabel;
    public void loadingAnimation()
    {
        loadingIcon = new ImageIcon("/Users/SuchenTan/Desktop/Digital Diary/Images/7.gif");
        loadingLabel = new JLabel(loadingIcon, JLabel.CENTER);

        frame.add(loadingLabel);
        validateAndRepaint();
    }

    public void loadUI()
    {            
        GridBagConstraints c = new GridBagConstraints();
        panel1 = new JPanel(new GridBagLayout());
        panel2 = new JPanel(new GridBagLayout());
        panel1.setOpaque(false);
        panel2.setOpaque(false);

        addNotesUI();
        addContactsUI();
        addReminderUI();
        addListeners();

        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0,0,50,50);

        frame.add(panel1,c);
        c.gridx = 1;
        frame.add(panel2,c);

        validateAndRepaint();
    }

    public void validateAndRepaint(){
        frame.validate();
        frame.repaint();
    }

    public void addReminderUI()
    {
        remindersOptionsButton = new JButton("Events Options");
        remindersButton = new JButton("Upcoming Events");
        remindersButton.setPreferredSize(new Dimension(465,40));
        remindersOptionsButton.setPreferredSize(new Dimension(465,40));
        GridBagConstraints c = new GridBagConstraints();
        reminderScrollPane = new JScrollPane();        
        reminderList = new JList(reminderDatabase.toArray());

        reminderList.setVisibleRowCount(6);
        reminderList.setFont(font);
        reminderList.setBackground(lightBlueFocusLostTextFieldColor);
        reminderList.setFixedCellHeight(24);
        reminderList.setFixedCellWidth(442);
        DefaultListCellRenderer renderer =  (DefaultListCellRenderer)reminderList.getCellRenderer();  
        renderer.setHorizontalAlignment(JLabel.CENTER); 
        reminderScrollPane.setViewportView(reminderList);

        c.gridx = 0;
        c.gridy = 2;
        panel2.add(remindersButton,c);
        c.gridy = 3;
        panel2.add(reminderScrollPane,c);
        c.gridy = 4;
        panel2.add(remindersOptionsButton,c);

    }

    public void addNotesUI(){
        GridBagConstraints c = new GridBagConstraints();

        saveNotesButton = new JButton("Save Notes");
        notesTextArea = new JTextArea(9,34);
        notesTextArea.append(notesInformation);

        noteScrollPane = new JScrollPane(notesTextArea);
        saveNotesButton.setPreferredSize(new Dimension(465,40));
        notesTextArea.setBackground(lightBlueFocusLostTextFieldColor);
        notesTextArea.setFont(font);

        notesTextArea.setCaretPosition(0);
        c.gridx = 0;
        c.gridy = 0;
        panel2.add(saveNotesButton,c);
        c.gridy = 1;
        panel2.add(noteScrollPane,c);

    }

    public void addContactsUI(){
        contactsButton = new JButton("Contacts");
        contactsOptionsButton = new JButton("Contacts Options");
        contactsButton.setPreferredSize(new Dimension(203,40));
        contactsOptionsButton.setPreferredSize(new Dimension(203,40));
        contactsScrollPane = new JScrollPane();        
        contactsList = new JList(contactDatabase.toArray());

        contactsList.setVisibleRowCount(14);
        contactsList.setFont(font);
        contactsList.setBackground(lightBlueFocusLostTextFieldColor);
        contactsList.setFixedCellHeight(24);
        contactsList.setFixedCellWidth(180);
        DefaultListCellRenderer renderer =  (DefaultListCellRenderer)contactsList.getCellRenderer();  
        renderer.setHorizontalAlignment(JLabel.CENTER); 
        contactsScrollPane.setViewportView(contactsList);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        panel1.add(contactsButton,c);
        c.gridy = 1;
        panel1.add(contactsScrollPane,c);
        c.gridy = 2;
        panel1.add(contactsOptionsButton,c);        
    }

    public void addListeners(){
        contactsList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent listSelectionEvent) {
                    JList list = (JList) listSelectionEvent.getSource();
                    Object selectedObject = list.getSelectedValue();

                    if(selectedObject != null){
                        selectedContact = (Contact)selectedObject;
                        contactsButton.setText(selectedContact.getFirstName() + " " +selectedContact.getLastName());
                        contactImgDirectory = selectedContact.getImgDirectory();
                    }
                    else{      
                        contactsButton.setText("Contacts");
                        selectedContact = null;
                    }
                }
            });

        contactsButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(selectedContact == null){
                        JOptionPane.showMessageDialog(frame, "Please select a contact.","Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                        Icon img = new ImageIcon("/Users/SuchenTan/Desktop/Digital Diary/Images/" + contactImgDirectory);
                        JOptionPane.showMessageDialog(frame,selectedContact.toStringGUI(),selectedContact.toString(),1,img);
                    }
                }
            });  

        contactsOptionsButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String[] buttons = {"Edit Contacts","Sort Contacts", "Remove Contacts", "Add Contacts"};    
                    int returnValue = JOptionPane.showOptionDialog(null, "                                                   What would you like to do?", "Contacts Options",JOptionPane.DEFAULT_OPTION, 0, transparentImg, buttons, null);
                    contactOptions(returnValue);        
                }
            }); 

        saveNotesButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try{
                        notesInformation = notesTextArea.getText();
                        BufferedWriter output = new BufferedWriter(new FileWriter(NOTES_LIST_PATH, false));
                        output.write(notesInformation);
                        output.flush();
                        output.close();
                        JOptionPane.showMessageDialog(frame, "Notes saved successfully!");
                    }catch(IOException ee){
                        System.out.println("something went wrong");
                    }

                }
            }); 

        remindersButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(selectedReminder == null){
                        JOptionPane.showMessageDialog(frame, "Please select an event.","Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                        Icon img = new ImageIcon("/Users/SuchenTan/Desktop/Digital Diary/Images/" + reminderImgDirectory);
                        JOptionPane.showMessageDialog(frame,selectedReminder.toStringGUI(),selectedReminder.toString(),1,img);
                    }
                }
            });  

        remindersOptionsButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String[] buttons = {"Edit Reminders","Sort Reminders" ,"Remove Reminders","Add Reminders"};    
                    int returnValue = JOptionPane.showOptionDialog(null, "                                                        What would you like to do?", "Events Options",JOptionPane.DEFAULT_OPTION, 0, transparentImg, buttons, null);
                    reminderOptions(returnValue);  
                }
            });
        reminderList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent listSelectionEvent) {
                    JList list = (JList) listSelectionEvent.getSource();
                    Object selectedObject = list.getSelectedValue();

                    if(selectedObject != null){
                        selectedReminder = (Reminder)selectedObject;
                        remindersButton.setText(selectedReminder.getTitle());
                        reminderImgDirectory = selectedReminder.getImgDirectory();
                    }
                    else{      
                        remindersButton.setText("Upcoming Events");
                        selectedReminder = null;
                    }
                }
            });

    }
    /*
    private class MyCellRender extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<E> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    if(today.getDay() == reminderDatabase.get(i).getEndDate().getDay() && today.getYear() == reminderDatabase.get(i).getEndDate().getYear() 
    && today.getMonth() == reminderDatabase.get(i).getEndDate().getMonth()) {
    Color fg = value.toString().contains("COMPLETED") ? Color.green : Color.red;
    setForeground(fg);
    } else {
    setForeground(list.getForeground());
    }
    return this;
    }
    }
     */
    /*
    public Component getListCellRendererComponent(JList list) {
    Component c = super.getListCellRendererComponent();
    ListModel mode= list.getModel();

    for(int i=0; i< model.getSize(); i++){
    }

    for(int i
    }*/

    public void reminderOptions(int optionValue)
    {
        String targetDeleteReminder;
        Reminder newReminder;
        DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
        if(optionValue == 0){
            editReminder();
        }

        else if(optionValue == 1){
            sortReminders();
        }
        else if(optionValue == 2){

            targetDeleteReminder = JOptionPane.showInputDialog("What is the title of the event you want to delete?");
            if(targetDeleteReminder != null)
                deleteReminder(targetDeleteReminder);
        }
        else if(optionValue == 3){
            String title = JOptionPane.showInputDialog("What do you want to call your event?");
            String message = JOptionPane.showInputDialog("How do you want to describe your event?");
            String location = JOptionPane.showInputDialog("Where will your event be?");
            String endDateString = JOptionPane.showInputDialog("When will it take place?");
            String alarm = JOptionPane.showInputDialog("What do you want your alarm to say");
            Date endDate = null;
            Date startDate = new Date();

            try{
                endDate = df.parse(endDateString);

                newReminder = new Reminder(title,message,location,alarm,startDate,endDate,"temp");

                addReminder(newReminder,-1);
            }catch(Exception e)
            {
                JOptionPane.showMessageDialog(frame, "Event creation failed!","Error", JOptionPane.ERROR_MESSAGE); 
            }

        }
    }

    public void sortReminders()
    {
        String[] buttons =  {"Date (Newest-Oldest)","Date (Oldest-Newest)", "Title (A-Z)"};    
        int returnValue = JOptionPane.showOptionDialog(null, "                               What do you want to sort your contacts by?", "Sort Contacts",JOptionPane.DEFAULT_OPTION, 0, transparentImg, buttons, null);

        if(returnValue == 0){
            Collections.sort(reminderDatabase, new Comparator<Reminder>() {
                    public int compare(Reminder reminder1, Reminder reminder2){
                        Date dateReminder1 = reminder1.getEndDate();
                        Date dateReminder2 = reminder2.getEndDate();

                        if(dateReminder1.before(dateReminder2))
                            return 1;
                        else if(dateReminder2.before(dateReminder1))
                            return -1;
                        else return 0;
                    }
                });
        }
        else if(returnValue == 1){
            Collections.sort(reminderDatabase, new Comparator<Reminder>() {
                    public int compare(Reminder reminder1, Reminder reminder2){
                        Date dateReminder1 = reminder1.getEndDate();
                        Date dateReminder2 = reminder2.getEndDate();

                        if(dateReminder2.before(dateReminder1))
                            return 1;
                        else if(dateReminder1.before(dateReminder2))
                            return -1;
                        else return 0;
                    }
                });
        }
        else if(returnValue == 2){
            Collections.sort(reminderDatabase, new Comparator<Reminder>() {
                    public int compare(Reminder reminder1, Reminder reminder2){
                        String titleReminder1 = reminder1.getTitle();
                        String titleReminder2 = reminder2.getTitle();

                        if(titleReminder1.compareTo(titleReminder2) > 0)
                            return 1;
                        else if(titleReminder1.compareTo(titleReminder2) < 0)
                            return -1;
                        else return 0;
                    }
                });
        }

        reminderList.setListData(reminderDatabase.toArray());
        validateAndRepaint();
        updateTextFile("Reminder");
    }

    public void contactOptions(int optionValue)
    {
        String targetDeleteName;
        String firstName, lastName, email, age, gender, phoneNumber;
        Contact newContact = null;

        if(optionValue == 0){
            editContacts();            
        }
        else if(optionValue == 1){
            sortContacts();
        }
        else if(optionValue == 2){
            targetDeleteName = JOptionPane.showInputDialog("What is the name of the person you want to delete?");

            if(targetDeleteName != null)
                deleteContact(targetDeleteName);
        }
        else if(optionValue == 3){
            firstName = JOptionPane.showInputDialog("What is your first name?");
            lastName = JOptionPane.showInputDialog("What is you last name?");
            email = JOptionPane.showInputDialog("What is your E-mail address?");
            age = JOptionPane.showInputDialog("What is your age?");
            gender = JOptionPane.showInputDialog("Are you Male or Female?");
            phoneNumber = JOptionPane.showInputDialog("What is your phone number?");

            newContact = new Contact(firstName, lastName, email, age, gender, phoneNumber, "temp");
            addContact(newContact,-1);
        }

    } 

    public void sortContacts()
    {

        String[] buttons =  {"Gender (Male-Female)","Age (Low-High)","Last Name (A-Z)", "First Name (A-Z)"};    
        int returnValue = JOptionPane.showOptionDialog(null, "                                                   What do you want to sort your contacts by?", "Sort Contacts",JOptionPane.DEFAULT_OPTION, 0, transparentImg, buttons, null);
        if(returnValue == 3){
            Collections.sort(contactDatabase, new Comparator<Contact>() {
                    public int compare(Contact contact1, Contact contact2){
                        String firstContact1 = contact1.getFirstName();
                        String firstContact2 = contact2.getFirstName();

                        if(firstContact1.compareTo(firstContact2) > 0)
                            return 1;
                        else if(firstContact1.compareTo(firstContact2) < 0)
                            return -1;
                        else return 0;
                    }
                });
        }

        else if(returnValue == 2){
            Collections.sort(contactDatabase, new Comparator<Contact>() {
                    public int compare(Contact contact1, Contact contact2){
                        String lastContact1 = contact1.getLastName();
                        String lastContact2 = contact2.getLastName();

                        if(lastContact1.compareTo(lastContact2) > 0)
                            return 1;
                        else if(lastContact1.compareTo(lastContact2) < 0)
                            return -1;
                        else return 0;
                    }
                });
        }

        else if(returnValue == 1){
            Collections.sort(contactDatabase, new Comparator<Contact>() {
                    public int compare(Contact contact1, Contact contact2){
                        int ageContact1 = Integer.parseInt(contact1.getAge());
                        int ageContact2 = Integer.parseInt(contact2.getAge());

                        if(ageContact1 > ageContact2)
                            return 1;
                        else if(ageContact1 < ageContact2)
                            return -1;
                        else 
                            return 0;
                    }
                });

        }
        else if(returnValue == 0){
            Collections.sort(contactDatabase, new Comparator<Contact>() {
                    public int compare(Contact contact1, Contact contact2){
                        String genderContact1 = contact1.getGender();
                        String genderContact2 = contact2.getGender();

                        if(genderContact1.compareTo(genderContact2) < 0)
                            return 1;
                        else if(genderContact1.compareTo(genderContact2) > 0)
                            return -1;
                        else 
                            return 0;
                    }
                });

        }

        contactsList.setListData(contactDatabase.toArray());
        validateAndRepaint();
        updateTextFile("Contact");
    }

    public void editContacts(){
        String fullName = JOptionPane.showInputDialog("Who's profile do you want to edit?"), imgDirectory,
        firstName="", lastName="";
        boolean isFound = false, transitionToLast = false;
        int contactIndex=0;
        Icon img;
        Contact editedContact;

        if(fullName != null){
            for(int i = 0; i< contactDatabase.size();i++){
                if(fullName.equals(contactDatabase.get(i).getFirstName() + " " + contactDatabase.get(i).getLastName())){                
                    contactIndex = i;
                    isFound = true;
                    break;
                }
            }

            img = new ImageIcon("/Users/SuchenTan/Desktop/Digital Diary/Images/" + contactDatabase.get(contactIndex).getImgDirectory());
            imgDirectory = contactDatabase.get(contactIndex).getImgDirectory();
            if(isFound == false){
                JOptionPane.showMessageDialog(frame, "Invalid name","Error", JOptionPane.ERROR_MESSAGE); 
            }

            else{
                Panel namePanel = new Panel(), agePanel = new Panel(), genderPanel = new Panel(), emailPanel = new Panel(),
                phoneNumberPanel = new Panel();
                JTextField nameField = new JTextField(14), ageField = new JTextField(14), genderField = new JTextField(14), emailField = new JTextField(14),
                phoneNumberField= new JTextField(14);

                nameField.setText(contactDatabase.get(contactIndex).getFirstName() + " " +contactDatabase.get(contactIndex).getLastName());
                ageField.setText(contactDatabase.get(contactIndex).getAge());
                genderField.setText(contactDatabase.get(contactIndex).getGender());
                emailField.setText(contactDatabase.get(contactIndex).getEmail());
                phoneNumberField.setText(contactDatabase.get(contactIndex).getPhoneNumber());

                namePanel.add(new JLabel("Name: "));
                namePanel.add(nameField);
                agePanel.add(new JLabel("Age: "));
                agePanel.add(ageField);
                genderPanel.add(new JLabel("Gender: "));
                genderPanel.add(genderField);
                emailPanel.add(new JLabel("Email: "));
                emailPanel.add(emailField);
                phoneNumberPanel.add(new JLabel("Phone Number: "));
                phoneNumberPanel.add(phoneNumberField);

                final Object [] inputs = new Object[] {namePanel,agePanel,genderPanel,emailPanel,phoneNumberPanel};
                JOptionPane.showMessageDialog(null, inputs,contactDatabase.get(contactIndex).getFirstName() + " " +contactDatabase.get(contactIndex).getLastName() , JOptionPane.PLAIN_MESSAGE, img);

                for(int i = 0; i < nameField.getText().length(); i++){
                    if(("" + nameField.getText().charAt(i)).equals(" ")){
                        transitionToLast = true;
                    }
                    else if(transitionToLast == false && !("" + nameField.getText().charAt(i)).equals(" ")){
                        firstName +=nameField.getText().charAt(i);
                    }
                    else if(transitionToLast == true && !("" + nameField.getText().charAt(i)).equals(" ")){
                        lastName +=nameField.getText().charAt(i);
                    }
                }

                contactDatabase.remove(contactIndex);
                editedContact = new Contact(firstName, lastName, emailField.getText(), ageField.getText(),
                    genderField.getText(),phoneNumberField.getText(), imgDirectory);

                addContact(editedContact,contactIndex);

            }
        }
    }

    public void editReminder(){
        String title = JOptionPane.showInputDialog("Which reminder do you want to edit?"), imgDirectory;
        boolean isFound = false, wrongDateFormat = false;
        int reminderIndex=0;
        Icon img;
        Reminder editedReminder = null;
        Date startDate;

        DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);

        if(title != null){
            for(int i = 0; i< reminderDatabase.size();i++){
                if(title.equals(reminderDatabase.get(i).getTitle())){                
                    reminderIndex = i;
                    isFound = true;
                    break;
                }
            }

            img = new ImageIcon("/Users/SuchenTan/Desktop/Digital Diary/Images/" + reminderDatabase.get(reminderIndex).getImgDirectory());
            imgDirectory = reminderDatabase.get(reminderIndex).getImgDirectory();
            startDate = reminderDatabase.get(reminderIndex).getStartDate();
            if(isFound == false){
                JOptionPane.showMessageDialog(frame, "Invaid event!","Error", JOptionPane.ERROR_MESSAGE); 
            }

            else{
                Panel alarmMessagePanel = new Panel(), titlePanel = new Panel(), descriptionPanel = new Panel(), dateEndPanel = new Panel(), 
                locationPanel = new Panel(), dateCreatedPanel = new Panel();
                JTextField alarmMessageField = new JTextField(14),titleField = new JTextField(14), descriptionField = new JTextField(14), locationField = new JTextField(14), 
                dateEndField = new JTextField(14);

                titleField.setText(reminderDatabase.get(reminderIndex).getTitle());
                descriptionField.setText(reminderDatabase.get(reminderIndex).getMessage());
                dateEndField.setText(reminderDatabase.get(reminderIndex).getEndDate().toString());
                alarmMessageField.setText(reminderDatabase.get(reminderIndex).getAlarmMessage());
                locationField.setText(reminderDatabase.get(reminderIndex).getLocation());

                titlePanel.add(new JLabel("Title: "));
                titlePanel.add(titleField);
                descriptionPanel.add(new JLabel("Description: "));
                descriptionPanel.add(descriptionField);
                dateEndPanel.add(new JLabel("When: "));
                dateEndPanel.add(dateEndField);
                locationPanel.add(new JLabel("Where: "));
                locationPanel.add(locationField);
                alarmMessagePanel.add(new JLabel("Alarm Message: "));
                alarmMessagePanel.add(alarmMessageField);

                final Object [] inputs = new Object[] {titlePanel, descriptionPanel, locationPanel, dateEndPanel, alarmMessagePanel};
                JOptionPane.showMessageDialog(null, inputs, reminderDatabase.get(reminderIndex).getTitle(), JOptionPane.PLAIN_MESSAGE, img);

                try{
                    Date test = df.parse(dateEndField.getText());
                }catch(Exception e){
                    wrongDateFormat = true;
                }

                if(wrongDateFormat == false){
                    reminderDatabase.remove(reminderIndex);

                    try{
                        editedReminder = new Reminder(titleField.getText(), descriptionField.getText(), locationField.getText(), alarmMessageField.getText(), startDate,            
                            df.parse(dateEndField.getText()), imgDirectory);
                    }catch(Exception e){
                        System.out.println("Incorrect date format!!!!");
                    }
                    addReminder(editedReminder,reminderIndex);
                }
                else if(wrongDateFormat == true)
                {
                    JOptionPane.showMessageDialog(frame, "Invaid date format!","Error", JOptionPane.ERROR_MESSAGE);            
                }

            }
        }
    }

    public void addReminder(Reminder reminder, int index)
    {
        if(index < 0){
            reminderDatabase.add(reminder);
        }
        else if(index >= 0){
            reminderDatabase.add(index,reminder);
        }
        reminderList.setListData(reminderDatabase.toArray());
        validateAndRepaint();

        updateTextFile("Reminder");
    }

    public void addContact(Contact newContact, int index)
    {
        if(index < 0){
            contactDatabase.add(newContact);
        }
        else if(index >= 0){
            contactDatabase.add(index,newContact);
        }
        contactsList.setListData(contactDatabase.toArray());
        validateAndRepaint();

        updateTextFile("Contact");
    }

    public void deleteContact(String fullName)
    {        
        boolean isFound = false;
        for(int i = 0; i < contactDatabase.size();i++){
            if(fullName.equals(contactDatabase.get(i).getFirstName() + " " + contactDatabase.get(i).getLastName())){                
                contactDatabase.remove(i);
                isFound = true;
                break;
            }
        }

        if(isFound == false){
            JOptionPane.showMessageDialog(frame, "Invalid name!!","Error", JOptionPane.ERROR_MESSAGE); 
        }

        else{
            contactsList.setListData(contactDatabase.toArray());
            validateAndRepaint();
        }

        updateTextFile("Contact");

    }

    public void updateTextFile(String type) //"Contact" or "Reminder"
    {
        File targetFileDirectory = null;
        String targetFileTitle = null;

        if(type.equalsIgnoreCase("Contact")){
            targetFileDirectory = CONTACT_LIST_PATH;
            targetFileTitle = CONTACT_LIST_TITLE;
        }
        else if(type.equalsIgnoreCase("Reminder")){
            targetFileDirectory = REMINDER_LIST_PATH;
            targetFileTitle = REMINDER_LIST_TITLE; 
        }

        try{
            out = new PrintWriter(targetFileDirectory);             
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        finally{
            if(out != null){
                out.flush();
                out.close();
            }
        }

        try{
            textOutputStream = new BufferedWriter(new FileWriter(targetFileDirectory,true));
            try{
                textOutputStream.write(targetFileTitle);

                if(type.equalsIgnoreCase("Contact")){
                    for(int i = 0; i < contactDatabase.size(); i++){
                        textOutputStream.newLine();
                        textOutputStream.write(contactDatabase.get(i).getFirstName() + "|" + contactDatabase.get(i).getLastName() + "|" +
                            contactDatabase.get(i).getEmail() + "|" + contactDatabase.get(i).getAge() + "|" + contactDatabase.get(i).getGender() + "|" +
                            contactDatabase.get(i).getPhoneNumber() + "|" + contactDatabase.get(i).getImgDirectory());

                    }
                }
                else if(type.equalsIgnoreCase("Reminder")){
                    for(int i = 0; i < reminderDatabase.size(); i++){
                        textOutputStream.newLine();
                        textOutputStream.write(reminderDatabase.get(i).getTitle() + "|" + reminderDatabase.get(i).getMessage()
                            + "|" + reminderDatabase.get(i).getLocation() + "|" +reminderDatabase.get(i).getAlarmMessage()+"|"+ reminderDatabase.get(i).getStartDate()
                            + "|" + reminderDatabase.get(i).getEndDate() + "|" + reminderDatabase.get(i).getImgDirectory());

                    }
                }

                textOutputStream.flush();
                textOutputStream.close();
            }catch(IOException e)
            {
                System.out.println("Something went wrong with writn o the file");
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("Something went wrong with mmaking the file");
        }
    }

    public void deleteReminder(String reminder)
    {        
        boolean isFound = false;

        for(int i = 0; i < reminderDatabase.size();i++){
            if(reminder.equals(reminderDatabase.get(i).getTitle())){                
                reminderDatabase.remove(i);
                isFound = true;
                break;
            }
        }

        if(isFound == false){
            JOptionPane.showMessageDialog(frame, "Invalid event!","Error", JOptionPane.ERROR_MESSAGE); 
        }

        else{
            reminderList.setListData(reminderDatabase.toArray());
            updateTextFile("Reminder");
            validateAndRepaint();
        }

    }

    public void updateContactDatabase(File file)
    {
        String line;
        String[] updateContactsElements;
        Contact newContact;
        boolean error = false;

        contactDatabase.clear();

        try {
            textInputStream = new BufferedReader(new FileReader(file));

            while ((line = textInputStream.readLine()) != null) {
                try {
                    if (!line.equals(CONTACT_LIST_TITLE)) {
                        updateContactsElements = line.split("\\|");

                        newContact = new Contact(updateContactsElements[0],updateContactsElements[1],updateContactsElements[2],
                            updateContactsElements[3],updateContactsElements[4],updateContactsElements[5], updateContactsElements[6]);
                        contactDatabase.add(newContact);
                    }
                } catch (Exception e) {
                    System.out.println("Something went wrong with reading the file. Missing args?");
                    e.printStackTrace();
                    error = true;
                }
            }

        } catch (IOException e) {
            System.out.println("Something went wrong with creating the textInputStream.");
            e.printStackTrace();
            error = true;
        }

        if (error == false) {
            // System.out.println("Contacts Database Successfully Loaded!");
        }
    }

    public void updateReminderDatabase(File file)
    {
        String line;
        String[] updateReminderElements;
        Reminder newReminder;
        boolean error = false;

        DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);

        reminderDatabase.clear();

        try {
            textInputStream = new BufferedReader(new FileReader(file));

            while ((line = textInputStream.readLine()) != null) {
                try {
                    if (!line.equals(REMINDER_LIST_TITLE)) {
                        updateReminderElements = line.split("\\|");

                        newReminder = new Reminder(updateReminderElements[0],updateReminderElements[1],updateReminderElements[2],
                            updateReminderElements[3],df.parse(updateReminderElements[4]), df.parse(updateReminderElements[5]),updateReminderElements[6]);
                        reminderDatabase.add(newReminder);
                    }
                } catch (Exception e) {
                    System.out.println("Something went wrong with reading the file. Missing args?gg");
                    e.printStackTrace();
                    error = true;
                }
            }

        } catch (IOException e) {
            System.out.println("Something went wrong with creating the textInputStream.");
            e.printStackTrace();
            error = true;
        }

        if (error == false) {
            // System.out.println("Contacts Database Successfully Loaded!");
        }
    }

    public void updateUserDatabase()
    {
        String line;
        String[] updateUserElements;
        User newUser;
        boolean error = false;

        userDatabase.clear();
        try {
            textInputStream = new BufferedReader(new FileReader(USER_LIST_PATH));

            while ((line = textInputStream.readLine()) != null) {
                try {
                    if (!line.equals(USER_LIST_TITLE)) {
                        updateUserElements = line.split("\\|");

                        newUser = new User(updateUserElements[0],updateUserElements[1],updateUserElements[2],
                            updateUserElements[3],updateUserElements[4],updateUserElements[5],updateUserElements[6]
                        ,updateUserElements[7], updateUserElements[8]);
                        userDatabase.add(newUser);
                    }
                } catch (Exception e) {
                    System.out.println("Something went wrong with reading the file. Missing args?");
                    e.printStackTrace();
                    error = true;
                }
            }

        } catch (IOException e) {
            System.out.println("Something went wrong with creating the textInputStream.");
            e.printStackTrace();
            error = true;
        }

        if (error == false) {
            // System.out.println("User Database Successfully Loaded!");
        }
    }

    public void updateNotesDatabase(File file)
    {
        String line;
        notesInformation = "";
        try {
            textInputStream = new BufferedReader(new FileReader(file));

            while ((line = textInputStream.readLine()) != null) {
                try {
                    notesInformation += line + "\n";
                } catch (Exception e) {
                    System.out.println("Something went wrong with reading the file. Missing args?");
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            System.out.println("Something went wrong with creating the textInputStream.");
            e.printStackTrace();
        }
    }
}