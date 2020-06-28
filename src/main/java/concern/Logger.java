package concern;

import model.Comment;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class Logger {
    public void badWordNotify() {
        System.out.println("badword");
    }
    @AfterThrowing(pointcut = "execution(public * service.CommentService.checkBadWord(..))",throwing = "e")
    public void log(JoinPoint joinPoint, Exception e){
        Object[] signatureArgs = joinPoint.getArgs();
        for(Object signature : signatureArgs){
            Comment comment = (Comment) signature;
            String time = java.time.LocalTime.now().toString();
            System.out.println("Feedback:" + comment.getFeedback()
                    + ". Author: " + comment.getAuthor()
                    + ". Date: " + comment.getDate()
                    +". Time: "+ time);
        }
    }
}
