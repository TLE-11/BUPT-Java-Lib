/**
 * 检测信用卡号的有效性
 * Hans Luhn(IBM), 1954
 * Luhn检测或Mod 10检测
 * @author Young@BUPT
 * @since 20230319
 */

import java.util.Scanner;

/* 需要测试的信用卡号码
4388576018402626
4388576018410707
4005550000000019
5105105105105100
5555555555554444
4222222222222
4111111111111111
4012888888881881
378282246310005
371449635398431
378734493671000
38520000023237
30569309025904
6011111111111117
6011000990139424
3530111333300000
3566002020360505
340000000000000
30200000000000
5432154321543215
622456000000000
6767123456789012

使用重定向一次性检测所有卡号(打开一个终端窗口)
java LuhnCheck < card.txt
card.txt 文件内容即为上面的卡号，每行一个
*/

public class LuhnCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;
        long number;
        // 4388576018402626
        // 4388576018410707
        // System.out.print("Enter a credit number as a long integer: ");
        while (scanner.hasNext()) { // Ctrl+D退出
            input = scanner.nextLine();
            if (!isCardFormatValid(input)) {
                System.out.println();
                System.out.println("input card error: length or digit");
                System.exit(1);
            }
            number = Long.parseLong(input);
            // 检测信用卡是否有效
            // 输出检测结果
            if (isValid(number) ) {
                System.out.println(number + " is valid.");
            } else {
                System.out.println(number + " is invalid.");
            }

            // System.out.print("Enter a credit number as a long integer: ");
        }
        scanner.close();
    }

    /**
     * 检测输入的合法性，长度和内容
     * 信用卡号长度
     *  Master 16
     *  VISA 13、16
     *  Discover 16
     *  JCB 16
     *  American Express 15
     *  Dinners Club 14
     * @param input 输入内容，字符串格式
     * @return true 合法
     */
    public static boolean isCardFormatValid(String input) {
        // 检测长度是否合法
        if (input.length() < 13 || input.length() > 16) {
            return false;
        }
        // 检测是否全为数字
        for (int i = 0; i < input.length(); ++ i) {
            if (input.charAt(i) - '0' < 0 || input.charAt(i) - '0' > 9) {
                return false;
            }
        }

        return true;
    }

    /**
     * 基于Luhn算法，判断卡号是否有效
     *
     * @param number 信用卡号
     * @return true 有效
     */
    public static boolean isValid(long number) {
        int sum = sumOfDoubleEvenPlace(number) + sumOfOddPlace(number);

        return (sum + number % 10) % 10 == 0;
    }

    /**
     * 基于规则，计算偶数位数字的二倍和
     *
     * @param number 信用卡号
     * @return 计算得到的数值
     */
    public static int sumOfDoubleEvenPlace(long number) {
        int sum = 0;
        String numberStr = Long.toString(number);
        for (int i = numberStr.length() - 2; i >= 0; i -= 2) {
            int numberInt = getDigit(numberStr.charAt(i) - '0');
            sum += 2 * numberInt;
        }
        return sum;

    }

    /**
     * 计算输入数字的每位数字的和，输入数字可能为1位或者2位
     *
     * @param number 输入数字
     * @return 计算得到的数值
     */
    public static int getDigit(int number) {
        if (number < 10) {
            return number;
        } else {
            return number / 10 + number % 10;
        }
    }

    /**
     * 基于规则，计算奇数位数的数字和
     *
     * @param number 信用卡号
     * @return 计算得到的数值
     */
    public static int sumOfOddPlace(long number) {
        int sum = 0;
        String numberStr = Long.toString(number);
        for (int i = numberStr.length() - 1; i >= 0; i -= 2) {
            sum += numberStr.charAt(i) - '0';
        }
        return sum;
    }

    /**
     * 判断输入的信用卡号，是否匹配d前缀
     *
     * @param number 信用卡号
     * @param d 要求匹配的前缀
     * @return true 满足
     */
    public static boolean prefixMatched(long number, int d) {
        int dLength = getSize(d);
        long prefix = getPrefix(number, dLength);

        if (prefix == 34 || prefix == 37 || prefix == 4 || (prefix < 56 && prefix > 49) || (prefix < 306 && prefix > 299) || (prefix < 3589 && prefix >3528) || prefix == 6334 || prefix == 6767) {
            return true;
        }
        return false;
    }

    /**
     * 返回输入数字的位数
     *
     * @param d 输入数字
     * @return 数字中包含的数字位数
     */
    public static int getSize(long d) {
        if (d == 0) {
            return 1;
        }
        int count = 0;
        while (d > 0) {
            count ++;
            d /= 10;
        }
        return count;
    }

    /**
     * 返回信用卡的前k位
     *
     * @param number 信用卡号
     * @param k 读取位数
     * @return 前k位形成的数字，若number不足k位，直接返回number
     */

    public static long getPrefix(long number, int k) {
        String numStr = Long.toString(number);
        if (numStr.length() < k) {
            return number;
        }
        return Long.parseLong(numStr.substring(0, k));
    }
}
 