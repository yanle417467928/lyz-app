package cn.com.leyizhuang.app.core.constant;

/**
 * Richard
 *
 * @author App常量
 * Created on 2017-09-20 15:38
 **/
public class AppConstant {

    /**
     * 用户密码加盐
     */
    public static final String APP_USER_SALT = "le1yi2zhuang3";

    /**
     * 不允许门店自提的公司标识
     */

    public static final String FORBIDDEN_SELF_TAKE_COMPANY_FLAG = "YR|LYZ";

    /**
     * 订单有效时间,按分钟计算
     */
    public static final int ORDER_EFFECTIVE_MINUTE = 60;
    /**
     * RMB对乐币比率
     */
    public static final Double RMB_TO_LEBI_RATIO = 10.00;

    /**
     * 乐观锁重试次数
     */
    public static final int OPTIMISTIC_LOCK_RETRY_TIME = 3;

    /**
     * 签到赠送乐币数量
     */
    public static final int SIGN_AWARD_LEBI_QTY = 1;

    /**
     * 付清线
     */
    public static final Double PAY_UP_LIMIT = 0D;

    /**
     * Double 零值
     */
    public static final Double DOUBLE_ZERO = 0D;

    /**
     * Integer 零值
     */
    public static final Integer INTEGER_ZERO = 0;

    /**
     * EBS接口发送地址
     */
    public static final String EBS_NEW_URL = "http://ebs.leyizhuang.com.cn:10001/ebs/";

    /**
     * WMS接口地址
     */
    public static final String WMS_URL = "http://120.76.214.99:8199/WmsInterServer.asmx?wsdl";

}
