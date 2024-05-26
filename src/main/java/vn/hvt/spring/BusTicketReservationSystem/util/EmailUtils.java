package vn.hvt.spring.BusTicketReservationSystem.util;

public class EmailUtils {
    public static String getVerificationUrl(String phoneNumber, String token) {
        return "http://localhost:8080/register/verify-account?phonenumber="+phoneNumber+"&token="+token;
    }

}
