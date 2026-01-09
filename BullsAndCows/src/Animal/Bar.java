package Animal;

/**
 * @description Bar类
 * @author TLE-11
 * @create 2025/11/18
 **/
public class Bar extends Animal {

    public Bar() {}

    public Bar(String name) {
        super(name, "Bar");
    }

    @Override
    public void eat() {
        System.out.println("Bar " + name + " doesn't eat anything");
    }

    @Override
    public void info() {
        System.out.println("This is a Bar named " + name);
    }
}
