public class Contact
{
    String firstName, lastName, email, age, gender, phoneNumber, imgDirectory;
    public Contact(String firstName, String lastName, String email, String age, String gender, String phoneNumber,String imgDirectory)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.imgDirectory = imgDirectory;
    }
    public String getFirstName()
    {
        return firstName;
    }
    public String getLastName()
    {
        return lastName;
    }
    public String getEmail(){
        return email;
    }
    public String getAge()
    {
        return age;
    }
    public String getPhoneNumber()
    {
        return phoneNumber;
    }
    public String getGender()
    {
        return gender;
    }
    public String getImgDirectory()
    {
        return imgDirectory;
    }
    public void setFirstName(String newFirstName)
    {
        firstName = newFirstName;
    }
    public void setLastName(String newLastName)
    {
        lastName = newLastName;
    }
    public void setEmail(String newEmail)
    {
        email = newEmail;
    }
    public void setGender(String newGender)
    {
        gender = newGender;
    }
    public void setAge(String newAge)
    {
        age = newAge;
    }
    
        
    public String toStringGUI()
    {
        String result;
        
        result = "Name: " + firstName + " " + lastName;
        result += "\nAge: " + age;
        result += "\nGender: " + gender;
        result += "\nE-mail: " + email;
        result += "\nPhone Number: " + phoneNumber;
        
        return result;
    }
    
    public String toString()
    {
        return firstName + " " + lastName;
    }
}