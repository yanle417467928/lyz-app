package cn.com.leyizhuang.app.foundation.pojo.datatransfer;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 配送单配送时序图实体
 *
 * @author Richard
 * Created on 2017-08-14 9:38
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TdOrderDeliveryTimeSeqDetail {

    private Long id;

    //操作时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operationTime;

    //时序号

    private Integer seqNo;

    //操作类型
    private String operationType;

    //操作详细描述

    private String operationDescription;

    //主单号
    private String mainOrderNumber;

}
