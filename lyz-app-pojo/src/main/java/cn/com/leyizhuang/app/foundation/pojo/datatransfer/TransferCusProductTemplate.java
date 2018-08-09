package cn.com.leyizhuang.app.foundation.pojo.datatransfer;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 12421 on 2018/8/7.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransferCusProductTemplate {

    private Long id;

    private String cusCode;

    private String cusMobile;

    private String cusName;

    private String empCode;

    private String empName;

    private Long storeId;

    private String storeName;

    private String storeCode;

    private String sku;

    private Integer quantity;

    private Boolean isGift;

    private Date buyTime;

    private Double buyPrice;

    private Boolean status;

}
