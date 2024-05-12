package com.extrade.usermanagement.utilities;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserAccountStatusEnum {
    ACTIVE("A"),
    REGISTERED("R"),
    LOCKED("L"),
    DISABLED("D");

    private final String name;

    public String toString(){
        return this.name;
    }

//    public static void main(String[] args) {
//        System.out.println(UserAccountStatusEnum.ACTIVE.toString());
    }


