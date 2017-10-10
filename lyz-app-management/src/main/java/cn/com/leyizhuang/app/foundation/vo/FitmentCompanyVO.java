package cn.com.leyizhuang.app.foundation.vo;

import cn.com.leyizhuang.app.foundation.pojo.FitmentCompanyDO;
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
@Getter
@ToString
public class FitmentCompanyVO {

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

    public static final List<FitmentCompanyVO> transform(List<FitmentCompanyDO> fitmentCompanyDOList) {
        List<FitmentCompanyVO> decorationCompanyVOList;
        if (null != fitmentCompanyDOList && fitmentCompanyDOList.size() > 0) {
            decorationCompanyVOList = new ArrayList<>(fitmentCompanyDOList.size());
            fitmentCompanyDOList.forEach(decorationCompanyDO -> decorationCompanyVOList.add(transform(decorationCompanyDO)));
        } else {
            decorationCompanyVOList = new ArrayList<>(0);
        }
        return decorationCompanyVOList;
    }

    public static final FitmentCompanyVO transform(FitmentCompanyDO fitmentCompanyDO) {
        if (null != fitmentCompanyDO) {
            FitmentCompanyVO fitmentCompanyVO = new FitmentCompanyVO();
            fitmentCompanyVO.setId(fitmentCompanyDO.getId());
            fitmentCompanyVO.setName(fitmentCompanyDO.getName());
            fitmentCompanyVO.setCode(fitmentCompanyDO.getCode());
            fitmentCompanyVO.setAddress(fitmentCompanyDO.getAddress());
            fitmentCompanyVO.setPhone(fitmentCompanyDO.getPhone());
            fitmentCompanyVO.setCredit(fitmentCompanyDO.getCredit());
            fitmentCompanyVO.setPromotionMoney(fitmentCompanyDO.getPromotionMoney());
            fitmentCompanyVO.setWalletMoney(fitmentCompanyDO.getWalletMoney());
            fitmentCompanyVO.setFrozen(fitmentCompanyDO.getFrozen());
            return fitmentCompanyVO;
        } else {
            return null;
        }
    }

}
