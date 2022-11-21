package com.github.theprogmatheus.zonadelivery.server.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.theprogmatheus.zonadelivery.server.entity.UserAuthorityEntity;

public interface UserAuthorityRepository extends JpaRepository<UserAuthorityEntity, UUID> {

}
