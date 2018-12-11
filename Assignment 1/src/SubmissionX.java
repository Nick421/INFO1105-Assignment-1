import java.util.*;
public class SubmissionX implements Submission{
	
	private String unikey;
	private Date time;
	private Integer grade;
	
	public SubmissionX(String unikey, Date time, Integer grade){
		this.unikey = unikey;
		this.time = time;
		this.grade = grade;
	}
	
	@Override
	public String getUnikey() {
		// TODO Auto-generated method stub
		return unikey;
	}

	@Override
	public Date getTime() {
		// TODO Auto-generated method stub
		return time;
	}

	@Override
	public Integer getGrade() {
		// TODO Auto-generated method stub
		return grade;
	}

	
}
