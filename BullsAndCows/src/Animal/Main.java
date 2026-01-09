package Animal;

/**
 * @description Main类
 * @author TLE-11
 * @create 2025/11/18
 **/
public class Main {
    public static void main(String[] args) {
        Zoo zoo = new Zoo();

        // 创建100只动物
        zoo.createAnimals();
        zoo.getZooStatus();

        // 删除所有老虎
        System.out.println("\n删除所有老虎后：");
        zoo.removeAllTigers();
        zoo.getZooStatus();

        // 清空所有动物
        System.out.println("\n清空所有动物后：");
        zoo.clearAllAnimals();
        zoo.getZooStatus();

        // 游戏模式：10轮大乱斗
        zoo.gameMode();

        // 测试多态
        testPolymorphism();
    }

    public static void testPolymorphism() {
        Animal tiger = new Tiger("Test_Tiger");
        Animal cock = new Cock("Test_Cock");

        System.out.println("多态测试：");
        tiger.eat();
        tiger.info();
        cock.eat();
        cock.info();
    }
}
