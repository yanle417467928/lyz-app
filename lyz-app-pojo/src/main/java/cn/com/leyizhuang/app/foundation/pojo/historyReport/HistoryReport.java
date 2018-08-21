package cn.com.leyizhuang.app.foundation.pojo.historyReport;

import lombok.*;

/**
 * 历史报表
 *
 * @author LH
 * Created on 2017-10-10 10:48
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HistoryReport {

    private Long id;
    /**
     * 报表名称
     */
    private String reportName;
    /**
     * 报表地址
     */
    private String url;
}
