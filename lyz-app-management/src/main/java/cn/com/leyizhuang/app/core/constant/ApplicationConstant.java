package cn.com.leyizhuang.app.core.constant;


import cn.com.leyizhuang.app.core.config.InterfaceConfigure;

public class ApplicationConstant {

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

    public static String FIT_ORDER_TEMPLATE_URL;

    public static String unionPayRefundAsyncBack;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        ApplicationConstant.imagePath = imagePath;
    }

    public String getAlipayReturnUrl() {
        return alipayReturnUrl;
    }

    public void setAlipayReturnUrl(String alipayReturnUrl) {
        ApplicationConstant.alipayReturnUrl = alipayReturnUrl;
    }

    public String getAlipayReturnUrlAsnyc() {
        return alipayReturnUrlAsnyc;
    }

    public void setAlipayReturnUrlAsnyc(String alipayReturnUrlAsnyc) {
        ApplicationConstant.alipayReturnUrlAsnyc = alipayReturnUrlAsnyc;
    }

    public String getWechatReturnUrlAsnyc() {
        return wechatReturnUrlAsnyc;
    }

    public void setWechatReturnUrlAsnyc(String wechatReturnUrlAsnyc) {
        ApplicationConstant.wechatReturnUrlAsnyc = wechatReturnUrlAsnyc;
    }

    public String[] getCdnHosts() {
        return cdnHosts;
    }

    public void setCdnHosts(String[] cdnHosts) {
        ApplicationConstant.cdnHosts = cdnHosts;
    }

    public String getOssFolder() {
        return ossFolder;
    }

    public void setOssFolder(String ossFolder) {
        ApplicationConstant.ossFolder = ossFolder;
    }

    public String getOssBucket() {
        return ossBucket;
    }

    public void setOssBucket(String ossBucket) {
        ApplicationConstant.ossBucket = ossBucket;
    }

    public String getWmsUrl() {
        return wmsUrl;
    }

    public void setWmsUrl(String wmsUrl) {
        ApplicationConstant.wmsUrl = wmsUrl;
        InterfaceConfigure.WMS_WS_URL = wmsUrl;
    }

    public String getEbsUrl() {
        return ebsUrl;
    }

    public void setEbsUrl(String ebsUrl) {
        ApplicationConstant.ebsUrl = ebsUrl;
        InterfaceConfigure.EBS_WS_URL = ebsUrl;
    }

    public String getWechatApiClinetCert() {
        return wechatApiClinetCert;
    }

    public void setWechatApiClinetCert(String wechatApiClinetCert) {
        ApplicationConstant.wechatApiClinetCert = wechatApiClinetCert;
    }

    public String getFitOrderTemplateUrl() {
        return FIT_ORDER_TEMPLATE_URL;
    }

    public void setFitOrderTemplateUrl(String fitOrderTemplateUrl) {
        FIT_ORDER_TEMPLATE_URL = fitOrderTemplateUrl;
    }

    public String getUnionPayRefundAsyncBack() {
        return unionPayRefundAsyncBack;
    }

    public void setUnionPayRefundAsyncBack(String unionPayRefundAsyncBack) {
        ApplicationConstant.unionPayRefundAsyncBack = unionPayRefundAsyncBack;
    }
}
