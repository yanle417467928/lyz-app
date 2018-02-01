package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Jerry.Ren
 * Notes: WMS~APP退单单头返配上架
 * Created with IntelliJ IDEA.
 * Date: 2018/1/12.
 * Time: 15:24.
 */

@Getter
@Setter
@ToString
public class WtaReturningOrderHeader {


    private Long id;

    /**
     * 创建时间（本条数据创建时间）
     */
    private Date createTime;
    /**
     * 仓库编号
     */
    private String whNo;
    /**
     * 委托业主
     */
    private String ownerNo;
    /**
     * 返配验收单号
     */
    private String recNo;

    /**
     * 返配单号
     */
    private String backNo;
    /**
     * 备注
     */
    private String note;

    /**
     * 门店退单
     */
    private String poNo;
    /**
     * 分公司ID
     */
    private Long companyId;
}
