package cn.com.leyizhuang.app.foundation.pojo.vo;

import cn.com.leyizhuang.app.foundation.pojo.DecorationCompanyDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/19
 */
@Setter
@Getter@ToString
public class DecorationCompanyVO {

    // 自增主键
    private Long id;

    // 装饰公司名称
    private String name;

    // 装饰公司编码
    private String code;

    //公司地址
    private String address;

    //公司电话
    private String phone;

    // 信用金
    private Double credit = 0d;

    // 赞助金
    private Double promotionMoney = 0d;

    // 钱包金额
    private Double walletMoney = 0d;

    // 是否被冻结，默认未冻结
    private Boolean frozen = false;

    public static final List<DecorationCompanyVO> transform(List<DecorationCompanyDO> decorationCompanyDOList) {
        List<DecorationCompanyVO> decorationCompanyVOList;
        if (null != decorationCompanyDOList && decorationCompanyDOList.size() > 0) {
            decorationCompanyVOList = new ArrayList<>(decorationCompanyDOList.size());
            decorationCompanyDOList.forEach(decorationCompanyDO -> decorationCompanyVOList.add(transform(decorationCompanyDO)));
        } else {
            decorationCompanyVOList = new ArrayList<>(0);
        }
        return decorationCompanyVOList;
    }

    public static final DecorationCompanyVO transform(DecorationCompanyDO decorationCompanyDO) {
        if (null != decorationCompanyDO) {
            DecorationCompanyVO decorationCompanyVO = new DecorationCompanyVO();
            decorationCompanyVO.setId(decorationCompanyDO.getId());
            decorationCompanyVO.setName(decorationCompanyDO.getName());
            decorationCompanyVO.setCode(decorationCompanyDO.getCode());
            decorationCompanyVO.setAddress(decorationCompanyDO.getAddress());
            decorationCompanyVO.setPhone(decorationCompanyDO.getPhone());
            decorationCompanyVO.setCredit(decorationCompanyDO.getCredit());
            decorationCompanyVO.setPromotionMoney(decorationCompanyDO.getPromotionMoney());
            decorationCompanyVO.setWalletMoney(decorationCompanyDO.getWalletMoney());
            decorationCompanyVO.setFrozen(decorationCompanyDO.getFrozen());
            return decorationCompanyVO;
        } else {
            return null;
        }
    }

}
