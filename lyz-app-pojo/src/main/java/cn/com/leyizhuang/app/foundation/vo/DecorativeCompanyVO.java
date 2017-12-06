package cn.com.leyizhuang.app.foundation.vo;

import cn.com.leyizhuang.app.foundation.pojo.store.StoreDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ToString
@Getter
@Setter
public class DecorativeCompanyVO {

    private Long id;

    //装饰公司编码
    private String storeCode;

    //城市ID
    private CityVO cityCode;

    //门店名称
    private String storeName;

    // 是否生效
    private Boolean enable;

    // 创建时间
    private Date createTime;

    public static final DecorativeCompanyVO transform(StoreDO storeDO) {
        if (null != storeDO) {
            DecorativeCompanyVO decorativeCompanyVO = new DecorativeCompanyVO();
            decorativeCompanyVO.setId(storeDO.getStoreId());
            decorativeCompanyVO.setCityCode(storeDO.getCityCode());
            decorativeCompanyVO.setStoreName(storeDO.getStoreName());
            decorativeCompanyVO.setCreateTime(storeDO.getCreateTime());
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
