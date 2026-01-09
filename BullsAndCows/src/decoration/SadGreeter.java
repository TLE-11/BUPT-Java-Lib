package decoration;

public class SadGreeter extends GreeterDecorator {

    public SadGreeter (Greeter greeter){
        super(greeter);
    } 

    @Override
    public String getMessageOfTheDay(){
        return "Sad: " + super.getMessageOfTheDay();
    }

}
