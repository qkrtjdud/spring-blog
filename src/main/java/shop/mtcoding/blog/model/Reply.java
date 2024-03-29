package shop.mtcoding.blog.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

//User(1) - Reply(N)
//BoaRD(1) - Reply(N)
@Table(name = "reply_tb")
@Entity
@Getter
@Setter
public class Reply {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(nullable = false, length = 100)
    private String comment; // 댓글내용

    @JoinColumn(name = "user_id") // 아이디 이름이나 유니크 등등 내가 직접 설정 가능
    @ManyToOne
    private User user;// FK user_id
    @ManyToOne
    private Board board;// FK board_id
}
