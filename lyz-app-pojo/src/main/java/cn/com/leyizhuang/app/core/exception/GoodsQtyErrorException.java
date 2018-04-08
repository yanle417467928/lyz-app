package cn.com.leyizhuang.app.core.exception;

/**
 * 商品数量异常
 *
 * @author Richard
 * Created on 2017/5/18.
 */
public class GoodsQtyErrorException extends RuntimeException {

    public GoodsQtyErrorException(String message) {
        super(message);
    }
}
