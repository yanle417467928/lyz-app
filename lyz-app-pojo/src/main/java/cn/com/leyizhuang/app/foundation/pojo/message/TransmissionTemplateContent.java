package cn.com.leyizhuang.app.foundation.pojo.message;

import lombok.*;

import java.io.Serializable;

/**
 * @author Created on 2018-01-26 10:42
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TransmissionTemplateContent implements Serializable {

    private static final long serialVersionUID = -3612408925068445861L;
    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 有效荷载
     */
    private Payload payload;


}
