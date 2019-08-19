package xin.banghua.beiyuan.ParseJSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.PortUnreachableException;

public class ParseJSONObject {
    String rawJSON;
    JSONObject jsonObject;

    public ParseJSONObject(String rawJSON){
        this.rawJSON = rawJSON;
    }

    public JSONObject getParseJSON() throws JSONException {

        jsonObject = new JSONObject(rawJSON);

        return jsonObject;
    }
}
