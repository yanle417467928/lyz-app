package cn.com.leyizhuang.app.core.exception;


import cn.com.leyizhuang.app.core.constant.DataTransferExceptionType;

/**
 * 一二期数据迁移异常
 *
 * @author CrazyApeDX
 * Created on 2017/5/18.
 */
public class DataTransferException extends RuntimeException {

    private DataTransferExceptionType type;

    public DataTransferException(String message, DataTransferExceptionType type) {
        super(message);
        this.type = type;
    }

    public DataTransferExceptionType getType() {
        return type;
    }

    public void setType(DataTransferExceptionType type) {
        this.type = type;
    }
}
