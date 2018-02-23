package cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany;

import cn.com.leyizhuang.app.foundation.pojo.management.city.SimpleCityParam;
import cn.com.leyizhuang.app.foundation.pojo.management.store.StoreDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 装饰公司详情VO
 *
 * @author liuh
 * Created on 2017-11-03 14:14
 **/
@ToString
@Getter
@Setter
public class DecorativeCompanyDetailVO {

    private Long id;

    //装饰公司编码
    private String storeCode;

    //城市ID
    private SimpleCityParam cityCode;

    //门店名称
    private String storeName;

    // 是否生效
    private Boolean enable;

    // 创建时间
    private Date createTime;

    // 销售经理
    private String salesManager;



    public static final DecorativeCompanyDetailVO transform(StoreDO storeDO) {
        if (null != storeDO) {
            DecorativeCompanyDetailVO decorativeCompanyVO = new DecorativeCompanyDetailVO();
            decorativeCompanyVO.setId(storeDO.getStoreId());
            decorativeCompanyVO.setCityCode(storeDO.getCityCode());
            decorativeCompanyVO.setStoreName(storeDO.getStoreName());
            decorativeCompanyVO.setCreateTime(storeDO.getCreateTime());
            decorativeCompanyVO.setEnable(storeDO.getEnable());
            decorativeCompanyVO.setStoreCode(storeDO.getStoreCode());
            decorativeCompanyVO.setSalesManager(storeDO.getSalesManager());
            return decorativeCompanyVO;
        } else {
            return null;
        }
    }


    public static final List<DecorativeCompanyDetailVO> transform(List<StoreDO> storeList) {
        List<DecorativeCompanyDetailVO> decorativeCompanyVOList;
        if (null != storeList && storeList.size() > 0) {
            decorativeCompanyVOList = new ArrayList<>(storeList.size());
            storeList.forEach(storeDO -> decorativeCompanyVOList.add(transform(storeDO)));
        } else {
            decorativeCompanyVOList = new ArrayList<>(0);
        }
        return decorativeCompanyVOList;
    }
}
