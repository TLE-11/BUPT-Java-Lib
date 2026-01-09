package Animal;

/**
 * @description Worm类
 * @author TLE-11
 * @create 2025/11/18
 **/
public class Worm extends Animal {
    public Worm(String name) {
        super(name, "Worm");
    }

    @Override
    public void eat() {
        System.out.println("Worm " + name + " is eating soil");
    }

    @Override
    public void info() {
        System.out.println("This is a Worm named " + name);
    }
}
