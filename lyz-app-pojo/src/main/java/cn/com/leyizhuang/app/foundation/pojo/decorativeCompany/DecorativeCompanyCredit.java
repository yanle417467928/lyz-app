package cn.com.leyizhuang.app.foundation.pojo.decorativeCompany;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DecorativeCompanyCredit {
    private Long  cid;

    private BigDecimal credit;
}
