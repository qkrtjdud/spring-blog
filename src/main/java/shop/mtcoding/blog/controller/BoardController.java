package shop.mtcoding.blog.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blog.dto.BoardDetailDTO;
import shop.mtcoding.blog.dto.UpdateDTO;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.Reply;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.BoardRepository;
import shop.mtcoding.blog.repository.ReplyRepository;

@Controller
public class BoardController {
    @Autowired
    private HttpSession session;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @ResponseBody
    @GetMapping("/test/reply")
    public List<Reply> test2() {
        List<Reply> replys = replyRepository.findByBoardId(1);
        return replys;
    }

    /* 보드만 들고왔는데 유저 오브젝트도 들고 있으니, 유저 내용도 다 제이슨으로 들고옴 */
    @ResponseBody
    @GetMapping("/test/board/1")
    public Board test() {
        Board board = boardRepository.findById(1);
        return board;
    }

    @PostMapping("/board/{id}/update")
    public String update(@PathVariable Integer id, UpdateDTO updateDTO) {
        // 1.인증검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 2.권한검사
        Board board = boardRepository.findById(id);
        if (board.getUser().getId() != sessionUser.getId()) {
            return "redirect:/40x";
        }

        // 3.핵심로직
        // update board_tb set title =:title, content =:content where id =:id;
        boardRepository.update(updateDTO, id);
        return "redirect:/board/" + id;
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateFrom(@PathVariable Integer id, HttpServletRequest request) {
        // 1.인증검사
        // 2.권한검사

        // 3.핵심로직
        Board board = boardRepository.findById(id);
        request.setAttribute("board", board);

        return "board/updateForm";
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable Integer id) { // 1.PathVariable 값 받기
        // 2.인증검사 ->정상적인 접근에서는 이미 로그인 부분에서 막히지만
        // 포스트맨으로(비정상적인 접근) 접근하면 다 뚫리기 때문에

        // session에 접근해서 sessionUser 키값을 가져오세요
        // null 이면 로그인페이지로 보내고, nul 아니면 3번 실행
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 3.권한 검사
        Board board = boardRepository.findById(id);
        if (board.getUser().getId() != sessionUser.getId()) {
            return "redirect:/40x";
        }

        // boardRepository.deleteById(id); 호출하세요-> 리턴을 받지 마세요
        // 4.모델에 접근해서 삭제 delete from board_tb where id =:id

        boardRepository.deleteById(id);
        // System.out.println("확인: " + id);

        return "redirect:/";
    }

    @GetMapping({ "/", "/board" })
    public String index(@RequestParam(defaultValue = "0") Integer page, HttpServletRequest request) {
        // 1.유효성 검사 x ->http body 데이터가 없으니깐 필요 x, get요청,프로토콜
        // 2.인증검사 x ->로그인 안해도 누구나 볼수 있게 구현 할거라서

        List<Board> boardList = boardRepository.findAll(page); // 현재 페이지 =1
        int totalCount = boardRepository.count(); // totalpage =5

        // System.out.println("테스트 : totalCount :" + totalCount);
        int totalPage = totalCount / 3; // totalpage=1
        if (totalCount % 3 > 0) {
            totalPage = totalPage + 1; // totoalpage=2
        }
        boolean last = totalPage - 1 == page;

        // System.out.println("테스트: " + boardList.size());
        // System.out.println("테스트: " + boardList.get(0).getTitle());
        // 인덱스값이 없는데 자꾸 찾아서 터짐
        request.setAttribute("boardList", boardList);
        request.setAttribute("prevpage", page - 1);
        request.setAttribute("nextpage", page + 1);
        request.setAttribute("first", page == 0 ? true : false);
        request.setAttribute("last", last);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("totalCount", totalCount);
        return "index";
    }

    @PostMapping("/board/save")
    public String save(WriteDTO writeDTO) {
        // valodation check(유효성 검사)
        if (writeDTO.getTitle() == null || writeDTO.getTitle().isEmpty()) {
            return "redirect:/40x";
        }
        if (writeDTO.getContent() == null || writeDTO.getContent().isEmpty()) {
            return "redirect:/40x";
        }

        // 인증체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        boardRepository.save(writeDTO, sessionUser.getId());
        return "redirect:/";

    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        } // 로그인 안하고 접근 하는걸 막음
        return "board/saveForm";
    }

    // localhost:8080/board/1
    // localhost:8080/board/50
    @GetMapping("/board/{id}") // PK를 조회
    public String detail(@PathVariable Integer id, HttpServletRequest request) { // C(controller)
        User sessionUser = (User) session.getAttribute("sessionUser"); // 세션접근
        List<BoardDetailDTO> dtos = null; // M(model)
        if (sessionUser == null) {
            dtos = boardRepository.findByIdJoinReply(id, null);
        } else {
            dtos = boardRepository.findByIdJoinReply(id, sessionUser.getId());
        }

        boolean pageOwner = false;
        if (sessionUser != null) {
            // System.out.println("테스트 세션 ID : " + sessionUser.getId());
            // System.out.println("테스트 세션 board.getUser().getId() : " +
            // board.getUser().getId());
            pageOwner = sessionUser.getId() == dtos.get(0).getBoardUserId();

        }

        request.setAttribute("dtos", dtos);
        request.setAttribute("pageOwner", pageOwner);
        return "board/detail"; // V(veiw)

    }
}
