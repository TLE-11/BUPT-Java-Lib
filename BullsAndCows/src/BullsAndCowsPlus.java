import java.util.Scanner;

// 2025.10.11

public class BullsAndCowsPlus {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 步骤1：指定数字长度和是否允许重复
        System.out.print("请输入数字长度：");
        int length = scanner.nextInt();
        System.out.print("是否允许数字重复（true/false）：");
        boolean allowRepeat = scanner.nextBoolean();

        // 生成目标数字
        String target = generateTarget(length, allowRepeat);
//        String target = "1233";
        System.out.println("目标数字为:" + target);
        int count = 0; // 记录猜测次数

        System.out.println("猜数字游戏开始！请猜一个" + length + "位数字" + (allowRepeat ? "（可重复）" : "（无重复）") + "：");
        while (true) {
            count++;
            System.out.print("请输入你第" + count + "次猜的数字：");
            String guess = scanner.next();

            // 合法性检查：长度正确且全为数字
            if (guess.length() != length || !guess.matches("[0-9]+")) {
                System.out.println("输入不合法，请输入" + length + "位数字！");
                continue;
            }

            int[] result = calculateAB(target, guess);
            System.out.println("猜测结果：" + result[0] + "A" + result[1] + "B");

            if (target.equals(guess)) {
                System.out.println("恭喜你猜对了！你一共猜了" + count + "次。");
                break;
            }
        }
    }

    // 生成目标数字（支持自定义长度和重复规则）
    private static String generateTarget(int length, boolean allowRepeat) {
        StringBuilder sb = new StringBuilder();
        if (allowRepeat) {
            // 允许重复：直接生成随机数字
            for (int i = 0; i < length; i++) {
                sb.append((int) (Math.random() * 10));
            }
        } else {
            // 不允许重复：确保数字不重复
            boolean[] used = new boolean[10];
            for (int i = 0; i < length; i++) {
                int num;
                do {
                    num = (int) (Math.random() * 10);
                } while (used[num]);
                used[num] = true;
                sb.append(num);
            }
        }
        return sb.toString();
    }

    // 计算A（数字且位置正确）和B（数字正确但位置错误）
    private static int[] calculateAB(String target, String guess) {
        int a = 0;
        int b = 0;
        int length = target.length();
        boolean[] targetUsed = new boolean[length]; // 标记目标数字的字符是否已用于计算A
        boolean[] guessUsed = new boolean[length];  // 标记猜测数字的字符是否已用于计算A

        // 先计算A（位置和数字都正确）
        for (int i = 0; i < length; i++) {
            if (target.charAt(i) == guess.charAt(i)) {
                a++;
                targetUsed[i] = true;
                guessUsed[i] = true;
            }
        }

        // 再计算B（数字正确但位置错误）
        for (int i = 0; i < length; i++) {
            if (!guessUsed[i]) { // 该位置的猜测字符未用于A的计算
                for (int j = 0; j < length; j++) {
                    if (!targetUsed[j] && target.charAt(j) == guess.charAt(i)) {
                        b++;
                        targetUsed[j] = true;
                        break;
                    }
                }
            }
        }

        return new int[]{a, b};
    }
}
