package cn.com.leyizhuang.app.foundation.pojo.management.guide;

import cn.com.leyizhuang.app.foundation.vo.management.guide.GuideCreditChangeDetailVO;
import lombok.*;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * 后台导购欠款
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
public class GuideArrears {

    private Long id;
    //创建时间
    private Date  createTime;
    //临时额度改变
    private GuideTempCreditChange tempCreditChangeId;
    //固定额度改变
    private GuideFixedCreditChange fixedCreditChangeId;
    //可用变更
    private GuideAvailableCreditChange availableCreditChangId;
    //相关单号
    private String referenceNumber;
    //变更类型
    private String changeType;
    //变更类型描述
    private String changeTypeDesc;
    //操作人id
    private Long operatorId;
    //操作人id
    private String operatorName;
    // 导购id
    private Long  empId;
    //操作人身份类型
    private String operatorType;
    //操作人IP
    private String operatorIp;

}
