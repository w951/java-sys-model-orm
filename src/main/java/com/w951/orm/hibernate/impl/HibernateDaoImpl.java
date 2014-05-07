package com.w951.orm.hibernate.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.stereotype.Component;

import com.w951.orm.hibernate.HibernateDao;
import com.w951.orm.hibernate.template.MyHibernateTemplate;

@Component
public class HibernateDaoImpl extends MyHibernateTemplate implements
		HibernateDao {

	@SuppressWarnings("unchecked")
	public <T> T get(T entity, Serializable id) {
		return (T) super.getHibernateTemplate().get(
				entity.getClass().getName(), id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T getByHQL(final String hql, final Object[][] params) {
		return (T) super.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Query query = session.createQuery(hql);
						if (params != null) {
							for (int i = 0; i < params.length; i++) {
								query.setParameter(
										String.valueOf(params[i][0]),
										params[i][1]);
							}
						}
						return query.uniqueResult();
					}
				});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T getByHQLAndCache(final String hql, final Object[][] params) {
		return (T) super.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Query query = session.createQuery(hql);
						if (params != null) {
							for (int i = 0; i < params.length; i++) {
								query.setParameter(
										String.valueOf(params[i][0]),
										params[i][1]);
							}
						}
						return query.setCacheable(true).uniqueResult();
					}
				});
	}

	public <T> long getCount(final T entity) {
		return (Long) super.getHibernateTemplate().execute(
				new HibernateCallback<T>() {
					@SuppressWarnings("unchecked")
					public T doInHibernate(Session session)
							throws HibernateException {
						String hql = String.format("SELECT COUNT(*) FROM %s",
								entity.getClass().getName());
						Query query = session.createQuery(hql);
						return (T) query.uniqueResult();
					}
				});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public long getCountByHql(final String hql, final Object[][] params) {
		return super.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Query query = session.createQuery(hql);
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						query.setParameter(String.valueOf(params[i][0]),
								params[i][1]);
					}
				}
				return query.uniqueResult();
			}
		});
	}

	public <T> void insert(T entity) {
		super.getHibernateTemplate().save(entity);
	}

	public <T> void insertAll(List<T> entities) {
		Session session = super.getHibernateTemplate().getSessionFactory()
				.openSession();
		Transaction tx = session.beginTransaction();
		// tx.begin(); // 如果外面的service包了事务，这里就不能加了，因为不允许内嵌事务
		try {
			if (entities != null && entities.size() > 0) {
				for (int i = 0; i < entities.size(); i++) {
					session.save(entities.get(i));
					if ((i % 50 == 0 || i == (entities.size() - 1)) && i != 0) {
						session.flush();
						session.clear();
					}
				}
				tx.commit();
			}
		} catch (HibernateException e) {
			tx.rollback();
		} finally {
			session.close();
		}
	}

	public <T> void delete(T entity) {
		super.getHibernateTemplate().delete(entity);
	}

	public <T> void deleteAll(List<T> entities) {
		super.getHibernateTemplate().deleteAll(entities);
	}

	public <T> void update(T entity) {
		super.getHibernateTemplate().merge(entity);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> queryList(final T entity, final String... order) {
		return ((List<T>) super.getHibernateTemplate().execute(
				new HibernateCallback<T>() {
					public T doInHibernate(Session session)
							throws HibernateException {
						StringBuffer hql = new StringBuffer();
						hql.append(String.format("FROM %s t", entity.getClass()
								.getName()));
						order(hql, order);
						Query query = session.createQuery(hql.toString());
						return (T) query.list();
					}
				}));
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> queryListByCache(final T entity, final String... order) {
		return ((List<T>) super.getHibernateTemplate().execute(
				new HibernateCallback<T>() {
					public T doInHibernate(Session session)
							throws HibernateException {
						StringBuffer hql = new StringBuffer();
						hql.append(String.format("FROM %s t", entity.getClass()
								.getName()));
						order(hql, order);
						Query query = session.createQuery(hql.toString());
						return (T) query.setCacheable(true).list();
					}
				}));
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> queryPageList(final T entity, final int pageIndex,
			final int pageSize, final String... order) {
		return ((List<T>) super.getHibernateTemplate().execute(
				new HibernateCallback<T>() {
					public T doInHibernate(Session session)
							throws HibernateException {
						StringBuffer hql = new StringBuffer();
						hql.append(String.format("FROM %s t", entity.getClass()
								.getName()));
						order(hql, order);
						Query query = session.createQuery(hql.toString());
						query.setFirstResult((pageIndex - 1) * pageSize);
						query.setMaxResults(pageSize);
						return (T) query.list();
					}
				}));
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> queryPageListByCache(final T entity,
			final int pageIndex, final int pageSize, final String... order) {
		return ((List<T>) super.getHibernateTemplate().execute(
				new HibernateCallback<T>() {
					public T doInHibernate(Session session)
							throws HibernateException {
						StringBuffer hql = new StringBuffer();
						hql.append(String.format("FROM %s t", entity.getClass()
								.getName()));
						order(hql, order);
						Query query = session.createQuery(hql.toString());
						query.setFirstResult((pageIndex - 1) * pageSize);
						query.setMaxResults(pageSize);
						return (T) query.setCacheable(true).list();
					}
				}));
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryListBySQL(String sql,
			String... params) {
		Session session = super.getHibernateTemplate().getSessionFactory()
				.openSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					sqlQuery.setParameter(i, params[i]);
				}
			}
			Query query = sqlQuery
					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			return query.list();
		} catch (HibernateException e) {
			throw new HibernateException(e.getMessage());
		} finally {
			session.flush();
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> queryListByHql(final String hql, final Object[][] params) {
		return (List<T>) super.getHibernateTemplate().execute(
				new HibernateCallback<T>() {
					public T doInHibernate(Session session)
							throws HibernateException {
						Query query = session.createQuery(hql);
						if (params != null) {
							for (int i = 0; i < params.length; i++) {
								query.setParameter(
										String.valueOf(params[i][0]),
										params[i][1]);
							}
						}
						return (T) query.list();
					}
				});
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> queryPageListByHql(final String hql,
			final int pageIndex, final int pageSize, final Object[][] params) {
		return (List<T>) super.getHibernateTemplate().execute(
				new HibernateCallback<T>() {
					public T doInHibernate(Session session)
							throws HibernateException {
						Query query = session.createQuery(hql);
						if (params != null) {
							for (int i = 0; i < params.length; i++) {
								query.setParameter(
										String.valueOf(params[i][0]),
										params[i][1]);
								query.setFirstResult((pageIndex - 1) * pageSize);
								query.setMaxResults(pageSize);
							}
						}
						return (T) query.list();
					}
				});
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> queryListByHqlAndCache(final String hql,
			final Object[][] params) {
		return (List<T>) super.getHibernateTemplate().execute(
				new HibernateCallback<T>() {
					public T doInHibernate(Session session)
							throws HibernateException {
						Query query = session.createQuery(hql);
						if (params != null) {
							for (int i = 0; i < params.length; i++) {
								query.setParameter(
										String.valueOf(params[i][0]),
										params[i][1]);
							}
						}
						return (T) query.setCacheable(true).list();
					}
				});
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> queryPageListByHqlAndCache(final String hql,
			final int pageIndex, final int pageSize, final Object[][] params) {
		return (List<T>) super.getHibernateTemplate().execute(
				new HibernateCallback<T>() {
					public T doInHibernate(Session session)
							throws HibernateException {
						Query query = session.createQuery(hql);
						if (params != null) {
							for (int i = 0; i < params.length; i++) {
								query.setParameter(
										String.valueOf(params[i][0]),
										params[i][1]);
								query.setFirstResult((pageIndex - 1) * pageSize);
								query.setMaxResults(pageSize);
							}
						}
						return (T) query.setCacheable(true).list();
					}
				});
	}

	public boolean excuteSQL(String sql, Object[] params) {
		Session session = super.getHibernateTemplate().getSessionFactory()
				.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					sqlQuery.setParameter(i, params[i]);
				}
			}
			sqlQuery.executeUpdate();
			transaction.commit();
			return true;
		} catch (HibernateException e) {
			transaction.rollback();
		} finally {
			session.flush();
			session.close();
		}
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int excuteHQL(final String hql, final Object[][] params) {
		return super.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Query query = session.createQuery(hql);
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						query.setParameter(String.valueOf(params[i][0]),
								params[i][1]);
					}
				}
				return query.executeUpdate();
			}
		});
	}

	/*---------- 自定义函数 ----------*/

	/**
	 * 排序条件
	 * 
	 * @param hql
	 * @param order
	 */
	private void order(StringBuffer hql, String... order) {
		if (order == null || order.length == 0 || order.length % 2 != 0) {
			return;
		}
		hql.append(" ORDER BY");
		for (int i = 0; i < order.length; i++) {
			if (i % 2 == 0) {
				hql.append(" t.");
				hql.append(order[i]);
			} else {
				hql.append(" ");
				hql.append(order[i]);
				if (i < order.length - 1) {
					hql.append(",");
				} else {
					hql.append(" ");
				}
			}
		}
	}

}
