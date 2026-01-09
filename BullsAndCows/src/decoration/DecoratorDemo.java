package decoration;

public class DecoratorDemo {
    public static void main(String[] args) {
        Greeter greeter = new BasicGreeter();
        String motd = greeter.getMessageOfTheDay();
        System.out.println(motd);

        Greeter strangeGreeter = new StrangerGreeter(greeter);
        String newMotd =  strangeGreeter.getMessageOfTheDay();
        System.out.println(newMotd);

        Greeter strange2Greeter = new StrangerGreeter(new StrangerGreeter(greeter));
        String newestMotd = strange2Greeter.getMessageOfTheDay();
        System.out.println(newestMotd);

        Greeter greeters = new SadGreeter(
                new HappyGreeter(
                        new StrangerGreeter(
                                new SadGreeter(
                                        new HappyGreeter(greeter)))));
        System.out.println(greeters.getMessageOfTheDay());
    }
}
