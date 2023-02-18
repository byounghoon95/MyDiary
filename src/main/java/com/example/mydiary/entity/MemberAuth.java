package com.example.mydiary.entity;

import java.util.HashMap;
import java.util.Map;

public enum  MemberAuth {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ;

    private final String abbreviation;

    private static final Map<String,MemberAuth> lookup = new HashMap<>();

    /* 초기화 블럭 */
    static {
        for(MemberAuth auth : MemberAuth.values()) {
            lookup.put(auth.abbreviation,auth);
        }
        System.out.println("lookup : " + lookup);
    }

    MemberAuth(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return this.abbreviation;
    }

    public static MemberAuth get(String abbreviation) {
        return lookup.get(abbreviation);
    }

    public static boolean containsKey(String abbreviation) {
        return lookup.containsKey(abbreviation);
    }

}