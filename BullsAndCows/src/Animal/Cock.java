package Animal;

/**
 * @description Cock类
 * @author TLE-11
 * @create 2025/11/18
 **/
public class Cock extends Animal {

    // 无参构造方法
    public Cock() {}

    // 带参构造方法
    public Cock(String name) {
        super(name, "Cock");
    }

    @Override
    public void eat() {
        System.out.println("Cock " + name + " is eating grains");
    }

    @Override
    public void info() {
        System.out.println("This is a Cock named " + name);
    }
}
