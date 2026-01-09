package Animal;

/**
 * @description Tiger类
 * @author TLE-11
 * @create 2025/11/18
 **/
public class Tiger extends Animal {
    public Tiger(String name) {
        super(name, "Tiger");
    }

    @Override
    public void eat() {
        System.out.println("Tiger " + name + " is eating meat");
    }

    @Override
    public void info() {
        System.out.println("This is a Tiger named " + name);
    }
}
