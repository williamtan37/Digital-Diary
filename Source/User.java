public class User
{
    String firstName, lastName, age, gender, email, password, contactListLocation, reminderListLocation, notesListLocation;
    public User(String firstName, String lastName,String age, String gender, String email, String password, String contactListLocation,
    String reminderListLocation, String notesListLocation)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.contactListLocation = contactListLocation;
        this.reminderListLocation = reminderListLocation;
        this.notesListLocation = notesListLocation;
    }
    
    public String getFirstName(){
        return firstName;
    }
    
    public String getLastName(){
        return lastName;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public String getContactListLocation()
    {
        return contactListLocation;
    }
    
     public String getReminderListLocation()
    {
        return reminderListLocation;
    }
    
    public String getNotesListLocation()
    {
        return notesListLocation;
    }
 }
        