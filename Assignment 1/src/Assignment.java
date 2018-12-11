import java.util.*;

public class Assignment implements SubmissionHistory {
	/*
	 * Default constructor
	 */

	private TreeMap<String, TreeMap<Date, SubmissionX>> studentSubmissions;

	/*
	 * this TreeMap is for storing students's submissions filter by their
	 * unikey. the nested TreeMap is filter using submissions's date to store
	 * submissions.
	 */

	private TreeMap<String, TreeMap<Integer, Integer>> listBestGrade;
	/*
	 * this TreeMap is for storing each student's best grade. the nested treeMap
	 * is storing grades as keys and occurrences of the grade as the value
	 */

	private TreeSet<String> bestStudents;

	// This list stores unikeys of best students.

	private TreeSet<String> studentRegress;

	// This list stores unikeys of students whose recent submission's grade is
	// less than their highest grade.

	private int highestGrade = 0;

	// integer variable to store current highest grade.

	private int countGrades = 1;

	/*
	 * integer variable to store how many times the grade has been submitted.
	 * this is used for listBestGrade as a counter to keep track. It helps
	 * remove method and get best grade
	 */

	public Assignment() {
		// initialise all data structures

		studentSubmissions = new TreeMap<String, TreeMap<Date, SubmissionX>>();

		listBestGrade = new TreeMap<String, TreeMap<Integer, Integer>>();

		bestStudents = new TreeSet<String>();

		studentRegress = new TreeSet<String>();

	}

	@Override
	public Integer getBestGrade(String unikey) { // total run time O(log(n))

		if (unikey == null) {
			throw new IllegalArgumentException();
		}

		// if the listBestGrade map contains this unikey, return the max grade
		// in the map.
		if (listBestGrade.containsKey(unikey) == true) { // containsKey
															// O(log(n))

			return listBestGrade.get(unikey).lastKey(); // O(log(n))
		}

		return null;
	}

	@Override
	public Submission getSubmissionFinal(String unikey) { // total O(log(n))

		if (unikey == null) {
			throw new IllegalArgumentException();
		}

		// if studentSubmissions doesn't contain the unikey
		if (studentSubmissions.containsKey(unikey) == false) { // containsKey
																// O(log(n))
			return null;
		}

		if (studentSubmissions.get(unikey).lastEntry() == null) {

			return null;
		}

		// return the value of last Entry of the treeMap.
		return studentSubmissions.get(unikey).lastEntry().getValue(); // take
																		// O(log(n))

	}

	@Override
	public Submission getSubmissionBefore(String unikey, Date deadline) { // total
																			// O(log(n))

		if (unikey == null || deadline == null) {
			throw new IllegalArgumentException();
		}

		if (studentSubmissions.containsKey(unikey) == false) { // containsKey
																// O(log(n))

			return null;
		}

		// if there is no values(date) before the deadline in studentSubmissions
		// TreeMap

		if (studentSubmissions.get(unikey).floorEntry(deadline) == null) { // O(log(n))
			return null;
		}

		// return the lastest date that is less than or equal to the deadline
		return studentSubmissions.get(unikey).floorEntry(deadline).getValue();
	}

