package org.example.constant;

public class ResponseConstant {
    public static String GENERIC_REQUEST_SUCCESS="REQ_SUCCESS";
    public static String SERVER_GENERIC_EXCEPTION_CODE="REQ_FAILED";
    public static String RESOURCE_NOT_FOUND_EXCEPTION_CODE = "USREX404";
    public static String USER_REGISTERED_SUCCESSFUL="USER_REG_SUCCESS";
    public static String USER_REGISTERED_FAILED="REG_FAILED";
    public static String USERNAME_NOT_FOUND="ERR_USERNAME_NOT_FOUND";
    public static String PASSWORD_NOT_FOUND="ERR_PASSWORD_NOT_FOUND";
    public static String USER_LOGIN_FAILED="ERRINVALIDLOGIN";
    public static String USER_DISABLED="ERRDISABLEUSER";
    public static String TOKEN_EXPIRED_OR_SESSION_CLOSED="ERAUTHREQ001";
    public static String PASS_MISMATCH="ERPASS001";
    public static String USERNAME_VALIDATION="ERR_USERNAME_VALID";
    public static String USER_EXISTS="ERR_USER_ALREADY_EXISTS";
    public static String BLANK_EMAIL="ERR_EMAIL_BLANK";
    public static String INVALID_EMAIL="ERR_INVALID_EMAIL";

    /**
     *
     * ORDER MESSAGES
     * ***/

    public static String ERR_DATA_NOT_FOUND = "ERR_DATA_NOT_FOUND";
    public static String ERR_INVALID_INPUT = "ERR_INVALID_INPUT";
}
