package Account;

import java.util.Date;

/**
 * 银行账户基类，封装所有账户共有的属性与行为。
 * 该类提供了账户的基本操作，包括存款、取款、查询余额和利率等功能。
 *
 * @author TLE-11
 * @version 1.0
 * @since 2025/11/11
 */
public class Account {
    /** 账户编号 */
    private int id;
    /** 账户余额 */
    private double balance;
    /** 年利率（所有账户共享） */
    private double annualInterestRate;
    /** 开户日期 */
    private Date dateCreated;

    /**
     * 创建默认账户。
     * 账户编号和余额都初始化为0，年利率为0，开户日期为当前日期。
     */
    public Account() {
        this(0, 0);
    }

    /**
     * 创建指定id和初始余额的账户。
     * 年利率初始化为0，开户日期为当前日期。
     *
     * @param id 账户编号
     * @param balance 初始余额
     */
    public Account(int id, double balance) {
        this.id = id;
        this.balance = balance;
        this.annualInterestRate = 0;
        this.dateCreated = new Date();
    }

    /* ========== getter/setter ========== */

    /**
     * 获取账户编号。
     *
     * @return 账户编号
     */
    public int getId() {
        return id;
    }

    /**
     * 获取账户余额。
     *
     * @return 账户余额
     */
    public double getBalance() {
        return balance;
    }

    /**
     * 获取年利率。
     *
     * @return 年利率（百分比形式）
     */
    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    /**
     * 获取开户日期。
     *
     * @return 开户日期
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * 设置账户编号。
     *
     * @param id 新的账户编号
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 设置账户余额。
     *
     * @param balance 新的账户余额
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * 设置年利率。
     *
     * @param annualInterestRate 新的年利率（百分比形式）
     */
    public void setAnnualInterestRate(double annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    /**
     * 获取月利率。
     * 将年利率转换为月利率，假设按月计息。
     *
     * @return 月利率（小数形式，非百分比）
     */
    public double getMonthlyInterestRate() {
        return annualInterestRate / 12 / 100;
    }

    /**
     * 向账户存款。
     * 将指定金额添加到账户余额中。
     *
     * @param amount 存入金额，必须大于0
     * @throws AmountException 如果存入金额小于等于0
     */
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new AmountException("存入金额必须 > 0", "存款", amount, balance);
        }
        balance += amount;
    }

    /**
     * 从账户取款。
     * 从账户余额中扣除指定金额。
     *
     * @param amount 取出金额，必须大于0且不超过当前余额
     * @throws AmountException 如果取出金额小于等于0或超过当前余额
     */
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new AmountException("取出金额必须 > 0", "取款", amount, balance);
        }
        if (amount > balance) {
            throw new AmountException("余额不足", "取款", amount, balance);
        }
        balance -= amount;
    }
}