	@Override
	public Submission add(String unikey, Date timestamp, Integer grade) {

		if (unikey == null || timestamp == null || grade == null) {
			throw new IllegalArgumentException();
		}

		/*
		 * if listBestGrade contain the unikey, get the nested TreeMap's
		 * key(grade) if the grade already existed, increment the value of that
		 * key which is the occurrence of this grade. else occurrence of this
		 * grade is 1(first occurrence) insert in to the treeMap if the current
		 * stored highestGrdae is less than or equal grade that is pass from the
		 * argument. replace the highestGrade with the argument grade
		 */

		if (listBestGrade.containsKey(unikey) == true) { // containsKey
															// O(log(n))
			if (listBestGrade.get(unikey).containsKey(grade) == true) { // O(log(n))

				listBestGrade.get(unikey).put(grade, ++countGrades); // O(log(n))

			} else {
				countGrades = 1;
				listBestGrade.get(unikey).put(grade, countGrades); // O(log(n))
			}

			if (highestGrade <= grade) {
				highestGrade = grade;

			}

			/*
			 * else, create a temporary TreeMap to store the argument grade add
			 * the argument grade to the temporary TreeMap countGrades here will
			 * be 1 as it is the first occurrence of this grade for this student
			 * put the treeMap to the listBestGrade TreeMap using unikey as key
			 * if the current stored highestGrdae is less than or equal grade
			 * that is pass from the argument. replace the highestGrade with the
			 * argument grade
			 */

		} else {
			TreeMap<Integer, Integer> tempListOfGrade = new TreeMap<Integer, Integer>();

			countGrades = 1;
			tempListOfGrade.put(grade, countGrades); // O(log(n))

			listBestGrade.put(unikey, tempListOfGrade); // O(log(n))

			if (highestGrade <= grade) {

				highestGrade = grade;

			}

		}

		/*
		 * if studentSubmissions TreeMap contains unikey create a new submission
		 * object get the student using unikey and put timestamp and submission
		 * object in. if there latest submission grade is less than their best
		 * grade add this student's unikey to list studentRegress if the student
		 * listBestGrade contains the highest grade and that student is not in
		 * the bestStudents set, add that student to bestStudent set.
		 */

		if (studentSubmissions.containsKey(unikey) == true) {

			SubmissionX data = new SubmissionX(unikey, timestamp, grade);

			studentSubmissions.get(unikey).put(timestamp, data); // O(log(n))

			if (getSubmissionFinal(unikey).getGrade() < getBestGrade(unikey)) { // O(log(n))
				studentRegress.add(unikey); // O(log(n))
			}

			if (listBestGrade.get(unikey).containsKey(highestGrade) && !bestStudents.contains(unikey)) { // O(log(n))
				bestStudents.add(unikey); // O(log(n))

			}
			return studentSubmissions.get(unikey).get(timestamp); // O(log(n))

			/*
			 * else when unikey is not in the data structure make new submission
			 * make new temporary sub tree add the submission to temporary sub
			 * tree put unikey and sub tree in to studentSubmissions tree if
			 * there latest submission grade is less than their best grade add
			 * this student's unikey to list studentRegress
			 */
		} else {
			SubmissionX data = new SubmissionX(unikey, timestamp, grade);

			TreeMap<Date, SubmissionX> dataSubTree = new TreeMap<Date, SubmissionX>();

			dataSubTree.put(timestamp, data); // O(log(n))

			studentSubmissions.put(unikey, dataSubTree); // O(log(n))

			if (getSubmissionFinal(unikey).getGrade() < getBestGrade(unikey)) { // (O(log(n))
				studentRegress.add(unikey); // O(log(n))
			}

			return studentSubmissions.get(unikey).get(timestamp); // O(log(n))
		}

	}

	@Override
	public void remove(Submission submission) {

		if (submission == null) {
			throw new IllegalArgumentException();
		}

		// if unikey existed in this treeMap, remove submission associated with
		// the time of submission

		if (studentSubmissions.containsKey(submission.getUnikey())) { // O(log(n))

			studentSubmissions.get(submission.getUnikey()).remove(submission.getTime()); // O(log(n))

		}
		if (listBestGrade.get(submission.getUnikey()) == null) {
			// do nothing
		}

		// if unikey existed and the grade occurs once for this student, remove
		// that grade.

		if (listBestGrade.get(submission.getUnikey()).get(submission.getGrade()).intValue() == 1) { // O(log(n))

			listBestGrade.get(submission.getUnikey()).remove(submission.getGrade()); // O(log(n))
			
			if (listBestGrade.get(submission.getUnikey()).isEmpty()){ // if the student have no submission, remove the student
				
				studentRegress.remove(submission.getUnikey()); // also remove them off the regress list
				
				listBestGrade.remove(submission.getUnikey());
			}
			// else, more than 1 occurrence, get the number of occurrences of
			// this grade, -1 the
			// occurrence and put it back to the treeMap.

		} else if (listBestGrade.get(submission.getUnikey()).get(submission.getGrade()).intValue() > 1) {

			int deduct = listBestGrade.get(submission.getUnikey()).get(submission.getGrade()).intValue(); // O(log(n))

			--deduct;

			listBestGrade.get(submission.getUnikey()).put(submission.getGrade(), deduct); // O(log(n))

		}

	}

	@Override
	public List<String> listTopStudents() {

		// (you may ignore the length of the list in the analysis)
		LinkedList<String> list = new LinkedList<String>(bestStudents); // O(m)
																		// sub
																		// linear
																		// list
		return list;
	}

	@Override
	public List<String> listRegressions() {
		
		LinkedList<String> list = new LinkedList<String>(studentRegress);
		return list;
	}
}