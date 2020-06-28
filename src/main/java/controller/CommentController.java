package controller;

import exception.BadWordException;
import model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import service.CommentService;

import java.util.List;

@Controller
public class CommentController {
    private static int daySelection = 0;
    @Autowired
    private CommentService commentService;
    @GetMapping("/start")
    public ModelAndView showHome() {
        daySelection = 0;
        ModelAndView modelAndView = new ModelAndView("home");
        Iterable<Comment> comments =  commentService.allTodayComments();
        modelAndView.addObject("comments", comments);
        return modelAndView;
    }
    @GetMapping("/start/allComments")
    public ModelAndView showAllComment() {
        daySelection = 1;
        ModelAndView modelAndView = new ModelAndView("home");
        Iterable<Comment> comments = commentService.findAll();
        modelAndView.addObject("comments", comments);
        return modelAndView;
    }
    @PostMapping("/saveComment")
    public String addComment(Comment comment) throws BadWordException {
        comment.setDate();
        commentService.checkBadWord(comment);
        commentService.saveComment(comment);
        return "redirect:/start";
    }
    @ExceptionHandler(BadWordException.class)
    public String showBadWordNotification() {
        return "badWord";
    }
    @GetMapping("/likeComment/{id}")
    public String like(@PathVariable Long id) {
        Comment comment = commentService.findById(id);
        commentService.like(comment);
        if (daySelection == 0)
            return "redirect:/start";
        else
            return "redirect:/start/allComments";
    }
    @GetMapping("/dislikeComment/{id}")
    public String dislike(@PathVariable Long id) {
        Comment comment = commentService.findById(id);
        commentService.disLike(comment);
        if (daySelection == 0)
            return "redirect:/start";
        else
            return "redirect:/start/allComments";
    }
}
