package decoration;

public class BasicGreeter implements Greeter{
    @Override
    public String getMessageOfTheDay(){
        return "Basic: Welcome to my server!";
    }
}