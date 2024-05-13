package DTO;

public class OTPUserId {

    public int userId;

    public static int  reserveId ;

    public OTPUserId(int userId) {
        this.userId = userId;
    }

    public OTPUserId() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
        this.reserveId=userId;
    }
    public int getReserveId() {
        return reserveId;
    }

    public void setUserIdNull() {
        this.userId = 0;
        this.reserveId=0;
    }
}
