package com.helpshift.contactsearch.util;

/**
 * Created by milap.wadhwa.
 */
public class ContactSearchUtil {

    public static boolean isNullOrEmptyString(String str)
    {
        return str == null || str.isEmpty();
    }

    public static boolean isNotNullOrEmptyString(String str)
    {
        return !isNullOrEmptyString(str);
    }

}
