package Account;

import java.util.Scanner;

/**
 * 测试程序：按实验要求从标准输入读取账户信息并进行交易操作。
 * 该类演示了 NewAccount 类的使用方法，包括账户创建、存款、取款和异常处理功能。
 * 要求支持输入重定向：java AccountTest < input.txt
 *
 * @author TLE-11
 * @create 2025/11/11
 * @version 2.0
 * @see NewAccount
 * @see Account
 * @see Transaction
 * @see FormatException
 * @see AmountException
 */
public class AccountTest {

    /**
     * 检查用户名是否全部为英文字母
     * @param name 用户名
     * @return true如果全是字母，否则false
     */
    private static boolean isAllLetters(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        for (char c : name.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查用户ID是否全部为数字
     * @param id 用户ID字符串
     * @return true如果全是数字，否则false
     */
    private static boolean isAllDigits(String id) {
        if (id == null || id.isEmpty()) {
            return false;
        }
        for (char c : id.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 主方法，执行账户操作测试流程。
     * 读取顺序：
     * 1. 用户名称（必须全字母）
     * 2. 用户ID（必须全数字）
     * 3. 账号初始余额（必须为正数）
     * 4. 每一笔交易内容（存取取、金额、内容描述），直到遇到"E"结束
     *
     * 处理逻辑：
     * - 每个阶段如果读取到错误输入，则输出异常信息并继续读取，直到获得有效输入
     * - 交易处理阶段，如果遇到错误，输出异常信息并继续处理下一笔交易
     * - 所有处理完成后打印账户状态
     *
     * @param args 命令行参数（未使用）
     * @see NewAccount#deposit(double, String)
     * @see NewAccount#withdraw(double, String)
     * @see NewAccount#printAccountDetails()
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String name = null;
        String idStr = null;
        double initialBalance = 0;
        NewAccount account = null;

        // 阶段1：账户创建 - 使用循环读取直到获得有效输入
        System.out.println("=== 账户创建阶段 ===");

        // 1. 读取用户名称（必须全字母）
        while (name == null && scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            try {
                if (!isAllLetters(line)) {
                    throw new FormatException("用户名称必须全部为英文字母", "用户名称", line);
                }
                name = line;
                System.out.println("✓ 用户名称验证通过: " + name);
            } catch (FormatException e) {
                System.out.println("⚠ 格式异常: " + e.getMessage());
                // 继续读取下一行
            }
        }

        // 2. 读取用户ID（必须全数字）
        while (idStr == null && scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            try {
                if (!isAllDigits(line)) {
                    throw new FormatException("用户ID必须全部为数字", "用户ID", line);
                }
                idStr = line;
                System.out.println("✓ 用户ID验证通过: " + idStr);
            } catch (FormatException e) {
                System.out.println("⚠ 格式异常: " + e.getMessage());
                // 继续读取下一行
            }
        }

        // 3. 读取初始余额（必须为正数）
        while (initialBalance <= 0 && scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            try {
                double balance = Double.parseDouble(line);
                if (balance < 0) {
                    throw new AmountException("初始余额不能为负数", "初始余额", balance, 0);
                }
                initialBalance = balance;
                System.out.println("✓ 初始余额验证通过: " + initialBalance);
            } catch (NumberFormatException e) {
                System.out.println("⚠ 格式异常: 初始余额必须为数字 [字段: 初始余额, 输入值: '" + line + "']");
                // 继续读取下一行
            } catch (AmountException e) {
                System.out.println("⚠ 金额异常: " + e.getMessage());
                // 继续读取下一行
            }
        }

        // 创建账户（如果所有信息都有效）
        if (name != null && idStr != null && initialBalance > 0) {
            try {
                int id = Integer.parseInt(idStr);
                account = new NewAccount(id, initialBalance, name, 1.5);
                System.out.println("✓ 账户创建成功");
            } catch (Exception e) {
                System.out.println("⚠ 账户创建失败: " + e.getMessage());
                account = null;
            }
        } else {
            System.out.println("⚠ 账户信息不完整或无效，跳过账户创建");
        }

        // 阶段2：交易处理
        System.out.println("\n=== 交易处理阶段 ===");
        int transactionCount = 0;
        int successCount = 0;
        int errorCount = 0;

        while (scanner.hasNextLine()) {
            // 读取操作类型
            String operation = null;
            while (operation == null && scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                // 检查是否结束
                if (line.equalsIgnoreCase("E")) {
                    System.out.println("✓ 交易处理结束");
                    scanner.close();

                    // 打印统计和账户状态
                    printStatistics(transactionCount, successCount, errorCount);
                    if (account != null) {
                        System.out.println("\n" + "=".repeat(50));
                        System.out.println("最终账户状态:");
                        System.out.println("=".repeat(50));
                        account.printAccountDetails();
                    } else {
                        System.out.println("\n" + "=".repeat(50));
                        System.out.println("账户状态: 账户未创建，无状态信息");
                        System.out.println("=".repeat(50));
                    }
                    return;
                }

                try {
                    if (!line.equalsIgnoreCase("D") && !line.equalsIgnoreCase("W")) {
                        throw new FormatException("无效的操作类型，应为 D(存款) 或 W(取款)", "操作类型", line);
                    }
                    operation = line.toUpperCase();
                } catch (FormatException e) {
                    System.out.println("格式异常: " + e.getMessage());
                    // 继续读取下一行
                }
            }

            if (operation == null) {
                break; // 没有更多有效操作类型
            }

            // 读取金额
            Double amount = null;
            while (amount == null && scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                try {
                    double amt = Double.parseDouble(line);
                    amount = amt;
                } catch (NumberFormatException e) {
                    System.out.println("格式异常: 金额必须为数字 [字段: 金额, 输入值: '" + line + "']");
                    // 继续读取下一行
                }
            }

            if (amount == null) {
                System.out.println("交易数据不完整，缺失金额，跳过此交易");
                errorCount++;
                continue;
            }

            // 读取描述
            String description = "";
            if (scanner.hasNextLine()) {
                description = scanner.nextLine().trim();
            } else {
                System.out.println("交易数据不完整，缺失描述，跳过此交易");
                errorCount++;
                continue;
            }

            transactionCount++;

            // 执行交易
            try {
                if (account == null) {
                    System.out.printf("[交易%d] 账户不存在，跳过交易执行 (操作: %s, 金额: %.2f, 描述: %s)\n",
                            transactionCount, operation, amount, description);
                    errorCount++;
                    continue;
                }

                switch (operation) {
                    case "D": // 存款
                        try {
                            account.deposit(amount, description);
                            System.out.printf("[交易%d] 存款成功: %.2f, 描述: %s, 余额: %.2f\n",
                                    transactionCount, amount, description, account.getBalance());
                            successCount++;
                        } catch (AmountException e) {
                            System.out.printf("[交易%d] 存款异常: %s\n", transactionCount, e.getMessage());
                            errorCount++;
                        }
                        break;

                    case "W": // 取款
                        try {
                            account.withdraw(amount, description);
                            System.out.printf("[交易%d] 取款成功: %.2f, 描述: %s, 余额: %.2f\n",
                                    transactionCount, amount, description, account.getBalance());
                            successCount++;
                        } catch (AmountException e) {
                            System.out.printf("[交易%d] 取款异常: %s\n", transactionCount, e.getMessage());
                            errorCount++;
                        }
                        break;
                }

            } catch (Exception e) {
                System.out.printf("[交易%d] 未知异常: %s\n", transactionCount, e.getMessage());
                errorCount++;
            }
        }

        scanner.close();
        printStatistics(transactionCount, successCount, errorCount);

        // 打印账户明细（如果账户创建成功）
        if (account != null) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("最终账户状态:");
            System.out.println("=".repeat(50));
            account.printAccountDetails();
        } else {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("账户状态: 账户未创建，无状态信息");
            System.out.println("=".repeat(50));
        }
    }

    /**
     * 打印交易统计信息
     */
    private static void printStatistics(int total, int success, int error) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("交易处理统计:");
        System.out.println("-".repeat(50));
        System.out.printf("总交易数: %d\n", total);
        System.out.printf("成功交易: %d\n", success);
        System.out.printf("失败交易: %d\n", error);
    }
}