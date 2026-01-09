package Account;

/**
 * 自定义金额异常类，用于处理金额相关的错误
 */
public class AmountException extends RuntimeException {
    private double amount;
    private String operationType; // "存款" 或 "取款"
    private double currentBalance;

    public AmountException(String message, String operationType, double amount, double currentBalance) {
        super(String.format("%s [操作: %s, 金额: %.2f, 当前余额: %.2f]",
                message, operationType, amount, currentBalance));
        this.operationType = operationType;
        this.amount = amount;
        this.currentBalance = currentBalance;
    }

    public double getAmount() {
        return amount;
    }

    public String getOperationType() {
        return operationType;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }
}