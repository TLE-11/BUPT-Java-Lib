package decoration;

public class StrangerGreeter extends GreeterDecorator {

    public StrangerGreeter(Greeter greeter){
        super(greeter);
    } 

    @Override
    public String getMessageOfTheDay(){
        return "Strange: " + super.getMessageOfTheDay();
    }

}
