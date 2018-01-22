package cn.com.leyizhuang.app.core.config;

import cn.com.leyizhuang.app.core.constant.AppApplicationConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@AutoConfigureBefore(WebServiceConfiguration.class)
public class DeployConfiguration {

    private final Logger LOG = LoggerFactory.getLogger(DeployConfiguration.class);

    @Value("${deploy.image-path}")
    private String imagePath;

    @Value("${deploy.alipay.url}")
    private String alipayReturnUrl;

    @Value("${deploy.alipay.async.url}")
    private String alipayReturnUrlAsync;

    @Value("${deploy.wechat.async.url}")
    private String wechatReturnUrlAsnyc;

    @Value("${deploy.wechat.cert.path}")
    private String wechatApiClinetCert;

    @Value("${deploy.oss.cdnHosts}")
    private String[] cdnHosts;

    @Value("${deploy.oss.folder}")
    private String ossFolder;

    @Value("${deploy.oss.bucket}")
    private String ossBucket;
    /**
     * wmsUrl再给我合并掉了，我要咬你啊 = =！！！
     */
    @Value("${deploy.wms.url}")
    private String wmsUrl;

    @Bean
    public AppApplicationConstant applicationConstant() {
        LOG.info("imagePath : {}", imagePath);
        LOG.info("alipayReturnUrl : {}", alipayReturnUrl);
        LOG.info("alipayReturnUrlAsync : {}", alipayReturnUrlAsync);
        LOG.info("wechatReturnUrlAsnyc : {}", wechatReturnUrlAsnyc);
        LOG.info("wechatApiClinetCert : {}", wechatApiClinetCert);
        LOG.info("cdnHosts : {}", Arrays.toString(cdnHosts));
        LOG.info("ossFolder : {}", ossFolder);
        LOG.info("ossBucket : {}", ossBucket);
        LOG.info("wmsUrl : {}", wmsUrl);

        AppApplicationConstant constant = new AppApplicationConstant();
        constant.setImagePath(imagePath);
        constant.setAlipayReturnUrl(alipayReturnUrl);
        constant.setAlipayReturnUrlAsnyc(alipayReturnUrlAsync);
        constant.setWechatReturnUrlAsnyc(wechatReturnUrlAsnyc);
        constant.setWechatApiClinetCert(wechatApiClinetCert);
        constant.setCdnHosts(cdnHosts);
        constant.setOssFolder(ossFolder);
        constant.setOssBucket(ossBucket);
        constant.setWmsUrl(wmsUrl);
        return constant;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAlipayReturnUrl() {
        return alipayReturnUrl;
    }

    public void setAlipayReturnUrl(String alipayReturnUrl) {
        this.alipayReturnUrl = alipayReturnUrl;
    }

    public String getAlipayReturnUrlAsync() {
        return alipayReturnUrlAsync;
    }

    public void setAlipayReturnUrlAsync(String alipayReturnUrlAsync) {
        this.alipayReturnUrlAsync = alipayReturnUrlAsync;
    }

    public String getWechatReturnUrlAsnyc() {
        return wechatReturnUrlAsnyc;
    }

    public void setWechatReturnUrlAsnyc(String wechatReturnUrlAsnyc) {
        this.wechatReturnUrlAsnyc = wechatReturnUrlAsnyc;
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
        this.cdnHosts = cdnHosts;
    }

    public String getOssFolder() {
        return ossFolder;
    }

    public void setOssFolder(String ossFolder) {
        this.ossFolder = ossFolder;
    }

    public String getOssBucket() {
        return ossBucket;
    }

    public void setOssBucket(String ossBucket) {
        this.ossBucket = ossBucket;
    }

    public String getWmsUrl() {
        return wmsUrl;
    }

    public void setWmsUrl(String wmsUrl) {
        this.wmsUrl = wmsUrl;
    }
}