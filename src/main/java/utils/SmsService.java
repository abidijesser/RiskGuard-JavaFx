package utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsService {
    // Twilio Account SID and Auth Token
    public static final String ACCOUNT_SID = "ACd09f528a536eea7e43d61455cbddbd57";
    public static final String AUTH_TOKEN = "f90b91d5978124ee5c3b49538dac44cb";
    public static final String FROM_NUMBER = "+12766922769";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public static void sendSms(String to, String message) {
        Message sms = Message.creator(
                new PhoneNumber(to),  // To number
                new PhoneNumber(FROM_NUMBER),  // From Twilio number
                message
        ).create();
        System.out.println("SMS sent successfully! SID: " + sms.getSid());
    }
}
