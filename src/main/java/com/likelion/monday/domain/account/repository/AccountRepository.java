package com.likelion.monday.domain.account.repository;

import com.likelion.monday.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
