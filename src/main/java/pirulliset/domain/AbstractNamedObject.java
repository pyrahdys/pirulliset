package pirulliset.domain;

public class AbstractNamedObject {

    private Integer id;

    public AbstractNamedObject(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}