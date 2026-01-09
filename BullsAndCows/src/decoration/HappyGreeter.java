package decoration;

public class HappyGreeter extends GreeterDecorator {

    public HappyGreeter (Greeter greeter){
        super(greeter);
    } 

    @Override
    public String getMessageOfTheDay(){
        return "Happy: " + super.getMessageOfTheDay();
    }

}
