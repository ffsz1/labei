package com.juxiao.xchat.base.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.io.Writer;
import java.util.regex.Pattern;

/**
 * 处理XML工具类
 * 
 * @class: XmlUtils
 * @author: chenjunsheng
 * @date 2018年4月26日
 */
public final class XmlUtils {

	/**
	 * @author: chenjunsheng
	 * @date 2018年4月26日
	 * @param xml
	 * @param clazz
	 * @return
	 */
	public static <T> T fromXml(String xml, Class<T> clazz) {
		XStream xstream = new XStream(new DomDriver());
		xstream.autodetectAnnotations(true);
		xstream.processAnnotations(clazz);
		@SuppressWarnings("unchecked")
		T t = (T) xstream.fromXML(xml);
		return t;
	}

	/**
	 * @author: chenjunsheng
	 * @date 2018年4月26日
	 * @param object
	 * @param isCdata
	 * @return
	 */
	public static String toXml(Object object, boolean isCdata) {
		XppDriver driver;
		if (isCdata) {
			driver = new XppDriver(new XmlFriendlyNameCoder("_-", "_")) {
				@Override
				public HierarchicalStreamWriter createWriter(Writer out) {
					PrettyPrintWriter writer = new PrettyPrintWriter(out, getNameCoder()) {
						@Override
						protected void writeText(QuickWriter writer, String text) {
							if (Pattern.matches("[0-9]*(\\.?)[0-9]*", text) || Pattern.matches("[0-9]+", text)) {
								writer.write(text);
							} else {
								writer.write("<![CDATA[");
								writer.write(text);
								writer.write("]]>");
							}
						}
					};
					return writer;
				}
			};
		} else {
			driver = new XppDriver(new XmlFriendlyNameCoder("_-", "_"));
		}
		XStream xstream = new XStream(driver);
		xstream.autodetectAnnotations(true);
		return xstream.toXML(object);
	}
}
