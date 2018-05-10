package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Jerry.Ren
 * create 2018-01-27 16:19
 * desc: 取消退单结果确认
 **/
@Getter
@Setter
@ToString
public class WtaCancelReturnOrderResultEnter {

    private Long id;

    /**
     * 创建时间(wms提供)
     */
    private Date createTime;

    /**
     * 退单号
     */
    private String returnNumber;

    /**
     * 是否已退
     */
    private Boolean isCancel;

    //接收时间
    private Date receiveTime;
    //处理标记
    private String handleFlag;
    //错误信息
    private String errorMessage;
    //处理时间
    private Date handleTime;
}
