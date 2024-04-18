package com.sparta.wuzuzu.domain.message.repository;

import com.sparta.wuzuzu.domain.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositoryCustom {

}
