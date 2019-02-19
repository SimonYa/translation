package org.lwj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.lwj.vo.UpdatePassVo;
import org.lwj.vo.UserVo;

@Mapper
public interface UserMapper {
	public List<UserVo> selectAll();
	
	public UserVo selectUser(@Param("mobile") String mobile);

	public int insertUser(UserVo userVo);

	public int checkMobile(@Param("mobile") String mobile);

	public int login(@Param("mobile") String mobile, @Param("password") String password);

	public int updatePassword(UpdatePassVo updatePassVo);
}
