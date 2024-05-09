package DTO;

public class OTPUserId {

    private int userId;

    private static int  reserveId ;

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
}
