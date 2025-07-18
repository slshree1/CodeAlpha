import java.util.HashMap;
import java.util.Scanner;

class Student{
    static int highestScore;
    static int highestScoreId;
    static int lowestScore=500;
    static int lowestScoreId;
    int studId;
    String studName;
    int score;
    char grade;
    Student(int studId, String studName, int score){
        this.studId=studId;
        this.studName=studName;
        this.score=score;
        if(score>=100){
            this.grade='E';
        }
        else if(score>100 && score<=200){
            this.grade='D';
        }
        else if(score>200 && score<=300){
            this.grade='C';
        }
        else if(score>300 && score<=400){
            this.grade='B';
        }
        else if(score>400 && score<=500){
            this.grade='A';
        }else{
            this.grade='Z';
        }


        if(score>highestScore){
            highestScore=score;
            highestScoreId=studId;
        }
        if(score<lowestScore){
            lowestScore=score;
            lowestScoreId=studId;
        }

        
    }
}
public class StudentGradeTracker{
//    static ArrayList<Student> stlist=new ArrayList<Student>();
    static HashMap<Integer, Student>stmap=new HashMap<Integer, Student>();
    public static void addStudent(){
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter the student id: ");
        int studId=sc.nextInt();
        sc.nextLine();
        System.out.print("Enter the student Name: ");
        String studName=sc.nextLine();
        System.out.print("Enter the score between 0 to 500: ");
        int score=sc.nextInt();

        if(stmap.put(studId, new Student(studId, studName, score))!=null){
            System.out.println("Student Added Successfully");
        }
        else{
            System.out.println("Something wents wrong, we could not add the student!...");
        }

    }
    public static void studSummary(){
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter the Student Id: ");
        int id=sc.nextInt();
        Student std=(Student)stmap.get(id);
        System.out.println("Student Id: "+std.studId);
        System.out.println("Student Name: "+std.studName);
        System.out.println("Score: "+std.score);
        System.out.println("Grade: "+std.grade);
    }

    public static void averageScore(){
        int total_score=0;
        for(int key: stmap.keySet()){
            Student std=(Student)stmap.get(key);
            total_score=total_score+std.score;
//            System.out.println("Average score: "+total_score/stmap.size());
        }
        System.out.println("Average Score: "+total_score/stmap.size());
    }

    public static void highestScore(){
        Student std=(Student)stmap.get(Student.highestScoreId);
        System.out.println("Student Id: "+std.studId);
        System.out.println("Student Name: "+std.studName);
        System.out.println("The Highest Score is: "+std.score);
    }

    public static void lowestScore(){
        Student std=(Student)stmap.get(Student.lowestScoreId);
        System.out.println("Student Id: "+std.studId);
        System.out.println("Student Name: "+std.studName);
        System.out.println("The Lowest score is: "+std.score);
    }



    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        while(true){
            System.out.println("\n\n\n");

            System.out.println("1. Add Student.");
            System.out.println("2. Student Summary.");
            System.out.println("3. Average Score.");
            System.out.println("4. Highest Score.");
            System.out.println("5. Lowest Score.");
            System.out.println("6. Exit.");
            System.out.print("Enter the choice between 1 to 6: ");
            int flag=sc.nextInt();
            int flag1=0;




            switch(flag){
                case 1: {
//                    System.out.println("Add student");
                    addStudent();
                    break;
                }
                case 2: {
//                    System.out.println("student summary");
                    studSummary();
                    break;
                }
                case 3: {
//                    System.out.println("average score");
                    averageScore();
                    break;
                }
                case 4: {
//                    System.out.println("Highest score: ");
                    highestScore();
                    break;
                }
                case 5:{
//                    System.out.println("lowest score");
                    lowestScore();
                    break;
                }
                case 6: {
                    flag1=1;
                    break;
                }
                default: {
                    System.out.println("\n\nPlease Enter the Valid choice...");
                }

            }





            if(flag1==1){
                System.out.println("Thank You.....");
                break;
            }

        }
    }
}