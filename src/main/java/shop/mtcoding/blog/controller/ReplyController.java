package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.blog.dto.ReplyWriteDTO;
import shop.mtcoding.blog.model.Reply;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.ReplyRepository;

@Controller
public class ReplyController {
    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private HttpSession session;

    @PostMapping("/reply/{id}/delete")
    public String delete(@PathVariable Integer id, Integer boardId) { // PathVariable 값 받기

        // 유효성 검사
        if (boardId == null) {
            return "redirect:/40x";
        }

        // 인증검사 ->정상적인 접근에서는 이미 로그인 부분에서 막히지만
        // 포스트맨으로(비정상적인 접근) 접근하면 다 뚫리기 때문에
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 권한체크
        Reply reply = replyRepository.findById(id);
        if (reply.getUser().getId() == sessionUser.getId()) {
            return "redirect:/40x"; // 상태코드는 403
        }

        // 핵심로직
        replyRepository.deleteById(id);

        System.out.println("확인(댓글Id): " + id);
        System.out.println("확인(보드Id)" + boardId);

        return "redirect:/board/" + boardId;
    }

    @PostMapping("/reply/save")
    public String save(ReplyWriteDTO replyWriteDTO) {

        // comment 유효성 검사
        if (replyWriteDTO.getBoardId() == null) {
            return "redirect:/40x";
        }
        if (replyWriteDTO.getComment() == null || replyWriteDTO.getComment().isEmpty()) {
            return "redirect:/40x";
        }
        // 인증검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 댓글쓰기
        replyRepository.save(replyWriteDTO, sessionUser.getId());
        System.out.println("댓글쓰기: " + replyWriteDTO.getBoardId());
        return "redirect:/board/" + replyWriteDTO.getBoardId();

    }

}
