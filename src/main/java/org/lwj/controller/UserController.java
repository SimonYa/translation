package org.lwj.controller;

import java.util.List;
import java.util.UUID;

import org.lwj.base.Response;
import org.lwj.service.UserService;
import org.lwj.vo.CaptchaResultVo;
import org.lwj.vo.InsertUserVo;
import org.lwj.vo.UpdatePassVo;
import org.lwj.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;

@RestController
@RequestMapping(value = "/user")
public class UserController {
	@Autowired
	private UserService userService;

	private static String verifyCode = "";
	private static String verifyMobile = "";

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
	public Response insertUser(@RequestBody InsertUserVo insertUserVo) {
		Response response = new Response();

		UserVo userVo;
		if (insertUserVo != null) {
			userVo = new UserVo();
			if (verifyCode == null || verifyCode.length() == 0 || !verifyCode.equals(insertUserVo.getValidateCode())
					|| !verifyMobile.equals(insertUserVo.getMobile())) {
				response.setCode("600");
				response.setMessage("验证码不正确");
				return response;
			}

			userVo.setMobile(insertUserVo.getMobile());
			userVo.setName(insertUserVo.getName());
			userVo.setPassword(insertUserVo.getPassword());
		} else {
			response.setCode("600");
			response.setMessage("参数错误，注册失败");
			return response;
		}

		String id = UUID.randomUUID().toString();
		userVo.setId(id);
		int num = userService.insertUser(userVo);
		userVo.setHeadImg(
				"http://bhms-fru-dev.oss-cn-shenzhen.aliyuncs.com/images/8c5a89ce668440f9b25fce8337f704c8.png");
		userVo.setVip(0);
		if (num > 0) {
			response.setCode("200");
			response.setMessage("ok");
			response.setData(userVo);
		} else {
			response.setCode("600");
			response.setMessage("注册失败");
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
				response.setMessage("电话号码不存在");
			} else {
				response.setCode("600");
				response.setMessage("电话号码已存在");
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
				UserVo userVo = userService.selectUser(mobile);
				response.setCode("200");
				response.setMessage("ok");
				response.setData(userVo);
			}
		} else {
			response.setCode("600");
			response.setMessage("请正确填写账号和密码");
		}
		return response;
	}

	// 短信验证码
	public static String calcAuthorization(String source, String secretId, String secretKey, String datetime)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
		String signStr = "x-date: " + datetime + "\n" + "x-source: " + source;
		Mac mac = Mac.getInstance("HmacSHA1");
		Key sKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), mac.getAlgorithm());
		mac.init(sKey);
		byte[] hash = mac.doFinal(signStr.getBytes("UTF-8"));
		String sig = new BASE64Encoder().encode(hash);

		String auth = "hmac id=\"" + secretId + "\", algorithm=\"hmac-sha1\", headers=\"x-date x-source\", signature=\""
				+ sig + "\"";
		return auth;
	}

	public static String urlencode(Map<?, ?> map) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			if (sb.length() > 0) {
				sb.append("&");
			}
			sb.append(String.format("%s=%s", URLEncoder.encode(entry.getKey().toString(), "UTF-8"),
					URLEncoder.encode(entry.getValue().toString(), "UTF-8")));
		}
		return sb.toString();
	}

	@GetMapping("/captcha")
	public Response captcha(String mobile)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
		Response response = new Response();
		verifyMobile = mobile;
		// 云市场分配的密钥Id
		String secretId = "AKIDGq7dtE5JnHjG2Gxi7mMrt7WQqxlg3bEkO6f0";
		// 云市场分配的密钥Key
		String secretKey = "4SuK1Gmgab59u1a2bMQG6m7thxvoAl1K4cGR5DCi";
		String source = "market";

		Calendar cd = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		String datetime = sdf.format(cd.getTime());
		// 签名
		String auth = calcAuthorization(source, secretId, secretKey, datetime);
		// 请求方法
		String method = "GET";
		// 请求头
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("X-Source", source);
		headers.put("X-Date", datetime);
		headers.put("Authorization", auth);

		// 查询参数
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("mobile", mobile);
		verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
		queryParams.put("param", "code:" + verifyCode);
		queryParams.put("tpl_id", "TP1801042");
		// body参数
		Map<String, String> bodyParams = new HashMap<String, String>();

		// url参数拼接
		String url = "http://service-4xrmju6b-1255399658.ap-beijing.apigateway.myqcloud.com/release/dxsms";
		if (!queryParams.isEmpty()) {
			url += "?" + urlencode(queryParams);
		}

		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			conn.setRequestMethod(method);

			// request headers
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}

			// request body
			Map<String, Boolean> methods = new HashMap<>();
			methods.put("POST", true);
			methods.put("PUT", true);
			methods.put("PATCH", true);
			Boolean hasBody = methods.get(method);
			if (hasBody != null) {
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

				conn.setDoOutput(true);
				DataOutputStream out = new DataOutputStream(conn.getOutputStream());
				out.writeBytes(urlencode(bodyParams));
				out.flush();
				out.close();
			}

			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			String result = "";
			while ((line = in.readLine()) != null) {
				result += line;
			}

			CaptchaResultVo captchaResultVo = (CaptchaResultVo) JSON.parseObject(result, CaptchaResultVo.class);

			if (captchaResultVo.getReturn_code().equals("00000")) {
				response.setCode("200");
				response.setMessage("验证码已发送");
			} else {
				// 验证码发送不成功的错误码
				if (captchaResultVo.getReturn_code().equals("10001")) {
					response.setCode("600");
					response.setMessage("手机号格式不正确");
				} else if (captchaResultVo.getReturn_code().equals("10010")) {
					response.setCode("600");
					response.setMessage("重复调用");
				} else if (captchaResultVo.getReturn_code().equals("99999")
						|| captchaResultVo.getReturn_code().equals("10008")) {
					response.setCode("600");
					response.setMessage("系统错误");
				} else if (captchaResultVo.getReturn_code().equals("10006")) {
					response.setCode("600");
					response.setMessage("短信长度过长");
				} else if (captchaResultVo.getReturn_code().equals("10007")) {
					response.setCode("600");
					response.setMessage("手机号查询不到归属地");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return response;
	}

	// 修改密码
	@PostMapping("/updatePassword")
	public Response updatePassword(@RequestBody UpdatePassVo updatePassVo) {
		Response response = new Response();
		if (updatePassVo != null) {
			if (verifyCode == null || verifyCode.length() == 0 || !verifyCode.equals(updatePassVo.getValidateCode())
					|| !verifyMobile.equals(updatePassVo.getMobile())) {
				response.setCode("600");
				response.setMessage("验证码不正确");
				return response;
			}
			int num = userService.updatePassword(updatePassVo);
			if (num > 0) {
				response.setCode("200");
				response.setMessage("ok");
			} else {
				response.setCode("600");
				response.setMessage("修改密码失败");
			}
		} else {
			response.setCode("600");
			response.setMessage("参数错误");
		}

		return response;
	}

}
