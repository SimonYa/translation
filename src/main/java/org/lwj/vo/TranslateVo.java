package org.lwj.vo;

public class TranslateVo {
	//需要翻译的文本
	private String sourceText;
	//源语言
	private String sourceLanguage;
	//目标语言
	private String targetLanguage;
	
	public String getSourceText() {
		return sourceText;
	}
	public void setSourceText(String sourceText) {
		this.sourceText = sourceText;
	}
	public String getSourceLanguage() {
		return sourceLanguage;
	}
	public void setSourceLanguage(String sourceLanguage) {
		this.sourceLanguage = sourceLanguage;
	}
	public String getTargetLanguage() {
		return targetLanguage;
	}
	public void setTargetLanguage(String targetLanguage) {
		this.targetLanguage = targetLanguage;
	}
	
	@Override
	public String toString() {
		return "TranslateVo [sourceText=" + sourceText + ", sourceLanguage=" + sourceLanguage + ", targetLanguage="
				+ targetLanguage + "]";
	}
}
