package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.SexType;

import java.io.Serializable;
import java.util.Date;

/**
 * lyz-app-facade 用户抽象
 *
 * @author Richard
 * Created on 2017-09-19 11:00
 **/
public class AppCustomer implements Serializable {

    private static final long serialVersionUID = -5749739135096612483L;

    private Long id;

    //真实姓名
    private String name;

    //手机号码
    private String mobile;

    //生日
    private Date birthday;

    //状态 禁用，启用
    private Boolean status;

    //性别
    private SexType sex;

    //微信openId
    private String openId;

    //头像路径
    private String picUrl;

    //昵称
    private String nickName;

    //用户所在城市id
    private Long cityId;

    //销售顾问Id
    private Long salesConsultId;

    public AppCustomer() {
    }

    public AppCustomer(Long id, String name, String mobile, Date birthday, Boolean status, SexType sex, String openId, String picUrl, String nickName, Long cityId, Long salesConsultId) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.birthday = birthday;
        this.status = status;
        this.sex = sex;
        this.openId = openId;
        this.picUrl = picUrl;
        this.nickName = nickName;
        this.cityId = cityId;
        this.salesConsultId = salesConsultId;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public SexType getSex() {
        return sex;
    }

    public void setSex(SexType sex) {
        this.sex = sex;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getSalesConsultId() {
        return salesConsultId;
    }

    public void setSalesConsultId(Long salesConsultId) {
        this.salesConsultId = salesConsultId;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", birthday=" + birthday +
                ", status=" + status +
                ", sex=" + sex +
                ", openId='" + openId + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", nickName='" + nickName + '\'' +
                ", cityId=" + cityId +
                ", salesConsultId=" + salesConsultId +
                '}';
    }
}
