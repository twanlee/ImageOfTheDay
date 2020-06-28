package service;

import exception.BadWordException;
import model.Comment;

public interface CommentService {
    Iterable<Comment> findAll();

    Comment findById(Long id);

    void saveComment(Comment comment);

    void deleteComment(Long id);

    Iterable<Comment> allTodayComments();

    void like(Comment comment);

    void disLike(Comment comment);

    void checkBadWord(Comment comment) throws BadWordException, BadWordException;
}
