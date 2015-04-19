package ucsc.group12.agentmate.bll;

/**
 * Created by NRV on 8/23/2014.
 */


        import java.io.Serializable;

        import android.os.Parcel;
        import android.os.Parcelable;

public class Representative implements Serializable{



    public String Emp_id;
    public String UserName;
    public String enc_password;
    public String Question;
    public String Answer;

    public String getEmp_id() {
        return Emp_id;
    }

    public void setEmp_id(String emp_id) {
        Emp_id = emp_id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEnc_password() {
        return enc_password;
    }

    public void setEnc_password(String enc_password) {
        this.enc_password = enc_password;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public Representative(String empid_create,String username_create,String enc_pwd_create,String question_create,String Answer_create){
        //Naming convention "_create" stand for parameters.
        Emp_id=empid_create;
        UserName=username_create;
        enc_password=enc_pwd_create;
        Question=question_create;
        Answer=Answer_create;

    }
}
