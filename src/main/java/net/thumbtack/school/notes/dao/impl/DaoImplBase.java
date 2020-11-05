package net.thumbtack.school.notes.dao.impl;

import net.thumbtack.school.notes.mappers.*;
import org.apache.ibatis.session.SqlSession;

public class DaoImplBase {

    protected UserMapper getUserMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(UserMapper.class);
    }

    protected SectionMapper getSectionMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(SectionMapper.class);
    }

    protected NoteMapper getNoteMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(NoteMapper.class);
    }

    protected CommentMapper getCommentMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(CommentMapper.class);
    }

    protected SessionMapper getSessionMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(SessionMapper.class);
    }
}
