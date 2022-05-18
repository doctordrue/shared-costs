package org.doctordrue.sharedcosts.telegram.data.repositories;

import org.doctordrue.sharedcosts.telegram.data.entities.TelegramChatSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramChatSettingsRepository extends JpaRepository<TelegramChatSettings, Long> {

}