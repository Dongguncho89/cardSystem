import java.io.UnsupportedEncodingException;
import java.security.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Cancel {
    public static int cardNo;
    public static  String gbn;
    public static  String avlYmd;
    public static   int cvc;
    public static    int monthlyPay ;
    public static    int trnsAmt;
    public static   int vat;
    public static  String encrypted; 

    public static  void request (String mgntId ,int canclAmt ) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, ParseException {
        
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String dbFileUrl = "jdbc:sqlite:test.db";

        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("dbFileUrl ::: " + dbFileUrl);
            con = DriverManager.getConnection(dbFileUrl);
            System.out.println("Sqlite conn!!");

            stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT MGNT_ID , CARD_NO , MONTHLY_PAY , AVL_END_YMD , CVC, TRNS_AMT , VAT , TRNS_MGNT_NO , PASSWORD_CARD_INFO, TRT_STS FROM CARD_DATA "+
                                   "WHERE 1=1 " +
            // "WHERE 1=1 "+
                                  "AND MGNT_ID ='"+mgntId+"'"
            //        "AND TRT_STS='PAYMENT'"+ 
            //      "AND TRNS_AMT = "+CanclAmt+""
                   )
            ;
            

            System.out.println(rs);

            //String mgntId = rs.getString("MGNT_ID");
            cardNo = rs.getInt("CARD_NO");
            monthlyPay = 0 ; //rs.getInt("MONTHLY_PAY");
            avlYmd = rs.getString("AVL_END_YMD");

            cvc = rs.getInt("CVC");

            trnsAmt = rs.getInt("TRNS_AMT");
            vat = rs.getInt("VAT");

            encrypted = rs.getString("PASSWORD_CARD_INFO"); 

            gbn="CANCEL";

            //System.out.println("조회결과 ID:: "+id);
           
        }
        catch(Exception e) {
            System.out.println(e);
        }
        responseApi.selectCardData(mgntId, canclAmt);
        
     /*** 헤더를 생성한다 (데이터길이|데이터구분|관리번호) */
        String dataLenth="446" ;
        String dataGbn ;
        //mgntId ;

            
        dataLenth=String.format("%4s", dataLenth);
        dataGbn = String.format("%-10s", gbn); 

        
/*
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
*/
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
        vatTrns = String.format("%10d", vat); 
        
        String mgntIdTrns = " ";
        
        if (gbn.equals("CANCEL") ){

            mgntIdTrns = mgntId;

        }
        String password ;

       

        int encryplen =0 ;
        encryplen = encrypted.length();

        System.out.println("encryplen 정보 :: " + encryplen);


        //encryptedTrns = encrypted.substring(1,300);

        String encryptedTrns = String.format("%-300s", encrypted); 

        //String decrypted = encryptDecrypt.decryptRSA(encrypted,privateKey);



        System.out.println("암호화 정보 :: " + encryptedTrns);
        
        //String de_pass = encryptDecrypt.SHA512(pw_pass, salt);

        //System.out.println("복호화 정보 :: " + decrypted);
        
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

        trx_header ="{\"dataLen\":\""+dataLenth+"\",\"dataGbn\":\""+dataGbn+"\",\"mgntId\":\""+mgntId+"\"";

        trx_data = ",\"cardNoTrns\":\""+cardNoTrns+"\",\"monthlyPayTrns\":\""+monthlyPayTrns+"\"" +
              ",\"avlYmdTrns\":\""+avlYmdTrns+"\",\"cvcTrns\":\""+cvcTrns+"\""+
              ",\"trnsAmtTrns\":\""+trnsAmtTrns+"\",\"vatTrns\":\""+vatTrns+"\""+
              ",\"vatTrns\":\""+vatTrns+"\",\"mgntIdTrns\":\""+mgntIdTrns+"\""+
              ",\"encryptedTrns\":\""+encryptedTrns+"\",\"filedTrns\":\""+filedTrns+"\"}";
            
        trx = trx_header + trx_data;
        
        JSONParser jsonParser1 = new JSONParser();
        JSONObject jsonObject1;
        System.out.println("여기0");
        jsonObject1 = (JSONObject) jsonParser1.parse(trx);
        System.out.println("여기1"); 

        // 결재 요청
        requestApi.callApi(jsonObject1, "POST");

        
  
        }

}
