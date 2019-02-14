package org.lwj.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.lwj.vo.UserVo;

public interface UserService {
	public List<UserVo> selectAll();
	
	public int insertUser(UserVo userVo);
	
	public int checkMobile(String mobile);
	
	public int login(@Param("mobile") String mobile, @Param("password") String password);
}
