package cn.com.leyizhuang.app.foundation.pojo;


/**
 * @author CrazyApeDX
 *         Created on 2017/3/24.
 */
public class Store {

    private static final long serialVersionUID = 928068647093677455L;

    public Store() {
        super();
    }

    public Store(Long id, String name, String code) {
        super();
        this.id = id;
        this.name = name;
        this.code = code;
    }

    private Long id;
    private String name;
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
