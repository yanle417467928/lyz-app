package cn.com.leyizhuang.app.core.constant;


import cn.com.leyizhuang.app.core.config.InterfaceConfigure;

/**
 * @author Richard
 */
public class AppApplicationConstant {

    //从配置文件加载值的常量
    public static String imagePath;
    public static String alipayReturnUrl;
    public static String alipayReturnUrlAsnyc;
    public static String wechatReturnUrlAsnyc;
    public static String wechatApiClinetCert;
    public static String[] cdnHosts;
    public static String ossFolder;
    public static String ossBucket;
    public static String wmsUrl;
    public static String ebsUrl;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        AppApplicationConstant.imagePath = imagePath;
    }

    public String getAlipayReturnUrl() {
        return alipayReturnUrl;
    }

    public void setAlipayReturnUrl(String alipayReturnUrl) {
        AppApplicationConstant.alipayReturnUrl = alipayReturnUrl;
    }

    public String getAlipayReturnUrlAsnyc() {
        return alipayReturnUrlAsnyc;
    }

    public void setAlipayReturnUrlAsnyc(String alipayReturnUrlAsnyc) {
        AppApplicationConstant.alipayReturnUrlAsnyc = alipayReturnUrlAsnyc;
    }

    public String getWechatReturnUrlAsnyc() {
        return wechatReturnUrlAsnyc;
    }

    public void setWechatReturnUrlAsnyc(String wechatReturnUrlAsnyc) {
        AppApplicationConstant.wechatReturnUrlAsnyc = wechatReturnUrlAsnyc;
    }

    public String getWechatApiClinetCert() {
        return wechatApiClinetCert;
    }

    public void setWechatApiClinetCert(String wechatApiClinetCert) {
        AppApplicationConstant.wechatApiClinetCert = wechatApiClinetCert;
    }

    public String[] getCdnHosts() {
        return cdnHosts;
    }

    public void setCdnHosts(String[] cdnHosts) {
        AppApplicationConstant.cdnHosts = cdnHosts;
    }

    public String getOssFolder() {
        return ossFolder;
    }

    public void setOssFolder(String ossFolder) {
        AppApplicationConstant.ossFolder = ossFolder;
    }

    public String getOssBucket() {
        return ossBucket;
    }

    public void setOssBucket(String ossBucket) {
        AppApplicationConstant.ossBucket = ossBucket;
    }

    public String getWmsUrl() {
        return wmsUrl;
    }

    public void setWmsUrl(String wmsUrl) {
        AppApplicationConstant.wmsUrl = wmsUrl;
        InterfaceConfigure.WMS_WS_URL = wmsUrl;
    }

    public String getEbsUrl() {
        return ebsUrl;
    }

    public void setEbsUrl(String ebsUrl) {
        AppApplicationConstant.ebsUrl = ebsUrl;
        InterfaceConfigure.EBS_WS_URL = ebsUrl;
    }



    /**
     * 变更导购时间（天）
     */
    public static final int CHANGE_SELLER_DATE = 60;

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
     * 付清线
     */
    public static final Double PAY_UP_LIMIT = 0D;



}