package com.coderscampus.contractprogramming;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coderscampus.contractprogramming.domain.Client;

public interface ClientRepository extends JpaRepository<Client, Integer>
{

}
