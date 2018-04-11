package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author liuh
 * Notes:ebs~app 直营要货退货日志
 */
@Getter
@Setter
@ToString
public class EtaReturnAndRequireGoodsInfLog {

    private Long id;
    /**
     * 事务唯一ID
     */
    private String transId;
    /**
     * 创建时间
     */
    private Date creatDate;
    /**
     * 传输信息
     */
    private String msg;

}
