import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;

import org.json.*; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.JSONParser; 
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
 
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.JSONObject;



public class requestApi {
 
    public static void callApi(JSONObject params, String type){
        
        HttpURLConnection conn = null;
        JSONObject responseJson = null;
        
        try {
            //URL 설정
            URL url = new URL("http://localhost:8080/test/api/action");
 
            conn = (HttpURLConnection) url.openConnection();
            
            // type의 경우 POST, GET, PUT, DELETE 가능
            conn.setRequestMethod(type);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Transfer-Encoding", "chunked");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setDoOutput(true);
            
            responseApi.response(params);



        
            
            // BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            // // JSON 형식의 데이터 셋팅

            // JSONObject jsonObject1 = new JSONObject(); // 중괄호에 들어갈 속성 정의 { "a" : "1", "b" : "2" }
            // JSONArray jsonArray1 = new JSONArray(); // 대괄호 정의 [{ "a" : "1", "b" : "2" }]
            // JSONObject finalJsonObject1 = new JSONObject(); // 중괄호로 감싸 대괄호의 이름을 정의함 { "c" : [{  "a" : "1", "b" : "2" }] }
            
            // HashMap<String, Object> myHashMap1 = new HashMap<String, Object>();
       
            // myHashMap1.put("이름", "잠자리");
            // myHashMap1.put("다리갯수", "6");

            // jsonObject1 = new JSONObject(myHashMap1); 
            // jsonArray1.add(jsonObject1 );

    }
    catch(Exception e) {

    }

    }
}
