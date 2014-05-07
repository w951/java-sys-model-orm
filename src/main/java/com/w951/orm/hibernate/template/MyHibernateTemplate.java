package com.w951.orm.hibernate.template;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;

public class MyHibernateTemplate {
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	@Resource
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
