package Account;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @description 类异常练习
 * @author TLE-11
 * @create 2025/12/2
 **/
public class ExceptionPractice {
    public static void main(String[] args) {

        // 1. ArrayIndexOutOfBoundsException
        try {
            int[] arr = new int[2];
            // 访问超出长度的索引
            int x = arr[5];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("捕获到: ArrayIndexOutOfBoundsException");
            System.out.println("   信息: " + e.getMessage());
        }

        // 2. ArithmeticException
        try {
            // 除以 0
            int result = 10 / 0;
        } catch (ArithmeticException e) {
            System.out.println("捕获到: ArithmeticException");
            System.out.println("   信息: " + e.getMessage());
        }

        // 3. NullPointerException
        try {
            String s = null;
            // 对空对象调用方法
            s.length();
        } catch (NullPointerException e) {
            System.out.println("捕获到: NullPointerException");
            System.out.println("   信息: " + e.getMessage());
        }

        // 4. NumberFormatException
        try {
            // 将非数字字符串转换为整数
            Integer.parseInt("abc");
        } catch (NumberFormatException e) {
            System.out.println("捕获到: NumberFormatException");
            System.out.println("   信息: " + e.getMessage());
        }

        // 5. InputMismatchException
        try {
            Scanner sc = new Scanner("1.1");
            sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("捕获到: InputMismatchException");
            System.out.println("   信息: " + e.getMessage());
        }
    }
}