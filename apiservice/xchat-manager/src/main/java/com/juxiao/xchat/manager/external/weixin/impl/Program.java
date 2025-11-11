package com.juxiao.xchat.manager.external.weixin.impl;

public class Program {

	public static void main(String[] args) throws Exception {

		//
		// 第三方回复公众平台
		//

		// 需要加密的明文
		String encodingAesKey = "2oOvvXypHnbPQb0r1NKeJUienhZUTyPEmmZSysvT6r0";
		String token = "hjPALHtBbaWjEghGebYknXIJEuoWAgUN";
		String appId = "wxfdccf7c83108ee43";

		WXBizMsgCrypt crypt = new WXBizMsgCrypt(token, encodingAesKey, appId);
		String resutlt = crypt.decrypt("95PPCoVp/AtCMWjieoSZQCOK2HfehvT2pOw/SYsbo5vFih5gqvTka8kDb8DZrnROdRMSv+4tfckuetq9/pNVD7byNC9bn9mnW7LVIsr1XvQ/ehySh+xZeFPNl2JP810BqW3L0jZkrBgfVmdmfyV1KOk5d/Hhk+xl8ImNyo9Y6fs8lwdQAKdv+xWCgM80dYzi0TOYP1NatzhXMkuwJrAhHyk5dMV7tvCWtf+vH3KSL9o/PymtzeTG5B7fmoHUFzZ/IXtZz0MEh4JuPfN8Bfn8knqSI0m3g/iCwf0VY13x38Q=");
		System.out.println(resutlt);
//		String replyMsg = " 中文<xml><ToUserName><![CDATA[oia2TjjewbmiOUlr6X-1crbLOvLw]]></ToUserName><FromUserName><![CDATA[gh_7f083739789a]]></FromUserName><CreateTime>1407743423</CreateTime><MsgType><![CDATA[video]]></MsgType><Video><MediaId><![CDATA[eYJ1MbwPRJtOvIEabaxHs7TX2D-HV71s79GUxqdUkjm6Gs2Ed1KF3ulAOA9H1xG0]]></MediaId><Title><![CDATA[testCallBackReplyVideo]]></Title><Description><![CDATA[testCallBackReplyVideo]]></Description></Video></xml>";
//
//		WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
//		String mingwen = pc.encryptMsg(replyMsg, timestamp, nonce);
//		System.out.println("加密后: " + mingwen);
//
//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		DocumentBuilder db = dbf.newDocumentBuilder();
//		StringReader sr = new StringReader(mingwen);
//		InputSource is = new InputSource(sr);
//		Document document = db.parse(is);
//
//		Element root = document.getDocumentElement();
//		NodeList nodelist1 = root.getElementsByTagName("Encrypt");
//		NodeList nodelist2 = root.getElementsByTagName("MsgSignature");
//
//		String encrypt = nodelist1.item(0).getTextContent();
//		String msgSignature = nodelist2.item(0).getTextContent();
//
//		String format = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
//		String fromXML = String.format(format, encrypt);
//
//		//
//		// 公众平台发送消息给第三方，第三方处理
//		//
//
//		// 第三方收到公众号平台发送的消息
//		String result2 = pc.decryptMsg(msgSignature, timestamp, nonce, fromXML);
//		System.out.println("解密后明文: " + result2);
		
		//pc.verifyUrl(null, null, null, null);
	}
}
