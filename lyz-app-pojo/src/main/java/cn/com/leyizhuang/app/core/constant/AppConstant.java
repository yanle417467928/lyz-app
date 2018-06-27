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
     * 2018-03-15 因为业务需求暂时不开放乐币,设置比率为负,可关闭页面显示.
     */
    public static final Double RMB_TO_LEBI_RATIO = -10.00;

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
    public static final Double PAY_UP_LIMIT = 0.005;

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

    /**
     * 传wms接口报错接受错误信息的手机号
     */
    public static final String WMS_ERR_MOBILE = "18108117916";

    /**
     * 个推相关参数
     */
    public static final String GE_TUI_HOST = "http://sdk.open.api.igexin.com/apiex.htm";
    public static final String APP_ID = "I5pt99Lp5t7zrN98wU7Nt1";
    public static final String APP_KEY = "IWMq9NbOTJ9dhJUYEg5KV5";
    public static final String MASTER_SECRET = "T0qUSvYycU8TOZHeTWEGk1";

    /**
     * APP logo地址
     */
    public static final String APP_LOGO = "http://leyizhuang.oss-cn-shenzhen.aliyuncs.com/lyz-app/logo/Icon-256.png";

    /**
     * 二代App上线时间
     */
    public static final String PUB_DATE = "2018-03-28 00:00:00";

    /**
     * app 服务类商品companyFlag
     */
    public static final String FW_COMPANY_FLAG = "SRV|CVR|ART";


    /******************************** 银联支付相关 *************************/

    public static final String UNION_PAY_MERCHANT_ID ="302510189120014";

    /**
     * 银联支付编码
     */
    public static final String UNION_PAY_ENCODING = "UTF-8";


    /**
     * 利息利率单位
     */
    public static final Double INTEREST_RATE_UNIT = 0.0001;
}
