package com.voika.uploadfile.infrastructure.utils;

public class EmailUtil {

    public static boolean validateEmailFormat(String email) {
        if (StringUtil.isEmpty(email)) {
            return false;
        }
        if (!email.contains("@")) {
            return false;
        }
        if (!email.contains(".")) {
            return false;
        }
        if (email.indexOf("@") != email.lastIndexOf("@")) {
            return false;
        }
        if (email.indexOf(".") != email.lastIndexOf(".")) {
            return false;
        }
        if (email.endsWith("@") || email.startsWith("@")) {
            return false;
        }
        if (email.endsWith(".") || email.startsWith(".")) {
            return false;
        }
        if (email.replaceAll("[!,#,$,%,^,&,*,(,),_,+,\\-,=,！,#,¥,%,……,&,*,（,）,—,——,+,=]","").length() != email.length()) {
            return false;
        }
        return true;
    }

}
