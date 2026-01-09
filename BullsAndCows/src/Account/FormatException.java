package Account;

/**
 * 自定义格式异常类，用于处理用户名非字母、ID非数字等格式问题
 */
public class FormatException extends RuntimeException {
    private String inputValue;
    private String fieldName;

    public FormatException(String message, String fieldName, String inputValue) {
        super(message + " [字段: " + fieldName + ", 输入值: '" + inputValue + "']");
        this.fieldName = fieldName;
        this.inputValue = inputValue;
    }

    public FormatException(String message) {
        super(message);
    }

    public String getInputValue() {
        return inputValue;
    }

    public String getFieldName() {
        return fieldName;
    }
}