package org.lwj.controller;

import java.util.List;
import java.util.UUID;

import org.lwj.base.Response;
import org.lwj.service.UserService;
import org.lwj.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {
	@Autowired
	private UserService userService;

	// 查询全部
	@GetMapping("/selectAll")
	public Response selectAll() {
		Response response = new Response();
		List<UserVo> list = userService.selectAll();
		response.setDataList(list);
		return response;
	}

	// 注册
	@PostMapping("/insertUser")
	public Response insertUser(@RequestBody UserVo userVo) {
		Response response = new Response();
		if (userVo != null) {
			String id = UUID.randomUUID().toString();
			userVo.setId(id);
			int num = userService.insertUser(userVo);
			if (num > 0) {
				response.setCode("200");
				response.setMessage("ok");
			} else {
				response.setCode("600");
				response.setMessage("注册失败");
			}
		} else {
			response.setCode("600");
			response.setMessage("未知错误，请联系管理员");
		}

		return response;
	}

	// 检查电话号码是否存在
	@GetMapping("/checkMobile")
	public Response checkMobile(String mobile) {
		Response response = new Response();

		if (mobile != null && mobile.length() != 0) {
			int num = userService.checkMobile(mobile);
			if (num == 0) {
				response.setCode("200");
				response.setCode("ok");
			} else {
				response.setCode("600");
				response.setCode("电话号码已存在");
			}
		} else {
			response.setCode("700");
			response.setMessage("电话号码为空");
		}

		return response;
	}

	// 登录
	@PostMapping("/login")
	public Response login(String mobile, String password) {
		Response response = new Response();
		if (mobile != null && password != null && mobile.length() != 0 && password.length() != 0) {
			int num = userService.login(mobile, password);
			if (num == 0) {
				response.setCode("600");
				response.setMessage("账号或密码错误");
			} else {
				response.setCode("200");
				response.setMessage("ok");
			}
		} else {
			response.setCode("600");
			response.setMessage("请正确填写账号和密码");
		}
		return response;
	}
}
