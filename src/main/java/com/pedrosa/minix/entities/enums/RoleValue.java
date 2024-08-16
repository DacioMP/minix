package com.pedrosa.minix.entities.enums;

public enum RoleValue {

    ADMIN(1L),

    BASIC(2L);

    private final long roleId;

    RoleValue(long roleId) {
        this.roleId = roleId;
    }

    public long getRoleId() {
        return roleId;
    }
}

