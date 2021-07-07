1. 개발환경 : Visual Studio Code
2. 개발언어 : JAVA (Open jdk-16-0.1)
3. DB: 파일DB(SQLite) 를 사용
/* 카드정보를 저장합니다. */
create table CARD_DATA (
MGNT_ID CHAR(20) NOT NULL,
CARD_NO int NOT NULL,
MONTHLY_PAY INT  NOT NULL,
AVL_END_YMD CHAR(4)  NOT NULL ,
CVC INT  NOT NULL,
TRNS_AMT int,
VAT int,
CANCL_AMT int,
CANCL_VAT int,
TRNS_MGNT_NO CHAR(20),
PASSWORD_CARD_INFO VARCHAR2(300),
ADD_FIELD VARCHAR2(47),
TRT_STS VARCHAR2(10),
EMP_NO VARCHAR2(10),
DATA_REG_TM TIMESTAMP,
DATA_UPD_TM TIMESTAMP,
PRIMARY KEY (MGNT_ID, CARD_NO, MONTHLY_PAY, AVL_END_YMD, CVC)
)

;
/* 암호화 정보를 저장합니다 */
create table ENCDEC_INFO (
ENCRYPT VARCHAR2(300) ,
KEY_DAT    VARCHAR2(300) 
)

;
/* 카드사와 통신 정보를 저장합니다 */
create table TRX_INFO (
TRX_ID CHAR(20) ,
HEADER CHAR(34) ,
DATA CHAR(416) ,
DATA_REG_TM TIMESTAMP
)

4. 프로그램 동작
1) Main.java 를 통해 실행
2) 구분값을 입력합니다(승인: PAYMENT,  취소: CANCEL , 조회: SELECT)
3) 승인의 경우
    3-1) 카드정보, 할부개월, 유효일자, CVC, 거래금액을 입력
    3-2) 관리번호 생성 후, 전송정보 객체 생성
    3-3) 응답이 오면, DB에 저장(CARD_DATA, TRX_ID, ENCDEC_INFO) 

4) 취소의 경우
    4-1) 관리번호, 취소금액을 입력
    4-2) 관리번호로 저장된 카드정보 데이터를 조회
    4-3) 전송정보를 생성 후, DB 저장(CARD_DATA, TRX_ID)

5) 조회의 경우
    5-1) 관리번호, 0 을 입력
    5-2) CARD_DATA 정보를 조회

5. 제약조건 동작
1) 유효일자 체크(MMYY)
2) 카드번호 자리수 체크(10자리 미만/ 16자리 초과)
3) 결재금액이 100원 미만, 10억원 초과 여부 체크
4) 취소시에는 이미 취소된 경우 , 처리불가

6. 문제해결 전략 및 고민
1) 개발환경 구축 및 DB 생성을 위해서 최초 많은 고민이 되었습니다.
2) 암복호화 전략 수립
