package Animal;

/**
 * @description 动物类
 * @author TLE-11
 * @create 2025/11/18
 **/
public abstract class Animal {
    protected String name;
    protected String type;

    // 无参构造方法
    public  Animal() {}

    // 带参构造方法
    public Animal(String name, String type) {
        this.name = name;
        this.type = type;
    }

    // 抽象方法
    public abstract void eat();
    public abstract void info();

    // Getter方法
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
