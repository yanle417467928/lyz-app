package cn.com.leyizhuang.app.foundation.pojo.datatransfer;

import lombok.*;

/**
 * Created by 12421 on 2018/8/7.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransferCusPreDepositTemplate {

    private Long id;

    private String cusName;

    private String cusCode;

    private Double cusPreDepost;

    private Boolean status;
}
