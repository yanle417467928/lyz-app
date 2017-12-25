package cn.com.leyizhuang.app.foundation.vo.management.guide;

import cn.com.leyizhuang.app.foundation.pojo.management.employee.SimpleEmployeeParam;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideAvailableCreditChange;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideCreditChangeDetailDO;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideFixedCreditChange;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideTempCreditChange;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 导购信用金改变详情VO
 *
 * @author liuh
 * Created on 2017-11-03 14:14
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GuideCreditChangeDetailVO {

    private Long id;
    //''导购ID''
    private Long empId;
    //''创建时间''
    private Date  createTime;
    //'临时额度改变
    private GuideTempCreditChange tempCreditChangeId;
    //''固定额度改变''
    private GuideFixedCreditChange fixedCreditChangeId;
    //''可用变更'
    private GuideAvailableCreditChange availableCreditChangId;
    //'相关单号'
    private String referenceNumber;
    //''变更类型'
    private String changeType;
    //''变更类型描述''
    private String changeTypeDesc;
    //''操作人id''
    private SimpleEmployeeParam operatorId;
    //操作人IP
    private String operatorIp;

    public static final GuideCreditChangeDetailVO transform(GuideCreditChangeDetailDO guideCreditChangeDetailDO) {
        if (null != guideCreditChangeDetailDO) {
            GuideCreditChangeDetailVO guideCreditChangeDetailVO = new GuideCreditChangeDetailVO();
            guideCreditChangeDetailVO.setId(guideCreditChangeDetailDO.getId());
            guideCreditChangeDetailVO.setChangeType(guideCreditChangeDetailDO.getChangeType());
            guideCreditChangeDetailVO.setChangeTypeDesc(guideCreditChangeDetailDO.getChangeTypeDesc());
            guideCreditChangeDetailVO.setCreateTime(guideCreditChangeDetailDO.getCreateTime());
            guideCreditChangeDetailVO.setAvailableCreditChangId(guideCreditChangeDetailDO.getAvailableCreditChangId());
            guideCreditChangeDetailVO.setFixedCreditChangeId(guideCreditChangeDetailDO.getFixedCreditChangeId());
            guideCreditChangeDetailVO.setTempCreditChangeId(guideCreditChangeDetailDO.getTempCreditChangeId());
            guideCreditChangeDetailVO.setOperatorId(guideCreditChangeDetailDO.getOperatorId());
            guideCreditChangeDetailVO.setReferenceNumber(guideCreditChangeDetailDO.getReferenceNumber());
            guideCreditChangeDetailVO.setEmpId(guideCreditChangeDetailDO.getEmpId());
            return guideCreditChangeDetailVO;
        } else {
            return null;
        }
    }

    public static final List<GuideCreditChangeDetailVO> transform(List<GuideCreditChangeDetailDO> guideCreditChangeDetailDOList) {
        List<GuideCreditChangeDetailVO> guideCreditChangeDetailVOList;
        if (null != guideCreditChangeDetailDOList && guideCreditChangeDetailDOList.size() > 0) {
            guideCreditChangeDetailVOList = new ArrayList<>(guideCreditChangeDetailDOList.size());
            guideCreditChangeDetailDOList.forEach(guideCreditChangeDetailDO -> guideCreditChangeDetailVOList.add(transform(guideCreditChangeDetailDO)));
        } else {
            guideCreditChangeDetailVOList = new ArrayList<>(0);
        }
        return guideCreditChangeDetailVOList;
    }
}
