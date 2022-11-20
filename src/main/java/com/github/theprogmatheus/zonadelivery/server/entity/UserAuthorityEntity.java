package com.github.theprogmatheus.zonadelivery.server.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "authorities")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthorityEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, unique = true)
	private String authority;

	@ManyToMany(mappedBy = "authorities")
	private List<UserEntity> users;

}
