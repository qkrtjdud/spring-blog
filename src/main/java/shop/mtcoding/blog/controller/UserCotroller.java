package shop.mtcoding.blog.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.repository.UserRepositiory;

@Controller
public class UserCotroller {

    @Autowired
    private UserRepositiory userRepositiory;

    // 실무
    @PostMapping("/join")
    public String join(JoinDTO joinDTO) {

        // validation check(유효성 검사)
        // null 값과 공백이 들어올수 없도록 막음
        if (joinDTO.getUsername() == null || joinDTO.getUsername().isEmpty()) {
            return "redirect:/40x";
        }
        if (joinDTO.getPassword() == null || joinDTO.getPassword().isEmpty()) {
            return "redirect:/40x";
        }
        if (joinDTO.getEmail() == null || joinDTO.getEmail().isEmpty()) {
            return "redirect:/40x";
        }

        userRepositiory.save(joinDTO); // 핵심 기능
        return "redirect:/loginForm";
    }

    // // 정상인
    // @PostMapping("/join")
    // public String join(String username, String password, String email) {
    // // username=ssar&password=1234&email=ssar@nate.com
    // System.out.println("username : " + username);
    // System.out.println("password : " + password);
    // System.out.println("email : " + email);
    // return "redirect:/loginForm";
    // }

    // // 비정상
    // @PostMapping("/join")
    // public String join(HttpServletRequest request) throws IOException {
    // // username=ssar&password=1234&email=ssar@nate.com
    // BufferedReader br = request.getReader();
    // // 버퍼가 소비됨
    // String body = br.readLine();
    // // 버퍼에 값이 없어서, 못꺼냄.
    // String username = request.getParameter("username");
    // System.out.println("body : " + body);
    // System.out.println("username : " + username);
    // return "redirect:/loginForm";
    // }

    // 약간 정상
    // DS(컨트롤러 메서드 찾기, 바디데이터 파싱)
    // DS가 바디데이터를 파싱안하고 컨트롤러 메서드만 찾은 상황
    // @PostMapping("/join")
    // public String join(HttpServletRequest request) {
    // String username = request.getParameter("username");
    // String password = request.getParameter("password");
    // String email = request.getParameter("email");
    // System.out.println("username : " + username);
    // System.out.println("password : " + password);
    // System.out.println("email : " + email);
    // return "redirect:/loginForm";
    // }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm() {
        return "user/updateForm";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}
