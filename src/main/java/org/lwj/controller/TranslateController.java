package org.lwj.controller;

import org.lwj.base.Response;
import org.lwj.vo.TranslateVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.tmt.v20180321.TmtClient;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateRequest;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateResponse;

@RestController
@RequestMapping(value="/translate")
public class TranslateController {
	
	private final static String SecretId = "AKIDhTzQ0Ic0hcNuIqswUMXWGsGCuOdflEgy";
	private final static String SecretKey = "HJ5dxdsuCJMAmO0T7erS9GVoSvOEfiXr";
	
	@PostMapping(value="/translate")
	public Response translate(@RequestBody TranslateVo translateVo) {
		Response response = new Response();
		System.out.println("translate");
		if(translateVo == null) {
			response.setCode("600");
			response.setMessage("不能为空");
			return response;
		}
		System.out.println(translateVo.toString());
		
		try{
            Credential cred = new Credential(SecretId, SecretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("tmt.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);            
            TmtClient client = new TmtClient(cred, "ap-guangzhou", clientProfile);
            
            //请求参数
            String sourceText = translateVo.getSourceText();
            String sourceLanguage = translateVo.getSourceLanguage();
            String targetLanguage = translateVo.getTargetLanguage();
            
            String params = "{\"SourceText\":\""+sourceText+"\",\"Source\":\""+sourceLanguage+"\",\"Target\":\""+targetLanguage+"\",\"ProjectId\":1257991207}";
            TextTranslateRequest req = TextTranslateRequest.fromJsonString(params, TextTranslateRequest.class);
            TextTranslateResponse resp = client.TextTranslate(req);
            response.setData(resp);
        } catch (TencentCloudSDKException e) {
        	System.out.println(e);
        	response.setCode("600");
        	response.setMessage("未知错误");
        }
		return response;
	}
}
