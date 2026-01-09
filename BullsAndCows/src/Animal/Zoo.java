package Animal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * @description 动物园类
 * @author TLE-11
 * @create 2025/11/18
 **/
public class Zoo {
    private ArrayList<Animal> animals;

    public Zoo() {
        this.animals = new ArrayList<>();
    }

    // 添加动物
    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    // 删除动物
    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }

    // 获取动物园状态
    public void getZooStatus() {
        int tigerCount = 0;
        int cockCount = 0;
        int wormCount = 0;
        int barCount = 0;

        for (Animal animal : animals) {
            if (animal instanceof Tiger) {
                tigerCount++;
            } else if (animal instanceof Cock) {
                cockCount++;
            } else if (animal instanceof Worm) {
                wormCount++;
            } else if (animal instanceof Bar) {
                barCount++;
            }
        }

        System.out.println("=== Zoo Status ===");
        System.out.println("Tigers: " + tigerCount);
        System.out.println("Cocks: " + cockCount);
        System.out.println("Worms: " + wormCount);
        System.out.println("Bars: " + barCount);
        System.out.println("Total: " + animals.size());
        System.out.println("==================");
    }

    // 创建100只动物（1:1:1:1概率）
    public void createAnimals() {
        Random random = new Random();

        for (int i = 1; i <= 100; i++) {
            int animalType = random.nextInt(4); // 0-3

            switch (animalType) {
                case 0:
                    animals.add(new Tiger("Tiger_" + i));
                    break;
                case 1:
                    animals.add(new Cock("Cock_" + i));
                    break;
                case 2:
                    animals.add(new Worm("Worm_" + i));
                    break;
                case 3:
                    animals.add(new Bar("Bar_" + i));
                    break;
            }
        }
    }

    // 删除所有老虎
    public void removeAllTigers() {
        Iterator<Animal> iterator = animals.iterator();
        while (iterator.hasNext()) {
            Animal animal = iterator.next();
            if (animal instanceof Tiger) {
                iterator.remove();
            }
        }
    }

    // 清空所有动物
    public void clearAllAnimals() {
        animals.clear();
    }

    // 动物战斗
    public void fight(Animal a1, Animal a2) {
        String result = determineWinner(a1, a2);
    }

    // 判断胜负
    private String determineWinner(Animal a1, Animal a2) {
        String type1 = a1.getType();
        String type2 = a2.getType();

        // 老虎吃鸡
        if (("Tiger".equals(type1) && "Cock".equals(type2)) ||
                ("Cock".equals(type1) && "Tiger".equals(type2))) {
            return "Tiger".equals(type1) ? "Tiger wins" : "Cock wins";
        }
        // 鸡吃虫
        else if (("Cock".equals(type1) && "Worm".equals(type2)) ||
                ("Worm".equals(type1) && "Cock".equals(type2))) {
            return "Cock".equals(type1) ? "Cock wins" : "Worm wins";
        }
        // 虫蛀棒子
        else if (("Worm".equals(type1) && "Bar".equals(type2)) ||
                ("Bar".equals(type1) && "Worm".equals(type2))) {
            return "Worm".equals(type1) ? "Worm wins" : "Bar wins";
        }
        // 棒子打老虎
        else if (("Bar".equals(type1) && "Tiger".equals(type2)) ||
                ("Tiger".equals(type1) && "Bar".equals(type2))) {
            return "Bar".equals(type1) ? "Bar wins" : "Tiger wins";
        }
        // 其他为平局
        else {
            return "Draw";
        }
    }

    // 游戏模式：动物大乱斗
    public void gameMode() {
        int[] winCount = new int[4]; // 0:Tiger, 1:Cock, 2:Worm, 3:Bar

        for (int round = 1; round <= 10; round++) {

            // 重新创建100只动物
            clearAllAnimals();
            createAnimals();

            // 复制列表用于战斗
            ArrayList<Animal> battleQueue = new ArrayList<>(animals);

            while (battleQueue.size() > 1) {
                // 取出前两只动物
                Animal a1 = battleQueue.removeFirst();
                Animal a2 = battleQueue.removeFirst();

                // 战斗
                fight(a1, a2);
                String result = determineWinner(a1, a2);

                // 处理战斗结果
                if (result.contains("Tiger wins")) {
                    battleQueue.add(a1 instanceof Tiger ? a1 : a2);
                } else if (result.contains("Cock wins")) {
                    battleQueue.add(a1 instanceof Cock ? a1 : a2);
                } else if (result.contains("Worm wins")) {
                    battleQueue.add(a1 instanceof Worm ? a1 : a2);
                } else if (result.contains("Bar wins")) {
                    battleQueue.add(a1 instanceof Bar ? a1 : a2);
                }
                // 平局则都不加入（双双移出）
            }

            // 记录获胜者
            if (!battleQueue.isEmpty()) {
                Animal winner = battleQueue.getFirst();
                if (winner instanceof Tiger) {
                    winCount[0] ++;
                } else if (winner instanceof Cock) {
                    winCount[1] ++;
                } else if (winner instanceof Worm) {
                    winCount[2] ++;
                } else if (winner instanceof Bar) {
                    winCount[3] ++;
                }
            }
        }

        // 输出最终统计
        System.out.println("\n=== Final Statistics ===");
        System.out.println("Tiger wins: " + winCount[0]);
        System.out.println("Cock wins: " + winCount[1]);
        System.out.println("Worm wins: " + winCount[2]);
        System.out.println("Bar wins: " + winCount[3]);
    }

    // 获取动物列表（用于测试）
    public ArrayList<Animal> getAnimals() {
        return animals;
    }
}
