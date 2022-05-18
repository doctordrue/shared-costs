package org.doctordrue.sharedcosts.telegram.bot;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.doctordrue.sharedcosts.telegram.data.entities.BotStatus;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * @author Andrey_Barantsev
 * 5/17/2022
 **/
public class UserSession {

   private final Long userId;
   private BotStatus status;

   public UserSession(long userId) {
      this.userId = userId;
      this.status = BotStatus.IDLE;
   }

   public BotStatus getStatus() {
      return status;
   }

   public UserSession setStatus(BotStatus status) {
      this.status = status;
      return this;
   }

   public Long getUserId() {
      return userId;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;

      if (o == null || getClass() != o.getClass())
         return false;

      UserSession that = (UserSession) o;

      return new EqualsBuilder().append(getUserId(), that.getUserId()).isEquals();
   }

   @Override
   public int hashCode() {
      return new HashCodeBuilder(17, 37).append(getUserId()).toHashCode();
   }

   public static UserSession newSession(User user) {
      return new UserSession(user.getId()).setStatus(BotStatus.BEFORE_START);
   }
}
