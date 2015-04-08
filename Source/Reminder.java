import java.util.*;

public class Reminder
{
    String title, message, location, alarmMessage, imgDirectory;
    Date startDate, endDate;
    public Reminder(String title, String message, String location, String alarmMessage, Date startDate, Date endDate, String imgDirectory){
        this.title = title;
        this.message = message;
        this.location = location;
        this.alarmMessage = alarmMessage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imgDirectory = imgDirectory;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public String toString()
    {
        return title;
    }
    
    public String getImgDirectory()
    {
        return imgDirectory;
    }
    public String toStringGUI()
    {
        String result;
        
        result = "Description: " + message;
        result += "\nWhen: " + endDate.toGMTString();
        result += "\nWhere: " + location;
        result += "\nDate created: " + startDate.toGMTString();
        
        return result;
    }
    
}