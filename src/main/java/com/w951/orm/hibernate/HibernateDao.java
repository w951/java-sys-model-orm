package com.w951.orm.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface HibernateDao {
	/**
	 * 获取实体类对象
	 * @param entityName 实体类名称 entity.getClass().getName();
	 * @param id 实体类的标识索引
	 * @return entity
	 */
	public<T> T get(T entity, Serializable id);
	public<T> T getByHQL(String hql, Object[][] params);
	public<T> T getByHQLAndCache(String hql, Object[][] params);
	public<T> long getCount(T entity);
	public long getCountByHql(String hql, Object[][] params);
	
	public<T> void insert(T entity);
	public<T> void insertAll(List<T> entities);
	public<T> void delete(T entity);
	public<T> void deleteAll(List<T> entities);
	public<T> void update(T entity);
		
	public<T> List<T> queryList(T entity, String... order);
	public<T> List<T> queryListByCache(T entity, String... order);
	public<T> List<T> queryPageList(T entity, int pageIndex, int pageSize, String... order);
	public<T> List<T> queryPageListByCache(T entity, int pageIndex, int pageSize, String... order);
	
	public List<Map<String, Object>> queryListBySQL(String sql, String... params);
	
	/**
	 * 
	 * @param hql
	 * @param params new String[][] {new String[] {"字段名","字段值"}}
	 * @return
	 */
	public<T> List<T> queryListByHql(String hql, Object[][] params);
	public<T> List<T> queryPageListByHql(String hql, int pageIndex, int pageSize, Object[][] params);
	
	/**
	 * params new String[][] {new String[] {"字段名","字段值"}}
	 * @param hql
	 * @param params
	 * @return
	 */
	public<T> List<T> queryListByHqlAndCache(String hql, Object[][] params);
	public<T> List<T> queryPageListByHqlAndCache(String hql, int pageIndex, int pageSize, Object[][] params);
	
	public boolean excuteSQL(String sql, Object[] params);
	public int excuteHQL(String hql, Object[][] params);
}
