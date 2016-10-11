package com.diilo.streetartphone.server.core.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.diilo.streetartphone.server.core.dao.generics.GenericDaoHibernateImpl;
import com.diilo.streetartphone.server.core.entities.UserBean;

@Repository(value = "userDao")
public class UserDao extends GenericDaoHibernateImpl<UserBean, Long> {

	@Autowired
	public UserDao(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
		super(UserBean.class);
		setSessionFactory(sessionFactory);
	}

	public UserBean getByNickname(final String nickname) {
		List<UserBean> userList = getHibernateTemplate().find(
				"from UserBean user where lower(user.nickname) = lower(?)", nickname);
		if (userList.size() == 1) {
			return userList.get(0);
		} else {
			return null;
		}
	}

	public UserBean getByEmail(final String email) {
		List<UserBean> userList = getHibernateTemplate().find("from UserBean user where user.email = ?", email);
		if (userList.size() == 1) {
			return userList.get(0);
		} else {
			return null;
		}
	}

	public void setTwitterTokenToUsersEmail(final String email, final int twitterUserId, final String token,
			String tokenSecret) {
		Query q = getHibernateTemplate()
				.getSessionFactory()
				.getCurrentSession()
				.createQuery(
						"update UserBean set twitterAuthToken = :token, twitterUserId = :twitterUserId, twitterAuthSecretToken = :tokenSecret where email = :email");
		q.setString("token", token);
		q.setInteger("twitterUserId", twitterUserId);
		q.setString("tokenSecret", tokenSecret);
		q.setString("email", email);
		q.executeUpdate();
		getHibernateTemplate().flush();
	}

	public void setFacebookTokenToUsersEmail(String email, String facebookAccessToken) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createQuery("update UserBean set facebookAccessToken = :facebookAccessToken where email = :email");
		q.setString("facebookAccessToken", facebookAccessToken);
		q.setString("email", email);
		q.executeUpdate();
		getHibernateTemplate().flush();

	}
}
