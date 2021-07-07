
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject; 

public class requestApi {
 
    public static void callApi(JSONObject params, String type) throws Exception{
        
        HttpURLConnection conn = null;
        //JSONObject responseJson = null;
        
        try {
            //URL 설정
            URL url = new URL("http://localhost:8080/");
 
            conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod(type);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Transfer-Encoding", "chunked");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setDoOutput(true);
            
            // 카드사로부터 응답을 받았다고 가정.
            responseApi.response(params);



    }
    catch(Exception e) {
        throw new Exception();

    }

    }
}
