package com.pedrosa.minix.entities;

import com.pedrosa.minix.entities.enums.RoleValue;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_role")
public class Role implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    private String name;

    public Role() {}

    public Role(RoleValue roleValue, String name) {
        setRoleId(roleValue);
        this.name = name;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(RoleValue roleValue) {
        this.roleId = roleValue.getRoleId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(roleId, role.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(roleId);
    }
}
