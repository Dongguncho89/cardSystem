
import java.sql.*;
import org.json.simple.JSONObject;

public class responseApi {

    public static Connection con = null;
    public static Statement stmt = null;
    public static Statement stmt2 = null;
    public static ResultSet rs = null;
    public static String dbFileUrl = "jdbc:sqlite:test.db";

    
    public static void response(JSONObject params){

        // 카드사로 부터 받은 데이터          
        JSONObject json  = new JSONObject(params);

        String dataLen = (String) json.get("dataLen");// ("cardNo");
        String dataGbn = (String) json.get("dataGbn");// ("cardNo");
        String mgntId = (String) json.get("mgntId");// ("cardNo");

        String header = dataLen+dataGbn+mgntId;
        System.out.println("HEADER 정보:: "+ header);


        String cardNoTrns = (String) json.get("cardNoTrns");// ("cardNo");
        String monthlyPayTrns = (String) json.get("monthlyPayTrns");// ("cardNo");
        String avlYmdTrns = (String) json.get("avlYmdTrns");// ("cardNo");
        String cvcTrns = (String) json.get("cvcTrns");// ("cardNo");
        
        String trnsAmtTrns = (String) json.get("trnsAmtTrns");// ("cardNo");
        String vatTrns = (String) json.get("vatTrns");// ("cardNo");
        String mgntIdTrns = (String) json.get("mgntIdTrns");// ("cardNo");
        String encryptedTrns = (String) json.get("encryptedTrns");// ("cardNo"); 
        String filedTrns = (String) json.get("filedTrns");// ("cardNo");  

        String data = cardNoTrns+monthlyPayTrns+avlYmdTrns+cvcTrns+trnsAmtTrns+vatTrns+mgntIdTrns+encryptedTrns+filedTrns;

        // 카드사와의 통신 정보를 저장
        trxInfoDb(mgntId, header, data);

        // 결재정보를 저장
        cardDataDb(params, mgntId);

    }

