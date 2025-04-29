package com.lambdacode.creditscoreanalysiswebapplication.Constant;

public class AuthorizeConstant {
    public static final String ROLE_STAFF = "ROLE_LIBRARIAN";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String HAS_ROLE_STAFF_OR_ADMIN = "hasAnyAuthority('" + ROLE_STAFF + "', '" + ROLE_ADMIN + "')";
    public static final String HAS_ROLE_ADMIN = "hasAuthority('" + ROLE_ADMIN + "')";
    public static final String HAS_ROLE_STAFF = "hasAuthority('" + ROLE_STAFF + "')";
    public static final String HAS_ROLE_USER = "hasAuthority('" + ROLE_USER + "')";
    public static final String HAS_ROLE_STAFF_OR_ADMIN_OR_USER = "hasAnyAuthority('" + ROLE_STAFF + "', '" + ROLE_ADMIN + "','" + ROLE_USER +"')";
    public static final String HAS_ROLE_USER_OR_STAFF = "hasAuthority('" + ROLE_USER + "','" + ROLE_STAFF + "')";
}
