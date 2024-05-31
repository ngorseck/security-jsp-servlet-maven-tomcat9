package com.samanecorporation.security.dao;

import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import com.samanecorporation.security.config.HibernateUtil;
import com.samanecorporation.security.entity.UserEntity;

public class UserDao extends RepositoryImpl<UserEntity> implements IUserDao {
	
	private Session session = HibernateUtil.getSessionFactory().openSession();

	@Override
	public Optional<UserEntity> login(String email, String password) {

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<UserEntity> cr = cb.createQuery(UserEntity.class);
		Root<UserEntity> user = cr.from(UserEntity.class);
		//Predicate pour la clause where
		Predicate predicateEmail = cb.equal(user.get("email"), email);
		Predicate predicatePwd = cb.equal(user.get("password"), password);
		Predicate finalPredicate = cb.and(predicateEmail, predicatePwd);
		
		cr.select(user);
		cr.where(finalPredicate);
		
		return Optional.ofNullable(session.createQuery(cr).getSingleResult());
	}
}
