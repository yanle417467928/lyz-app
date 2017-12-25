package cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany;

import cn.com.leyizhuang.app.foundation.pojo.management.city.SimpleCityParam;
import cn.com.leyizhuang.app.foundation.pojo.management.store.StoreDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 装饰公司VO
 *
 * @author liuh
 * Created on 2017-11-03 14:14
 **/
@ToString
@Getter
@Setter
public class DecorativeCompanyVO {

    private Long id;

    //装饰公司编码
    private String storeCode;

    //城市ID
    private SimpleCityParam cityCode;

    //门店名称
    private String storeName;

    // 是否生效
    private Boolean enable;


    public static final DecorativeCompanyVO transform(StoreDO storeDO) {
        if (null != storeDO) {
            DecorativeCompanyVO decorativeCompanyVO = new DecorativeCompanyVO();
            decorativeCompanyVO.setId(storeDO.getStoreId());
            decorativeCompanyVO.setCityCode(storeDO.getCityCode());
            decorativeCompanyVO.setStoreName(storeDO.getStoreName());
            decorativeCompanyVO.setEnable(storeDO.getEnable());
            decorativeCompanyVO.setStoreCode(storeDO.getStoreCode());
            return decorativeCompanyVO;
        } else {
            return null;
        }
    }


    public static final List<DecorativeCompanyVO> transform(List<StoreDO> storeList) {
        List<DecorativeCompanyVO> decorativeCompanyVOList;
        if (null != storeList && storeList.size() > 0) {
            decorativeCompanyVOList = new ArrayList<>(storeList.size());
            storeList.forEach(storeDO -> decorativeCompanyVOList.add(transform(storeDO)));
        } else {
            decorativeCompanyVOList = new ArrayList<>(0);
        }
        return decorativeCompanyVOList;
    }
}
