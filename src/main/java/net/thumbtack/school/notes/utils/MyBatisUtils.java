package net.thumbtack.school.notes.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;

public class MyBatisUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyBatisUtils.class);
    private static SqlSessionFactory sqlSessionFactory;

    public static boolean initSqlSessionFactory() {
        try (Reader reader = Resources.getResourceAsReader("mybatis-config.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

// 		instead of setting here, it is possible to set in configuration XML file
//      sqlSessionFactory.getConfiguration().setAggressiveLazyLoading(false);

            return true;
        } catch (Exception e) {
            LOGGER.error("Error loading mybatis-config.xml", e);
            return false;
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
