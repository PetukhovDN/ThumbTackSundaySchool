package net.thumbtack.school.notes.daoimpl;

import net.thumbtack.school.notes.dao.CommonDao;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClearDatabaseDaoImpl extends DaoImplBase implements CommonDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClearDatabaseDaoImpl.class);

    @Override
    public void clear() {
        LOGGER.debug("Clear database");
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).deleteAll();
                getNoteMapper(sqlSession).deleteAll();
                getSectionMapper(sqlSession).deleteAll();
                getCommentMapper(sqlSession).deleteAll();
            } catch (RuntimeException ex) {
                LOGGER.info("Can't clear database");
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }
}
