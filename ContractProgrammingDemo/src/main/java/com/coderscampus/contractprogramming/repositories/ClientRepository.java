package com.coderscampus.contractprogramming.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coderscampus.contractprogramming.domain.Client;

public interface ClientRepository extends JpaRepository<Client, Integer>
{

  Set<Client> findByEmail(String email);

}
