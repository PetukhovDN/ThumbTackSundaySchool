package net.thumbtack.school.notes.daoimpl;

import net.thumbtack.school.notes.mappers.CommentMapper;
import net.thumbtack.school.notes.mappers.NoteMapper;
import net.thumbtack.school.notes.mappers.SectionMapper;
import net.thumbtack.school.notes.mappers.UserMapper;
import net.thumbtack.school.notes.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

public class DaoImplBase {

    protected SqlSession getSession() {
        return MyBatisUtils.getSqlSessionFactory().openSession();
    }

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
}
