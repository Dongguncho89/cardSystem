
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Random;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;


public class Payment {

    public static  void request (long cardNo, String gbn, String avlYmd, int cvc, int monthlyPay ,int trnsAmt ) throws Exception {
        
     /*** 헤더를 생성한다 (데이터길이|데이터구분|관리번호) */
        String dataLenth="446" ;
        String dataGbn ;
        String mgntId ;

        dataLenth=String.format("%4s", dataLenth);
        dataGbn = String.format("%-10s", gbn); 

        

        SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddHHmmss");
        String format_time1 = format1.format (System.currentTimeMillis());

        System.out.println("format_time1 :: "+format_time1);

        int rand;
        Random rd = new Random();//랜덤 객체 생성
        rand = rd.nextInt(100);
        String rand_st ;
        rand_st = String.format("%06d", rand);
        System.out.println(rand_st);

        // 관리번호생성(현재일시(14자리)+난수(6자리)
        mgntId=format_time1+rand_st;

        System.out.println("dataLenth :: "+dataLenth);
        System.out.println("dataGbn   :: "+dataGbn);
        System.out.println("mgntId :: "+mgntId);
        /*** 헤더부분 생성 끝 */

        /** 데이터부분 생성시작 */
        
        String cardNoTrns;

        cardNoTrns = String.valueOf(cardNo);
        cardNoTrns = String.format("%-20s", cardNo);

        

        String monthlyPayTrns;
        monthlyPayTrns = String.valueOf(monthlyPay);
        monthlyPayTrns = String.format("%02d", monthlyPay);
        //monthlyPay
        
        String avlYmdTrns ; 
        avlYmdTrns = avlYmd;
        
        String cvcTrns;
        cvcTrns = String.valueOf(cvc);
        cvcTrns = String.format("%-3d", cvc);
        
        String trnsAmtTrns;
        trnsAmtTrns = String.valueOf(trnsAmt);
        trnsAmtTrns = String.format("%10d", trnsAmt);   
        
        String vatTrns;
        int vat=0;
        
        vat = trnsAmt / 11 ; 
        vatTrns = String.format("%10d", vat); 
        
        String mgntIdTrns = " ";
        
        if (gbn.equals("CANCEL") ){

            mgntIdTrns = mgntId;

        }
        String password ;

        String encryptedTrns ;

        password = cardNo+"|"+avlYmd+"|"+cvc;
        
        HashMap<String, String> rsaKeyPair = encryptDecrypt.createKeypairAsString();
        String publicKey = rsaKeyPair.get("publicKey");
        String privateKey = rsaKeyPair.get("privateKey");
    
        String encrypted = encryptDecrypt.encode(password,publicKey);

        //int encryplen =0 ;
        //encryplen = encrypted.length();

        //System.out.println("encryplen 정보 :: " + encryplen);


        //encryptedTrns = encrypted.substring(1,300);

        encryptedTrns = String.format("%-300s", encrypted); 

        //String decrypted = encryptDecrypt.decryptRSA(encrypted,privateKey);

        String dbFileUrl = "jdbc:sqlite:test.db";
        try {
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection(dbFileUrl);
            System.out.println("Sqlite conn!!");

            Statement stmt = con.createStatement();
            // 암호화 정보를 저장하기 위한 구문
            ResultSet rs = stmt.executeQuery("INSERT INTO ENCDEC_INFO VALUES ('"+encrypted+"','"+privateKey+"')");
            //rs = stmt.executeQuery("INSERT INTO TRX_INFO VALUES ("+mgntId+","+header+","+data+",DATETIME('now')");
            System.out.println(rs);
           
        }
        catch(Exception e) {
            System.out.println(e);
        }


        System.out.println("암호화 정보 :: " + encryptedTrns);
        
        //String de_pass = encryptDecrypt.SHA512(pw_pass, salt);

       // System.out.println("복호화 정보 :: " + decrypted);
        
        String filed = "";
        String filedTrns;
        filedTrns = String.format("%-47s", filed); 

        System.out.println("카드 전송 정보 :: " + cardNoTrns);
        System.out.println("할부 전송 정보 :: " + monthlyPayTrns);
        System.out.println("유효일자 전송 정보 :: " + avlYmdTrns);
        System.out.println("CVC 전송 정보 :: " + cvcTrns);
        System.out.println("거래금액 전송 정보 :: " + trnsAmtTrns);
        System.out.println("부가세 전송 정보 :: " + vatTrns);
        System.out.println("관리번호 전송 정보 :: " + mgntIdTrns);
        System.out.println("암호화 전송 정보 :: " + encryptedTrns);
        System.out.println("예비 전송 정보 :: " + filedTrns);

        //, gbn, avlYmd, cvc, monthlyPay ,trnsAmt
        String trx ;

        String trx_header ;
        String trx_data ;
        // 카드사에 전송하기 위한 정보 생성
        trx_header ="{\"dataLen\":\""+dataLenth+"\",\"dataGbn\":\""+dataGbn+"\",\"mgntId\":\""+mgntId+"\"";

        trx_data = ",\"cardNoTrns\":\""+cardNoTrns+"\",\"monthlyPayTrns\":\""+monthlyPayTrns+"\"" +
              ",\"avlYmdTrns\":\""+avlYmdTrns+"\",\"cvcTrns\":\""+cvcTrns+"\""+
              ",\"trnsAmtTrns\":\""+trnsAmtTrns+"\",\"vatTrns\":\""+vatTrns+"\""+
              ",\"vatTrns\":\""+vatTrns+"\",\"mgntIdTrns\":\""+mgntIdTrns+"\""+
              ",\"encryptedTrns\":\""+encryptedTrns+"\",\"filedTrns\":\""+filedTrns+"\"}";
            
        trx = trx_header + trx_data;
        
        JSONParser jsonParser1 = new JSONParser();
        JSONObject jsonObject1;
        //System.out.println("여기0");
        jsonObject1 = (JSONObject) jsonParser1.parse(trx);
        //System.out.println("여기1"); 

        // 결재 요청(카드사 통신)
        requestApi.callApi(jsonObject1, "POST");

        
  
        }

}
