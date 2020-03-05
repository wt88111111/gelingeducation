package com.project.gelingeducation.dao.Impl;

import com.project.gelingeducation.dao.AdminInfoDao;
import com.project.gelingeducation.domain.AdminInfo;
import com.project.gelingeducation.dto.PageResult;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class AdminInfoDaoImpl implements AdminInfoDao {

    @Autowired
    private SessionFactory sessionFactory;

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }


    @Override
    public PageResult getLists(int page, int limits) {
        Session session = getSession();
        TypedQuery<AdminInfo> query = session.createQuery("from AdminInfo");
        query.setFirstResult(page);//得到当前页
        query.setMaxResults(limits);//得到每页的记录数
        String hql = "select count(*) from AdminInfo";//此处的Product是对象
        Query queryCount = session.createQuery(hql);

        PageResult pageResult = new PageResult();
        pageResult.setTotalPages(((Long) queryCount.setCacheable(true).uniqueResult()).intValue());
        List<AdminInfo> list = query.getResultList();
        pageResult.setLists(list);
        pageResult.setPageNum(page);
        pageResult.setPageSize(limits);

        return pageResult;
    }

    @Override
    public AdminInfo findById(long id) {
        AdminInfo adminInfo = getSession().get(AdminInfo.class, id);
        return adminInfo;
    }

    @Override
    public AdminInfo findByPhone(String account) {
        Query query = getSession().createQuery("from AdminInfo where account=?");
        query.setParameter(0, account);
        List<AdminInfo> list = query.list();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }

    }

    @Override
    public AdminInfo insert(AdminInfo adminInfo) {
        getSession().save(adminInfo);
        return adminInfo;
    }

    @Override
    public void delect(long id) {
        getSession().delete(getSession().get(AdminInfo.class, id));
    }

    @Override
    public void update(AdminInfo adminInfo) {
        getSession().update(adminInfo);
    }

    @Override
    public void updateCoverImg(long id, String coverImg) {
//        Query query = getSession().createQuery("update AdminInfo set coverImg = " + coverImg + " where id = " + id);
//        query.executeUpdate();
        Session session = getSession();
        AdminInfo adminInfo = session.get(AdminInfo.class, id);
        adminInfo.setCoverImg(coverImg);
        session.update(adminInfo);
    }

    @Override
    public void updateInfo(long id, String userName, String eMail, int sex, String note) {
        StringBuffer hql = new StringBuffer("update AdminInfo set ");
        int index = 0;
        if (!StringUtils.isEmpty(userName)) {
            hql.append("user_name = '" + userName + "'");
            index++;
        }
        if (!StringUtils.isEmpty(eMail)) {
            if (index > 0) {
                hql.append(" ,");
            }
            hql.append("email = '" + eMail + "'");
            index++;
        }
        if (!StringUtils.isEmpty(sex)) {
            if (index > 0) {
                hql.append(" ,");
            }
            hql.append("ssex = '" + sex + "'");
            index++;
        }
        if (!StringUtils.isEmpty(note)) {
            if (index > 0) {
                hql.append(" ,");
            }
            hql.append("note = '" + note + "'");
        }
        Query query = getSession().createQuery(hql.toString() + " where id = " + id);
        query.executeUpdate();
    }

    @Override
    public void updatePassword(long id, String newPassword) {
        Query query = getSession().createQuery("update AdminInfo set password = '" + newPassword + "' where id = " + id);
        query.executeUpdate();
    }
}
