package cn.com.leyizhuang.app.foundation.pojo.order;

import lombok.*;

/**
 * 门店订单主体信息
 *
 * @author Richard
 * Created on 2017-10-10 11:14
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StoreOrderSubjectInfo {


    //下单人门店编码
    private String storeCode;

    //门店组织全编码
    private String storeStructureCode;

    //下单人id
    private Long creatorIdStore;

    //下单人姓名
    private String creatorNameStore;

    //下单人手机
    private String creatorPhoneStore;

    //导购id
    private Long salesConsultId;

    //导购姓名
    private String salesConsultName;

    //导购手机
    private String salesConsultPhone;

    //顾客id
    private Long customerId;

    //顾客姓名
    private String customerName;

    //顾客手机
    private String customerPhone;

}
