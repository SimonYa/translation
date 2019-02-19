package org.lwj.service.impl;

import java.util.List;

import org.lwj.mapper.UserMapper;
import org.lwj.service.UserService;
import org.lwj.vo.UpdatePassVo;
import org.lwj.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public List<UserVo> selectAll() {
		// TODO Auto-generated method stub
		return userMapper.selectAll();
	}

	@Override
	public int insertUser(UserVo userVo) {
		// TODO Auto-generated method stub
		int num = userMapper.insertUser(userVo);
		return num;
	}

	@Override
	public int checkMobile(String mobile) {
		// TODO Auto-generated method stub
		return userMapper.checkMobile(mobile);
	}

	@Override
	public int login(String mobile, String password) {
		// TODO Auto-generated method stub
		return userMapper.login(mobile, password);
	}

	@Override
	public int updatePassword(UpdatePassVo updatePassVo) {
		// TODO Auto-generated method stub
		return userMapper.updatePassword(updatePassVo);
	}

	@Override
	public UserVo selectUser(String mobile) {
		// TODO Auto-generated method stub
		return userMapper.selectUser(mobile);
	}
}
