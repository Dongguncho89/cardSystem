

import java.util.*;



public class Main {

    public static void main(String[] args) throws Exception { 

        String mgntId ="0000"  ;
        int canclAmt ;

        long cardNo = 0;

        String gbn  ;

        int monthlyPay = 0;
        String avlYmd ="0000"  ;
        
        int cvc  = 0;
        int trnsAmt = 0 ;
        //avlYmd ='0000';
        Scanner sc = new Scanner(System.in);
        System.out.print("승인/취소구분을 입력해주세요(승인:PAYMENT , 취소: CANCEL , 조회: SELECT) ");

        gbn = sc.next();
        
        // 결재인 경우
        if (gbn.equals("PAYMENT")) {
            Scanner sc2 = new Scanner(System.in);
		    System.out.print("값을 입력하세요 (카드번호 할부개월 유효일자 CVC 거래금액): ");
        
		    cardNo = sc2.nextLong();
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
                sc2.close();
                throw new Exception();

            }
            String cardChk = Long.toString(cardNo);

            System.out.println("카드번호는 10자리 이상, 16자리 이하입니다. 확인해주세요." + cardChk.length());

            //int length = (int)(Math.log10(cardNo)+1);
            if (cardChk.length()< 10 || cardChk.length() > 16) {
                System.out.println("카드번호는 10자리 이상, 16자리 이하입니다. 확인해주세요.");
                sc2.close();
                throw new Exception();
            }
            if (trnsAmt < 100 && trnsAmt > 1000000000) {
                System.out.println("결재금액은 100원 이상, 10억원 이하 만 허용 됩니다. 확인해주세요.");
                 sc2.close();
                throw new Exception();
            }
            //결재API 호출
            Payment.request(cardNo, gbn, avlYmd, cvc, monthlyPay ,trnsAmt); 
            sc2.close();


        }
        else if (gbn.equals("CANCEL")) {
            Scanner sc3 = new Scanner(System.in);
		    System.out.print("값을 입력하세요 (관리번호 취소금액): ");

            mgntId = sc3.next();
            canclAmt = sc3.nextInt();
            // 취소 API 호출
            Cancel.request(mgntId, canclAmt); 
            sc3.close();
        }
        else if (gbn.equals("SELECT")) {
            Scanner sc4 = new Scanner(System.in);
		    System.out.print("값을 입력하세요 (관리번호, 0): ");

            mgntId = sc4.next();
            canclAmt = sc4.nextInt();
            // 조회 API 호출
            responseApi.selectCardData(mgntId, canclAmt); 
            sc4.close();
        }
        else {
            System.out.println("승인구분이 올바르지 않습니다. 확인해주세요.");
            sc.close();
            throw new Exception();   
        }

        sc.close();

        
        } // HTTP GET request

        
}