    public static void trxInfoDb (String mgntId, String header, String data){

        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection(dbFileUrl);
            System.out.println("Sqlite conn!!");

            stmt = con.createStatement();
            // 카드사와의 통신정보를 저장.
            rs = stmt.executeQuery("INSERT INTO TRX_INFO VALUES ('"+mgntId+"','"+header+"','"+data+"',DATETIME('now'))");
            //rs = stmt.executeQuery("INSERT INTO TRX_INFO VALUES ("+mgntId+","+header+","+data+",DATETIME('now')");
            //System.out.println(rs);
           
        }
        catch(Exception e) {
            System.out.println(e);
        }
    
    }
    public static void cardDataDb (JSONObject cardDataJson, String mgntId){

        JSONObject json  = new JSONObject(cardDataJson);
        mgntId = (String) json.get("mgntId");// ("cardNo");

        String dataGbn = (String) json.get("dataGbn");// ("cardNo");
        dataGbn = dataGbn.replaceAll(" ", "");

        String    cardNo  = (String) json.get("cardNoTrns");// ("cardNo");
        cardNo = cardNo.replaceAll(" ", "");
        Long cardNoInt = Long.parseLong(cardNo);

        String   monthlyPay = (String) json.get("monthlyPayTrns");// ("cardNo");
        monthlyPay = monthlyPay.replaceAll(" ", "");
        int monthlyPayInt = Integer.parseInt(monthlyPay);

        String avlYmd = (String) json.get("avlYmdTrns");// ("cardNo");

        String cvc = (String) json.get("cvcTrns");// ("cardNo");
        cvc = cvc.replaceAll(" ", "");
        int cvcInt = Integer.parseInt(cvc);

        
        String trnsAmtTrns = (String) json.get("trnsAmtTrns");// ("cardNo");
        trnsAmtTrns = trnsAmtTrns.replaceAll(" ", "");
        int trnsAmtInt = Integer.parseInt(trnsAmtTrns);  
        
        String vatTrns = (String) json.get("vatTrns");// ("cardNo");
        vatTrns = vatTrns.replaceAll(" ", "");
        int vatInt = Integer.parseInt(vatTrns);
        
        String mgntIdTrns = (String) json.get("mgntIdTrns");// ("cardNo");
        String encryptedTrns = (String) json.get("encryptedTrns");// ("cardNo"); 
       // String filedTrns = (String) json.get("filedTrns");// ("cardNo"); 

        System.out.println("mgntId ::: "+ mgntId);

        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection(dbFileUrl);
            System.out.println("Sqlite conn!!");
            
            //SQLiteConnection con = new SQLiteConnection(String.format(Data Source = {0};Pragma synchronous=off;Pragma auto_vacuum = 1;", dbFileUrl));


            stmt = con.createStatement();
            // 결재정보를 저장할 쿼리 생성
            String query = ("INSERT INTO CARD_DATA VALUES ('"+mgntId+"',"+cardNoInt+","+monthlyPayInt+",'"+avlYmd+"',"+cvcInt+
             ","+trnsAmtInt+","+vatInt+",0,0,'"+mgntIdTrns+"','"+encryptedTrns+
             "','','"+dataGbn+"','BATCH',DATETIME('now'),DATETIME('now') )");
            System.out.println("query :::: " +query);
            try {
            if (dataGbn.equals("PAYMENT")) {
      
                rs = stmt.executeQuery(query);
                rs = stmt.executeQuery("COMMIT;");
                rs.close();          
            
            }
            else if (dataGbn.equals("CANCEL")) {
                // 취소의 경우는, cancl_amt, calncl_vat, trt_sts 업데이트 처리
                rs = stmt.executeQuery("UPDATE CARD_DATA SET CANCL_AMT = "+trnsAmtInt+
                " , CANCL_VAT= "+vatInt+" , TRNS_MGNT_NO= '"+mgntIdTrns+"' , TRT_STS='CANCEL', DATA_UPD_TM = DATETIME('now') WHERE 1=1 AND MGNT_ID = '"+mgntId+"'" );
                rs = stmt.executeQuery("COMMIT;");
                rs.close();
            }
            }
            catch(SQLException e) {
                System.out.println(e);
                throw new SQLException(e);

            }
        
           
        }
        catch(Exception e) {
            System.out.println(e);
        }
    
    }

    public static void selectCardData (String mgntId , int canclAmt ){

        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("dbFileUrl ::: " + dbFileUrl);
            con = DriverManager.getConnection(dbFileUrl);
            System.out.println("Sqlite conn!!");

            stmt = con.createStatement();
            // 조회 처리
            rs = stmt.executeQuery("SELECT TRNS_AMT , VAT , TRT_STS, PASSWORD_CARD_INFO , CANCL_AMT, CANCL_VAT FROM CARD_DATA "+
            "WHERE 1=1 " +
            "AND MGNT_ID ='"+mgntId+"'"
            )
            ;
            

            //System.out.println(rs);

            int trnsAmt = rs.getInt("TRNS_AMT");
            int vat     = rs.getInt("VAT");
            int canclAmtT = rs.getInt("CANCL_AMT");
            int canclVat = rs.getInt("CANCL_VAT");

            String info =  rs.getString("PASSWORD_CARD_INFO");
            info = info.replaceAll(" ", "");
            String status =  rs.getString("TRT_STS");

            if(status.equals("CANCEL") && canclAmt!=0) {
                System.out.println("이미 취소된 정보입니다. 처리불가합니다. ::" + mgntId);
                throw new Exception();
            }

            rs = stmt.executeQuery("SELECT KEY_DAT FROM ENCDEC_INFO WHERE 1=1 AND ENCRYPT= '"+info+"'");
            String privateSt = rs.getString("KEY_DAT");
            
            //privateSt = privateSt.replaceAll(" ", "");
            System.out.println("조회결과 privateSt:: "+privateSt);
            //PrivateKey privateKey = encryptDecrypt.getPrivateKeyFromBase64String(privateSt);
            //PrivateKey privateKey = rs.getObject("KEY_DAT");//rs.getString("KEY_DAT") ;
             System.out.println("조회결과 222 ");
    
    
        String decrypted = encryptDecrypt.decode(info,privateSt);
            //System.out.println("조회결과 info:: "+info);
            
            System.out.println("조회결과 카드정보:: \n"+decrypted);
            System.out.println("조회결과 처리상태:: \n"+status);
            System.out.println("조회결과 처리금액:: "+trnsAmt+" 부가세:: "+vat);
            System.out.println("조회결과 취소금액:: "+canclAmtT+" 취소부가세:: "+canclVat);
            
           
        }
        catch(Exception e) {
            System.out.println(e);
        }
    
    }
}   
