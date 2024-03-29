package shop.mtcoding.blog.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.ReplyWriteDTO;
import shop.mtcoding.blog.model.Reply;

//Cotroller - User, Board, Reply, Error
//Repository - User, Board, Reply
//EntityMager 

@Repository
public class ReplyRepository {

    @Autowired
    private EntityManager em;

    @Transactional
    public void deleteById(Integer id) {
        Query query = em.createNativeQuery("delete from reply_tb where id =:id");
        query.setParameter("id", id);
        query.executeUpdate();

    }

    public List<Reply> findByBoardId(Integer boardId) {
        Query query = em.createNativeQuery("select * from reply_tb where board_id = :boardId", Reply.class);
        query.setParameter("boardId", boardId);
        return query.getResultList();

    }

    @Transactional
    public void save(ReplyWriteDTO replyWriteDTO, Integer userId) {
        Query query = em
                .createNativeQuery(
                        "insert into reply_tb(board_id, comment, user_id) values(:boardId, :comment, :userId)");
        query.setParameter("boardId", replyWriteDTO.getBoardId());
        query.setParameter("comment", replyWriteDTO.getComment());
        query.setParameter("userId", userId);
        query.executeUpdate();

        System.out.println("테스트2 보드: " + replyWriteDTO.getBoardId());
        System.out.println("테스트2 댓 내용: " + replyWriteDTO.getComment());
        System.out.println("테스트2 아이디: " + userId);
    }

    public Reply findById(Integer id) {
        Query query = em
                .createNativeQuery(
                        "select * from reply_tb where id =:id", Reply.class);
        query.setParameter("id", id);
        return (Reply) query.getSingleResult();
    }

}
