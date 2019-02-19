package org.lwj.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.lwj.vo.UpdatePassVo;
import org.lwj.vo.UserVo;

public interface UserService {
	public List<UserVo> selectAll();

	public UserVo selectUser(String mobile);

	public int insertUser(UserVo userVo);

	public int checkMobile(String mobile);

	public int login(String mobile, String password);

	public int updatePassword(UpdatePassVo updatePassVo);
}
