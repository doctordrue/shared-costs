package org.doctordrue.sharedcosts.telegram.data.repositories;

import java.util.Optional;

import org.doctordrue.sharedcosts.telegram.data.entities.TelegramGroupChatSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramChatSettingsRepository extends JpaRepository<TelegramGroupChatSettings, Long> {

   Optional<TelegramGroupChatSettings> findByGroup_Id(Long id);

}