package com.ls.train.files_encryption.dao.derbydao;

import com.ls.train.files_encryption.bean.FileBean;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface FileDao {

    FileBean selectByPrimaryKey(String fileId);
}
