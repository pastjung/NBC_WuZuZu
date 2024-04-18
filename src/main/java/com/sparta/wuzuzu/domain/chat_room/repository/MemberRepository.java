package com.sparta.wuzuzu.domain.chat_room.repository;

import com.sparta.wuzuzu.domain.chat_room.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
