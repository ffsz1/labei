package com.erban.main.wechat.pojo;


/**
 * 高级按键
 * ComplexButton
 * @author 电子小孩
 *
 * 2016年5月27日 下午5:31:37
 */
public class ComplexButton extends Button {
	
	private Button[] sub_button;

	public Button[] getSub_button() {
		return sub_button;
	}

	public void setSub_button(Button[] sub_button) {
		this.sub_button = sub_button;
	}

}
