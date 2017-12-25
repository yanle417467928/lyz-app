package cn.com.leyizhuang.app.foundation.pojo.management.guide;

import cn.com.leyizhuang.app.foundation.pojo.management.employee.SimpleEmployeeParam;
import cn.com.leyizhuang.app.foundation.vo.management.guide.GuideCreditChangeDetailVO;
import lombok.*;

import java.util.Date;
/**
 * Created with IntelliJ IDEA.
 * 后台导购额度详情DO
 *
 * @author liuh
 * Date: 2017/11/23.
 * Time: 10:41.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GuideCreditChangeDetailDO {

    private Long id;
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
    // 导购id
    private Long  empId;
    //操作人身份类型
    private String operatorType;
    //操作人IP
    private String operatorIp;

    public static final GuideCreditChangeDetailDO transform(GuideCreditChangeDetailVO guideCreditChangeDetailVO) {
        if (null != guideCreditChangeDetailVO) {
            GuideCreditChangeDetailDO guideCreditChangeDetailDO = new GuideCreditChangeDetailDO();
            guideCreditChangeDetailDO.setId(guideCreditChangeDetailVO.getId());
            guideCreditChangeDetailDO.setChangeType(guideCreditChangeDetailVO.getChangeType());
            guideCreditChangeDetailDO.setChangeTypeDesc(guideCreditChangeDetailVO.getChangeTypeDesc());
            guideCreditChangeDetailDO.setCreateTime(guideCreditChangeDetailVO.getCreateTime());
            guideCreditChangeDetailDO.setAvailableCreditChangId(guideCreditChangeDetailVO.getAvailableCreditChangId());
            guideCreditChangeDetailDO.setFixedCreditChangeId(guideCreditChangeDetailVO.getFixedCreditChangeId());
            guideCreditChangeDetailDO.setTempCreditChangeId(guideCreditChangeDetailVO.getTempCreditChangeId());
            guideCreditChangeDetailDO.setOperatorId(guideCreditChangeDetailVO.getOperatorId());
            guideCreditChangeDetailDO.setReferenceNumber(guideCreditChangeDetailVO.getReferenceNumber());
            guideCreditChangeDetailDO.setEmpId(guideCreditChangeDetailVO.getEmpId());
            return guideCreditChangeDetailDO;
        } else {
            return null;
        }
    }

}
