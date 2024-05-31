package com.samanecorporation.security.dao;

import java.util.Optional;

import com.samanecorporation.security.entity.UserEntity;

public interface IUserDao extends Repository<UserEntity> {

	public Optional<UserEntity> login (String email, String password);
}
