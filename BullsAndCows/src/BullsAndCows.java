import java.util.Scanner;

//2025.9.23

public class BullsAndCows {
    //生成随机的数字
    public static String generateSecretNumber(int length) {
        //生成可变长字符串
        StringBuilder sb = new StringBuilder();
        boolean[] used = new boolean[10];
        for (int i = 0; i < length; ++ i) {
            int num;
            do {
                num = (int)(Math.random() * 10);
            } while (used[num]);
            used[num] = true;
            sb.append(num);
        }
        return sb.toString();
    }

    //判断输入是否正确，并返回对应的反馈
    public static String calculateAB(String secret, String guess,int length) {
        int a = 0,b = 0;
        for (int i = 0; i < length; ++ i) {
            if (secret.charAt(i) == guess.charAt(i)) {
                ++ a;
            } else if (!secret.contains(String.valueOf(guess.charAt(i)))) {
                ++ b;
            }
        }
        return a + "A" + b + "B";
    }

    public static void main(String[] args) {
        System.out.println("请输入数字的长度(长度小于等于10，大于0):");
        Scanner scanner = new Scanner(System.in);
        int length = scanner.nextInt();
        //如果长度不符合要求就一直输入
        while (length > 10 || length <= 0) {
            System.out.println("输入错误，请重新输入!长度必须小于等于10，大于0！");
            length = scanner.nextInt();
        }

        //生成一个随机数
        String secret = generateSecretNumber(length);
        System.out.println("答案是：" + secret);

        //记录输入的次数
        int count = 0;

        //开始猜数字
        while (true) {
            count ++;
            System.out.println("第" + count + "次猜测");
            System.out.println("请输入你猜的数字:");
            String guess = scanner.next();

            //先判断数字长度是否符合规定
            if (guess.length() != length) {
                System.out.println("请输入" + length + "位数字");
                continue;
            }

            //判断输入是否有重复数字的出现
            boolean hasRepeat = false;
            for (int i = 0; i < length; ++ i) {
                if (guess.indexOf(guess.charAt(i)) != guess.lastIndexOf(guess.charAt(i))) {
                    hasRepeat = true;
                    break;
                }
            }
            if (hasRepeat) {
                System.out.println("请输入不重复的数字");
                continue;
            }

            //判断输入是否正确，并给出对应反馈
            String result = calculateAB(secret, guess, length);
            System.out.println(result);

            if (result.equals(length + "A0B")) {
                System.out.println("恭喜你猜对了！");
                break;
            } else if (!result.equals(length + "A0B")) {
                System.out.println("猜错啦，再接再厉！");
            }
        }
    }
}