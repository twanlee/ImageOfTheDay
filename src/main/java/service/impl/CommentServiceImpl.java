package service.impl;


import exception.BadWordException;
import model.Comment;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import repository.CommentRepository;
import service.CommentService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@Transactional
public class CommentServiceImpl implements CommentService {
    private static List<String> badWordList;
    private static SessionFactory sessionFactory;
    @PersistenceContext
    private  EntityManager entityManager;

    static {
        badWordList = new ArrayList<String>();
        badWordList.add("penis");
        badWordList.add("vagina");
        badWordList.add("pussy");
        badWordList.add("bitch");
        badWordList.add("nigger");
        badWordList.add("buồi");
        badWordList.add("lồn");
    }

//    static {
//        try {
//            sessionFactory = new Configuration()
//                    .configure("hibernate.conf.xml")
//                    .buildSessionFactory();
////            sessionFactory.close();
//            entityManager = sessionFactory.createEntityManager();
//        } catch (HibernateException e) {
//            e.printStackTrace();
//        }
//    }

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Iterable<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comment findById(Long id) {
        return commentRepository.findOne(id);
    }

    @Override
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }


    @Override
    public void deleteComment(Long id) {
        commentRepository.delete(id);
    }

    @Override
    public Iterable<Comment> allTodayComments() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        String day = today.format(formatter);
//        String queryStr = "SELECT c FROM Comment  AS c WHERE (c.date=:day)";
        TypedQuery<Comment> query = entityManager.createQuery("SELECT c FROM Comment  AS c WHERE (c.date=:day)", Comment.class);
        query.setParameter("day", day);
        return query.getResultList();

    }

    @Override
    public void like(Comment comment) {
        int like = comment.getLikes() + 1;
        comment.setLikes(like);
        commentRepository.save(comment);
    }

    @Override
    public void disLike(Comment comment) {
        comment.setLikes(comment.getLikes() - 1);
        commentRepository.save(comment);
    }

    @Override
    public void checkBadWord(Comment comment) throws BadWordException {
        for (String word : badWordList) {
            if (comment.getFeedback().toLowerCase().contains(word)) {
                throw new BadWordException();
            }
        }
    }
}
