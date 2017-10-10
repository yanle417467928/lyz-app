package cn.com.leyizhuang.app.foundation.vo;

import cn.com.leyizhuang.app.foundation.pojo.FitmentCompanyUserDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/20
 */
@Setter
@Getter
@ToString
public class FitmentCompanyUserVO {

    // 自增主键
    private Long id;

    // 员工姓名
    private String userName;

    // 员工手机号码
    private String mobile;

    // 是否是主账号
    private Boolean isMain = true;

    // 装饰公司id
    private Long companyId;

    //装饰公司名称
    private String companyName;

    // 是否被冻结，默认未冻结
    private Boolean frozen = false;

    //性别
    private Boolean sex;

    //年龄
    private Integer age;


    public static final List<FitmentCompanyUserVO> transform(List<FitmentCompanyUserDO> fitmentCompanyUserDOList) {
        List<FitmentCompanyUserVO> decorationCompanyUserVOList;
        if (null != fitmentCompanyUserDOList && fitmentCompanyUserDOList.size() > 0) {
            decorationCompanyUserVOList = new ArrayList<>(fitmentCompanyUserDOList.size());
            fitmentCompanyUserDOList.forEach(decorationCompanyUserDO -> decorationCompanyUserVOList.add(transform(decorationCompanyUserDO)));
        } else {
            decorationCompanyUserVOList = new ArrayList<>(0);
        }
        return decorationCompanyUserVOList;
    }

    public static final FitmentCompanyUserVO transform(FitmentCompanyUserDO fitmentCompanyUserDO) {
        if (null != fitmentCompanyUserDO) {
            FitmentCompanyUserVO fitmentCompanyUserVO = new FitmentCompanyUserVO();
            fitmentCompanyUserVO.setId(fitmentCompanyUserDO.getId());
            fitmentCompanyUserVO.setUserName(fitmentCompanyUserDO.getUserName());
            fitmentCompanyUserVO.setMobile(fitmentCompanyUserDO.getMobile());
            fitmentCompanyUserVO.setIsMain(fitmentCompanyUserDO.getIsMain());
            fitmentCompanyUserVO.setCompanyId(fitmentCompanyUserDO.getCompanyId());
            fitmentCompanyUserVO.setCompanyName(fitmentCompanyUserDO.getCompanyName());
            fitmentCompanyUserVO.setSex(fitmentCompanyUserDO.getSex());
            fitmentCompanyUserVO.setAge(fitmentCompanyUserDO.getAge());
            fitmentCompanyUserVO.setFrozen(fitmentCompanyUserDO.getFrozen());
            return fitmentCompanyUserVO;
        } else {
            return null;
        }
    }
}
