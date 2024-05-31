package com.samanecorporation.security.service;

import java.util.List;
import java.util.Optional;

import com.samanecorporation.security.dao.IUserDao;
import com.samanecorporation.security.dao.UserDao;
import com.samanecorporation.security.dto.UserDto;
import com.samanecorporation.security.entity.UserEntity;
import com.samanecorporation.security.mapper.UserMapper;

public class UserService implements IUserService {

	private IUserDao userDao = new UserDao();
	
	@Override
	public List<UserDto> getAll() {
	
		return UserMapper.listUserEntityToListUserDto(userDao.list(new UserEntity()));
	}

	@Override
	public boolean save(UserDto userDto) {
		
		return userDao.save(UserMapper.toUserEtity(userDto));
	}

	@Override
	public Optional<UserDto> login(String email, String password) {
		Optional<UserEntity> userEntity = userDao.login(email, password);
		
		if (userEntity.isPresent()) {
			UserEntity user = userEntity.get();
			return Optional.of(UserMapper.toUserDto(user));
		} else {
			return Optional.empty();
		}
	}

}
