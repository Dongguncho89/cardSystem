
import java.security.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {

    public static void main(String[] args) throws Exception { 
        Main main = new Main(); 
        Payment payment = new Payment();
        encryptDecrypt ed = new encryptDecrypt();
        requestApi http = new requestApi();

        String mgntId ="0000"  ;
        int canclAmt ;

        int cardNo = 0;
        

        String gbn  ;

        int monthlyPay = 0;
        String avlYmd ="0000"  ;
        
        int cvc  = 0;
        int trnsAmt = 0 ;
        //avlYmd ='0000';
        Scanner sc = new Scanner(System.in);
        System.out.print("승인/취소구분을 입력해주세요(승인:PAYMENT , 취소: CANCEL , 조회: SELECT) ");

        gbn = sc.next();

        if (gbn.equals("PAYMENT")) {
            Scanner sc2 = new Scanner(System.in);
		    System.out.print("값을 입력하세요 (카드번호 할부개월 유효일자 CVC 거래금액): ");
        
		    cardNo = sc2.nextInt();
		    monthlyPay = sc2.nextInt();
		    avlYmd = sc2.next();
		    cvc = sc2.nextInt();
		    trnsAmt = sc2.nextInt();
        
            System.out.print("카드번호 :::  " +  cardNo     + "\n");
            System.out.print("승인구분 :::  " +  gbn        + "\n");
            System.out.print("할부개월 :::  " +  monthlyPay + "\n");
            System.out.print("유효일자 :::  " +  avlYmd     + "\n") ;
            System.out.print("cvc     :::  " +  cvc        + "\n" ) ;
            System.out.print("거래금액 :::  " +  trnsAmt    + "\n");
            if (avlYmd.length() != 4) {
                System.out.println("유효일자가 정상적이지 않습니다. 확인해주세요.");
                throw new Exception();
            }
            if (trnsAmt < 100 && trnsAmt > 1000000000) {
                System.out.println("결재금액은 100원 이상, 10억원 이하 만 허용 됩니다. 확인해주세요.");
                throw new Exception();
            }
            //결재API 호출
            Payment.request(cardNo, gbn, avlYmd, cvc, monthlyPay ,trnsAmt); 


        }
        else if (gbn.equals("CANCEL")) {
            Scanner sc3 = new Scanner(System.in);
		    System.out.print("값을 입력하세요 (관리번호 취소금액): ");

            mgntId = sc3.next();
            canclAmt = sc3.nextInt();
            // 취소 API 호출
            Cancel.request(mgntId, canclAmt); 
        }
        else if (gbn.equals("SELECT")) {
            Scanner sc4 = new Scanner(System.in);
		    System.out.print("값을 입력하세요 (관리번호): ");

            mgntId = sc4.next();
            canclAmt = sc4.nextInt();
            // 취소 API 호출
            responseApi.selectCardData(mgntId, canclAmt); 
        }
        else {
            System.out.println("승인구분이 올바르지 않습니다. 확인해주세요.");
            throw new Exception();   
        }



        
        } // HTTP GET request

        public static  void makeHeader (String gbn ) { 
     
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


            mgntId=format_time1+rand_st;

            System.out.println("dataLenth :: "+dataLenth);
            System.out.println("dataGbn   :: "+dataGbn);
            System.out.println("mgntId :: "+mgntId);

           // return  mgntId;
            //mgntId = 
        }
}
