package com.towel.login;

import com.towel.role.RoleMember;

public interface User extends RoleMember {
    String getPassword();

    String getUsername();
}
