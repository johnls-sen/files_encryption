package com.ls.train.files_encryption.service;

import com.ls.train.files_encryption.bean.FileBean;

import java.util.List;

public interface BaseService<T>{

    boolean addBean(T bean);

    T findBeanById(String beanId);

    List<T> findBeanList();

    boolean modifyBean(T bean);

    boolean removeBean(String beanId);
}
