package cn.com.leyizhuang.app.foundation.pojo.datatransfer;

import lombok.*;

import java.util.Date;

/**
 * Created by 12421 on 2018/8/7.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransferCusTemplate {

    private Long id;

    private String storeName;

    private Long storeId;

    private String cusCode;

    private String cusName;

    private String cusType;

    private Date createTime;

    private String cusMobile;

    private String empCode;

    private String empName;

    private Boolean status;
}
