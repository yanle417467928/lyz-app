package cn.com.leyizhuang.app.foundation.pojo.datatransfer;

import lombok.*;

import java.util.Date;


/**
 * @author GenerationRoad
 * @date 2018/3/24
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DataTransferErrorLog {

    private Long id;

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 时间
     */
    private Date createTime;

}
