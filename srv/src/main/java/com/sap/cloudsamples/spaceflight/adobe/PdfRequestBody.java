package com.sap.cloudsamples.spaceflight.adobe;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PdfRequestBody {

	@SerializedName("xdpTemplate")
	@Expose
	private String xdpTemplate;
	@SerializedName("xmlData")
	@Expose
	private String xmlData;
	@SerializedName("formType")
	@Expose
	private String formType;
	@SerializedName("formLocale")
	@Expose
	private String formLocale;
	@SerializedName("taggedPdf")
	@Expose
	private Integer taggedPdf;
	@SerializedName("embedFont")
	@Expose
	private Integer embedFont;

	public String getXdpTemplate() {
		return xdpTemplate;
	}

	public void setXdpTemplate(String xdpTemplate) {
		this.xdpTemplate = xdpTemplate;
	}

	public String getXmlData() {
		return xmlData;
	}

	public void setXmlData(String xmlData) {
		this.xmlData = xmlData;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getFormLocale() {
		return formLocale;
	}

	public void setFormLocale(String formLocale) {
		this.formLocale = formLocale;
	}

	public Integer getTaggedPdf() {
		return taggedPdf;
	}

	public void setTaggedPdf(Integer taggedPdf) {
		this.taggedPdf = taggedPdf;
	}

	public Integer getEmbedFont() {
		return embedFont;
	}

	public void setEmbedFont(Integer embedFont) {
		this.embedFont = embedFont;
	}

}
