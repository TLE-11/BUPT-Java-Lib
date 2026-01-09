package Account;

import java.util.Date;

/**
 * 单笔交易记录（存款/取款）。
 * 该类用于记录银行账户的每一笔交易信息，包括交易类型、金额、余额、日期和描述。
 *
 * @author TLE-11
 * @version 1.0
 * @see Account
 */
public class Transaction {
    /** 交易类型：'D' 存款，'W' 取款 */
    private char type;
    /** 交易金额 */
    private double amount;
    /** 交易后账户余额 */
    private double balance;
    /** 交易日期 */
    private Date date;
    /** 交易描述 */
    private String desc;

    /**
     * 构造一笔完整的交易记录。
     *
     * @param type     交易类型，'D'表示存款，'W'表示取款
     * @param amount   交易金额，必须大于0
     * @param balance  交易后的账户余额
     * @param date     交易发生的日期和时间
     * @param desc     交易描述信息
     * @throws IllegalArgumentException 如果交易类型不是'D'或'W'，或者金额小于等于0
     */
    public Transaction(char type, double amount, double balance, Date date, String desc) {
        this.type = type;
        this.amount = amount;
        this.balance = balance;
        this.date = date;
        this.desc = desc;
    }

    /* ========== getter ========== */

    /**
     * 获取交易类型。
     *
     * @return 交易类型，'D'表示存款，'W'表示取款
     */
    public char getType() {
        return type;
    }

    /**
     * 获取交易金额。
     *
     * @return 交易金额
     */
    public double getAmount() {
        return amount;
    }

    /**
     * 获取交易后的账户余额。
     *
     * @return 交易后的账户余额
     */
    public double getBalance() {
        return balance;
    }

    /**
     * 获取交易日期。
     *
     * @return 交易发生的日期和时间
     */
    public Date getDate() {
        return date;
    }

    /**
     * 获取交易描述。
     *
     * @return 交易描述信息
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 返回交易记录的字符串表示形式。
     * 格式为：[日期 时间] 交易类型 金额 余额=余额 描述：描述信息
     *
     * @return 格式化的交易记录字符串
     */
    @Override
    public String toString() {
        return String.format("[%tF %<tT] %s %.2f  余额=%.2f  描述：%s",
                date, type == 'D' ? "存入" : "取出", amount, balance, desc);
    }
}