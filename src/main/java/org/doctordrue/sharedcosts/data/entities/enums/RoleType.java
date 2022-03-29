package org.doctordrue.sharedcosts.data.entities.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author Andrey_Barantsev
 * 3/29/2022
 **/
public enum RoleType implements GrantedAuthority {
   ANONYMOUS,
   USER,
   ADMIN;

   @Override
   public String getAuthority() {
      return "ROLE_" + this.name();
   }
}
