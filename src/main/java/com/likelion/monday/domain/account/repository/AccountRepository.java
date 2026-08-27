package com.likelion.monday.domain.account.repository;

import com.likelion.monday.domain.account.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);
}
