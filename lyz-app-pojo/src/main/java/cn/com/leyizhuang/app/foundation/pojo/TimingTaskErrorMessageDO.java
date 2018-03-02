package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * 定时任务错误记录
 * Created by caiyu on 2018/2/28.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TimingTaskErrorMessageDO {
    /**
     * id
     */
    private Long id;
    /**
     * 错误信息
     */
    private String message;
    /**
     * 对应单号
     */
    private String orderNumber;
    /**
     * 处理时间
     */
    private Date recordTime;
}
