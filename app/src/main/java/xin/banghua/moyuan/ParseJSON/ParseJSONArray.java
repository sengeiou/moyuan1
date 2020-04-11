package xin.banghua.moyuan.ParseJSON;

import org.json.JSONArray;
import org.json.JSONException;

public class ParseJSONArray {
    String rawJSON;
    JSONArray jsonArray;

    public ParseJSONArray(String rawJSON) {
        this.rawJSON = rawJSON;
    }

    public JSONArray getParseJSON() throws JSONException {

        jsonArray = new JSONArray(rawJSON);

        return jsonArray;
    }
}
