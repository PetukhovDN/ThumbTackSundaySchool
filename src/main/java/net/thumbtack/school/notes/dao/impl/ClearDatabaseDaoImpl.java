package net.thumbtack.school.notes.dao.impl;

import net.thumbtack.school.notes.dao.ClearDatabaseDao;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ClearDatabaseDaoImpl extends DaoImplBase implements ClearDatabaseDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClearDatabaseDaoImpl.class);

    private final SqlSession sqlSession;

    public ClearDatabaseDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public void clear() {
        LOGGER.debug("Clear database");
        try {
            getUserMapper(sqlSession).deleteAll();
            getNoteMapper(sqlSession).deleteAll();
            getSectionMapper(sqlSession).deleteAll();
            getCommentMapper(sqlSession).deleteAll();
        } catch (RuntimeException ex) {
            LOGGER.info("Can't clear database");
            throw ex;
        }
        sqlSession.commit();
    }
}
