
import org.apache.commons.csv.*;
import edu.duke.*;
import java.io.*;
import java.util.*;
public class babyBirths {
    //method for counting names  
    public void totalBirth(CSVParser parser){
      //declaring counting variables for the total names and for girls names and boys names
      int totalNum=0;
      int girlNum=0;
      int boyNum=0;
      //for each record in the file
      for(CSVRecord record: parser){
         // get number of the names from the third column to count total names
         int numBorn= Integer.parseInt(record.get(2));
         totalNum+=numBorn;
         //if it's a girl name then increment girls count and if not increment boys count
         if(record.get(1).equals("F")){
            girlNum+=numBorn;
         }
         else{
           boyNum+=numBorn;
         }
      }
      //print the numbers we got
      System.out.println("total nember of people birsh is" + totalNum);
      System.out.println("total nember of girls birsh is" + girlNum);
      System.out.println("total nember of boys birsh is" + boyNum);

  }
  //method for getting a rank for a given year,name and gender
  public int getRank(int year, String name, String gender){
      //create a new directory resource to let me choose many files
      DirectoryResource dr= new DirectoryResource();
      //intialize variables to count and get the rank
      int totalcount=0;
      int countGirl=0;
      int countBoy=0;
      int rank=0;
      //convert the integer year to string to search with it for the file name
      String yearName = Integer.toString(year);
      //for each file in the directory resource
      for (File f: dr.selectedFiles()){
         //if the file name contains the year we're looking for  
         if(f.getName().contains(yearName)){   
           //then create a file resource and a parser for the file
           FileResource fr= new FileResource(f);
           CSVParser parser= fr.getCSVParser(false);
           //for every record in this file 
           for(CSVRecord record: parser){
              totalcount++;    
              //if the record is for a girl name increment girls count
              if(record.get(1).equals("F") ){
                countGirl++;
              }
              //if the name in the record is the name you entered, and gender is the one you entered then the is your rank
              if(record.get(0).equals(name)&& gender.equals("F") && record.get(1).equals(gender)){
                rank = totalcount;
                return totalcount;
              }
              
              else if (record.get(0).equals(name) && gender.equals("M") && record.get(1).equals(gender) ) {
                 countBoy= totalcount-countGirl;

                 return countBoy;


              } 
           }

         }
      }
      //if the name is not found in the file return -1
      return -1;
  }
 //method to get the name of a given rank as the method before but instead of using a name we used a rank
  public String  getName(int year,int rank, String gender){
  DirectoryResource dr= new DirectoryResource();
  String yearName = Integer.toString(year);
  int totalCount=0;
  int countGirl=0;
  int countBoy=0;
  for(File f: dr.selectedFiles()){
     if(f.getName().contains(yearName)){
       FileResource fr= new FileResource(f);
       CSVParser parser= fr.getCSVParser(false);
       for(CSVRecord record: parser){
          totalCount++;
          if(record.get(1).equals("F") ){
            countGirl++;
          }
          if(rank== countGirl&& gender.equals("F") && record.get(1).equals(gender)){
            return record.get(0);
          }

          else if ((totalCount-countGirl)== rank && gender.equals("M") && record.get(1).equals(gender) ) {
               return record.get(0);
          }
       }
     }
  }
  //if the rank given is not found return no name found
  return "NO NAME";
 }
  //method to get your name at a different year based on your rank in your birth year 
  public void whatIsNameInYear(String name, int year,int newYear, String gender){
     //first get your rank by using getRank method
     int rank=getRank(year,name,gender);
     //then get your new name by giving the rank you got to getName method
     String newName= getName(newYear, rank,gender);
     if(rank== -1 || newName.equals("NO NAME")){
       System.out.println("No Name Found");
     }
     else{
         System.out.println(name + "born in" + year + "would be" + newName + "in" + newYear);
     }
  }
  //method to get the integer value from a file name that has the string value of this year
  public int getYear(File f){
    //first get the file name
    String fileName= f.getName();
    String year="";
    int yearInt=0;
    //for each character in the name of the file
    for(int i=0; i< fileName.length(); i++){
       //if the ascii value of this character is between 48 and 57(ascii values for the numbers as char)
       if(fileName.charAt(i)>= 48 && fileName.charAt(i) <= 57){
          //add this character to the string year
          year+= fileName.charAt(i);

       }

    }
    //convert the string year to integer
    yearInt= Integer.parseInt(year);
    return yearInt;
  }
  //method to get the rank without having a specific year to look for 
  public int rank(File f, String name, String gender){
    int totalcount=0;
    int countGirl=0;
    int countBoy=0;
    int rank=0;
    FileResource fr= new FileResource(f);
    CSVParser parser= fr.getCSVParser(false);
    for(CSVRecord record: parser){
       totalcount++;    
       if(record.get(1).equals("F") ){
         countGirl++;
       }
       if(record.get(0).equals(name)&& gender.equals("F") && record.get(1).equals(gender)){
         rank = totalcount;
         return totalcount;
       }
       else if (record.get(0).equals(name) && gender.equals("M") && record.get(1).equals(gender) ) {
             countBoy= totalcount-countGirl;
             return countBoy;
       }
    }
    return -1;
  }
  //method to get the year that has the biggest number for babies named by a given name and has a given gender
  public int yearOfHighestRank(String name, String gender){
     DirectoryResource dr= new DirectoryResource();
     int currentRank=0;
     int highestRank=0;
     int currentYear=0;
     int highestYear=0;
     for(File f: dr.selectedFiles()){
        //get the year name in integer value from the file name using getYear method
        currentYear=getYear(f);  
        //get the rank for this year using getRank method
        currentRank= getRank(currentYear, name, gender);
        //if we didn't start with any files to compare yet then start with the first one as your highest
        if(highestRank==0 && currentRank!= -1){
          highestRank=currentRank;
          highestYear=currentYear;
        }
        //if we have started a;lready then compare the current rank for this file with the highest you got so far
        else if(currentRank != -1 && currentRank<  highestRank ){
           highestRank = currentRank;
           highestYear=currentYear;
        }
     } 
     if (highestRank ==0){
     return -1;
     }
     return highestYear;
  }
   //method to get the average rank for a baby name through many years
   public double getAverageRank(String name, String gender){
     DirectoryResource dr= new DirectoryResource();
     int rank=0;
     int sum=0;
     double avg=0.0;
     int count=0;
     for (File f: dr.selectedFiles()){    
        //get th rank by using the rank method intead of getRank as it doesn't need a year to work
        rank= rank(f, name,gender);
        //if there's no name then the rank would be -1 so return -1.0
        if (rank == -1){
          return -1.0;
        }
        //else add every rank you get to the variable sum
        else {
          count++;
          sum+=rank;
        }
     }
     //calculate the average by using sum and count
     avg= sum/ (double)count;
     return avg;
   }
   //method to get the number of babies born with rank higher than the rank for a given name and have the same gender
   public int getTotalBirthsRankedHigher(int year, String name, String gender){
   //first get the rank for this given name
   int nameRank= getRank(year, name,gender);
   int rank=0;
   int countBirth=0;
   int totalBirth=0;
   FileResource fr=new FileResource();
   CSVParser parser=fr.getCSVParser(false);
   //for every record in the parser
   for(CSVRecord record: parser){
     //if it's the same gender as the given one
     if(record.get(1).equals(gender)){
       //covert the string value of the babies number into an integer value
       countBirth= Integer.parseInt(record.get(2));
       //get the rank of this baby's name
       rank=getRank(year,record.get(0), gender);
       //if its rank is higher than the given name's rank then add the number of babies with this name to count birth
       if(rank< nameRank){
         totalBirth+= countBirth;
       }
     }

   }
   if (totalBirth>0){
     return totalBirth;
   }
   return -1;
  }
  //method to test totalBirths method
  public void testTotalBirths (){
       FileResource fr= new FileResource();
       CSVParser parser = fr.getCSVParser(false);
       totalBirth(parser); 
    }

}
