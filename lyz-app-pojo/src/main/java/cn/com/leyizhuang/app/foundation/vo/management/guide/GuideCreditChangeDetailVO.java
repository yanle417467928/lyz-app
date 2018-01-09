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
    //'导购ID'
    private Long empId;
    //'创建时间'
    private Date  createTime;
    //'临时额度金额改变id
    private GuideTempCreditChange tempCreditChangeId;
    //'固定额度金额改变id'
    private GuideFixedCreditChange fixedCreditChangeId;
    //'可用额度金额改变id'
    private GuideAvailableCreditChange availableCreditChangId;
    //'相关单号'
    private String referenceNumber;
    //'变更类型'
    private String changeType;
    //''变更类型描述''
    private String changeTypeDesc;
    //'操作人id'
    private Long operatorId;
    //'操作人id'
    private String operatorName;
    //操作人IP
    private String operatorIp;

    public static final GuideCreditChangeDetailVO transform(GuideCreditChangeDetailDO guideCreditChangeDetailDO) {
        if (null != guideCreditChangeDetailDO) {
            GuideCreditChangeDetailVO guideCreditChangeDetailVO = new GuideCreditChangeDetailVO();
            guideCreditChangeDetailVO.setId(guideCreditChangeDetailDO.getId());
            if("CANCEL_ORDER".equals(guideCreditChangeDetailDO.getChangeType())){
                guideCreditChangeDetailVO.setChangeType("取消订单");
            }else if("PLACE_ORDER".equals(guideCreditChangeDetailDO.getChangeType())){
                guideCreditChangeDetailVO.setChangeType("下订单");
            }
            guideCreditChangeDetailVO.setChangeTypeDesc(guideCreditChangeDetailDO.getChangeTypeDesc());
            guideCreditChangeDetailVO.setCreateTime(guideCreditChangeDetailDO.getCreateTime());
            guideCreditChangeDetailVO.setAvailableCreditChangId(guideCreditChangeDetailDO.getAvailableCreditChangId());
            guideCreditChangeDetailVO.setFixedCreditChangeId(guideCreditChangeDetailDO.getFixedCreditChangeId());
            guideCreditChangeDetailVO.setTempCreditChangeId(guideCreditChangeDetailDO.getTempCreditChangeId());
            guideCreditChangeDetailVO.setOperatorId(guideCreditChangeDetailDO.getOperatorId());
            guideCreditChangeDetailVO.setReferenceNumber(guideCreditChangeDetailDO.getReferenceNumber());
            guideCreditChangeDetailVO.setEmpId(guideCreditChangeDetailDO.getEmpId());
            guideCreditChangeDetailVO.setOperatorName(guideCreditChangeDetailDO.getOperatorName());
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
