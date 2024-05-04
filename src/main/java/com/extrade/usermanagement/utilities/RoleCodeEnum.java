package com.extrade.usermanagement.utilities;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RoleCodeEnum {
    CUSTOMER("C"),
    TECHNICIAN("T"),
    STORE_ADMIN("SA"),
    CSR("CSR");

    private final String name;

    @Override
    public String toString() {
        return this.name;
    }
}
