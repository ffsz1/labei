package com.erban.main.wechat.message.resp;


import java.util.List;

/**
 * 图文响应
 * NewsMessage
 * @author 电子小孩
 *
 * 2016年5月27日 下午5:29:45
 */
public class NewsMessage extends BaseMessage {
	
	// 图文消息个数，限制为10条以内   
	private int ArticleCount;
	// 多条图文消息信息，默认第一个item为大图   
	private List<ArticleModel> Articles;

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<ArticleModel> getArticles() {
		return Articles;
	}

	public void setArticles(List<ArticleModel> articles) {
		Articles = articles;
	}

}
