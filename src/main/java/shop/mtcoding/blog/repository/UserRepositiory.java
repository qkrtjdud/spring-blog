package shop.mtcoding.blog.repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.JoinDTO;

//BoardController, UserController, UserRepository
//EntityManager, HttpSession
@Repository // @Controller @Repository -> 메모리에 뜸움!
public class UserRepositiory {
    @Autowired
    private EntityManager em;

    @Transactional
    public void save(JoinDTO joinDTO) {
        Query query = em
                .createNativeQuery("insert into user_tb(username,password,email)values(:username,:password,:email)");
        query.setParameter("username", joinDTO.getUsername());
        query.setParameter("password", joinDTO.getPassword());
        query.setParameter("email", joinDTO.getEmail());
        query.executeUpdate();
    }

}
