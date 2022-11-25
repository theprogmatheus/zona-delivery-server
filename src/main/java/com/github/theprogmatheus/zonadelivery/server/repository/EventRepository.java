package com.github.theprogmatheus.zonadelivery.server.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.theprogmatheus.zonadelivery.server.entity.EventEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, UUID> {

}
