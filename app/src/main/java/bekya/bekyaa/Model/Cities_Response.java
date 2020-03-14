package bekya.bekyaa.Model;

import android.support.annotation.NonNull;

public class Cities_Response {

    private String id;
    private String name;
    public Cities_Response() {
    }
    public Cities_Response(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
