package cn.com.leyizhuang.app.foundation.pojo;


/**
 * @author CrazyApeDX
 *         Created on 2017/3/24.
 */
public class Area {

    private static final long serialVersionUID = 4587626195174356726L;

    public Area() {
        super();
    }

    public Area(Long id, String name, Integer sobId) {
        super();
        this.id = id;
        this.name = name;
        this.sobId = sobId;
    }
    private Long id;
    private String name;
    private Integer sobId;

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

    public Integer getSobId() {
        return sobId;
    }

    public void setSobId(Integer sobId) {
        this.sobId = sobId;
    }
}
