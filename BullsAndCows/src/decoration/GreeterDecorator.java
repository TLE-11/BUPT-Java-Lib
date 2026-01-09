package decoration;

public abstract class GreeterDecorator implements Greeter{
    protected Greeter greeter;
    
    public GreeterDecorator(Greeter greeter){
        this.greeter = greeter;
    }

    @Override
    public String getMessageOfTheDay(){
        return greeter.getMessageOfTheDay();
    }
}
