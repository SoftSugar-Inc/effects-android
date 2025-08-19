package softsugar.senseme.com.effects.display.JsonUtil;

public class JsonDataInfo {
    private int code;
    private String data;

    @Override
    public String toString() {
        return "code = "+code+ " data = " + data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
