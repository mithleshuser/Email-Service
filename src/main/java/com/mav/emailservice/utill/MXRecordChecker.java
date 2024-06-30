package com.mav.emailservice.utill;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class MXRecordChecker {
    public static boolean hasMXRecords(String domain) {
        try {
            DirContext context = new InitialDirContext();
            Attributes attributes = context.getAttributes("dns:///" + domain, new String[]{"MX"});
            Attribute mxRecords = attributes.get("MX");
            return mxRecords != null && mxRecords.size() > 0;
        } catch (NamingException e) {
            return false;
        }
    }
}