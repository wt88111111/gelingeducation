package com.project.gelingeducation.dao.Impl;

import com.project.gelingeducation.dao.IWebDataBeanDao;
import com.project.gelingeducation.domain.WebDataBean;
import org.springframework.stereotype.Repository;

@Repository
public class WebDataBeanDaoImpl extends BaseDao implements IWebDataBeanDao {

    @Override
    public WebDataBean findById(int id) {
        return getSession().get(WebDataBean.class, id);
    }

    @Override
    public void save(WebDataBean webDataBean) {
        getSession().save(webDataBean);
    }

    @Override
    public void update(WebDataBean webDataBean) {
        getSession().update(webDataBean);
    }

    @Override
    public WebDataBean getOnlyData() {
        return (WebDataBean) getSession().createQuery("from WebDataBean").uniqueResult();
    }
}