package jwp.model;
import java.time.LocalDateTime;
import java.util.Objects;

public class Question {
    private Long questionId;       // bigint
    private String writer;         // varchar(30)
    private String title;          // varchar(50)
    private String contents;       // varchar(5000)
    private LocalDateTime createdDate; // timestamp
    private int countOfAnswer;     // int

    public Question(Long questionId, String writer, String title, String contents,
                    LocalDateTime createdDate, int countOfAnswer) {
        this.questionId = questionId;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.createdDate = createdDate;
        this.countOfAnswer = countOfAnswer;
    }

    public Question(String writer, String title, String contents) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public String getContents() {
        return contents;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public int getCountOfAnswer() {
        return countOfAnswer;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public void update(Question updateQuestion) {
        this.writer = updateQuestion.writer;
        this.title = updateQuestion.title;
        this.contents = updateQuestion.contents;
        this.createdDate = updateQuestion.createdDate;
        this.countOfAnswer = updateQuestion.countOfAnswer;
    }

    public boolean isSameQuestion(Question question) {
        return isSameQuestion(question.getQuestionId());
    }

    public boolean isSameQuestion(Long questionId) { return questionId.equals(this.questionId); }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return countOfAnswer == question.countOfAnswer
                && Objects.equals(questionId, question.questionId)
                && Objects.equals(writer, question.writer)
                && Objects.equals(title, question.title)
                && Objects.equals(contents, question.contents)
                && Objects.equals(createdDate, question.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, writer, title, contents, createdDate, countOfAnswer);
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionId=" + questionId +
                ", writer='" + writer + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", createdDate=" + createdDate +
                ", countOfAnswer=" + countOfAnswer +
                '}';
    }
}
