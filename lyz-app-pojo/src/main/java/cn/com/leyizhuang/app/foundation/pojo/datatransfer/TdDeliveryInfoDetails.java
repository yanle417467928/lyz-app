package cn.com.leyizhuang.app.foundation.pojo.datatransfer;


import lombok.Data;

import java.util.Date;

/**
 * @author Jerry.Ren
 * create 2018-03-24 9:34
 * desc:
 **/
@Data
public class TdDeliveryInfoDetails {

    private Long id;

    private String mainOrderNumber;

    private String operationDescription;

    private Date operationTime;

    private String operationType;

    private String seqNo;

    private String driver;

    private String whNo;

    private String taskNo;

    private Date payTime;

    private String diySiteCode;

    private String sku;

    private Double jxDif;

    private Integer quantity;

    private Double difTotal;

    private Long statusId;
}
