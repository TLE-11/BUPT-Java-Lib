package Account;

import java.util.ArrayList;
import java.util.List;

/**
 * 带有交易记录功能的账户子类。
 * 继承自 Account 类，增加了客户姓名和交易记录功能，可以记录所有的存款和取款操作。
 * 支持生成账户明细报告，包括控制台输出和 HTML 格式文件。
 *
 * @author TLE-11
 * @version 1.0
 * @since 2025/11/11
 * @see Account
 * @see Transaction
 */
public class NewAccount extends Account {
    /** 客户姓名 */
    private String name;
    /** 按时间顺序保存所有交易 */
    private List<Transaction> transactions;

    /**
     * 创建新账户。
     * 初始化账户信息并记录开户交易。
     *
     * @param id 账户编号
     * @param balance 初始余额
     * @param name 客户姓名
     * @param annualInterestRate 年利率（百分比）
     * @throws IllegalArgumentException 如果初始余额小于0
     */
    public NewAccount(int id, double balance, String name, double annualInterestRate) {
        super(id, balance);
        this.name = name;
        setAnnualInterestRate(annualInterestRate);
        transactions = new ArrayList<>();
        // 初始余额也记为一笔交易
        transactions.add(new Transaction('D', balance, balance, getDateCreated(), "开户初始金额"));
    }

    /**
     * 获取客户姓名。
     *
     * @return 客户姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 返回不可修改的交易列表，仅供查看。
     * 使用此方法可以防止外部代码修改交易记录，保证交易记录的完整性。
     *
     * @return 不可修改的交易列表
     * @see java.util.Collections#unmodifiableList(List)
     */
    public List<Transaction> getTransactions() {
        return java.util.Collections.unmodifiableList(transactions);
    }

    /**
     * 存款操作。
     * 重写父类方法，在存款的同时记录交易信息。
     *
     * @param amount 存入金额，必须大于0
     * @throws AmountException 如果存入金额小于等于0
     * @see Account#deposit(double)
     */
    @Override
    public void deposit(double amount) {
        super.deposit(amount);                     // 更新余额
        transactions.add(new Transaction('D', amount, getBalance(), new java.util.Date(),
                "存款"));
    }

    /**
     * 带描述的存款操作。
     * 允许为存款交易添加自定义描述信息。
     *
     * @param amount 存入金额，必须大于0
     * @param desc 交易描述信息
     * @throws AmountException 如果存入金额小于等于0
     */
    public void deposit(double amount, String desc) {
        super.deposit(amount);
        transactions.add(new Transaction('D', amount, getBalance(), new java.util.Date(), desc));
    }

    /**
     * 取款操作。
     * 重写父类方法，在取款的同时记录交易信息。
     *
     * @param amount 取出金额，必须大于0且不超过当前余额
     * @throws AmountException 如果取出金额小于等于0或超过当前余额
     * @see Account#withdraw(double)
     */
    @Override
    public void withdraw(double amount) {
        super.withdraw(amount);                    // 更新余额
        transactions.add(new Transaction('W', amount, getBalance(), new java.util.Date(),
                "取款"));
    }

    /**
     * 带描述的取款操作。
     * 允许为取款交易添加自定义描述信息。
     *
     * @param amount 取出金额，必须大于0且不超过当前余额
     * @param desc 交易描述信息
     * @throws AmountException 如果取出金额小于等于0或超过当前余额
     */
    public void withdraw(double amount, String desc) {
        super.withdraw(amount);
        transactions.add(new Transaction('W', amount, getBalance(), new java.util.Date(), desc));
    }

    /**
     * 打印账户明细。
     * 同时在控制台输出和生成 HTML 格式的账户明细文件。
     * HTML 文件保存为 "account.html"，使用 UTF-8 编码。
     *
     * @see Transaction
     */
    public void printAccountDetails() {

        System.out.println("------------------ 账户明细 ------------------");
        System.out.println("姓名：" + name);
        System.out.println("年利率：" + getAnnualInterestRate() + "%");
        System.out.println("--------------------------------------------");
        for (Transaction t : transactions) {
            System.out.println(t);
        }
        System.out.println("--------------------------------------------");
    }
}