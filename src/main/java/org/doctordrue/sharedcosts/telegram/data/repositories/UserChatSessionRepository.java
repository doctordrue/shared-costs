package org.doctordrue.sharedcosts.telegram.data.repositories;

import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChatSessionRepository extends JpaRepository<UserChatSession, Long> {

}