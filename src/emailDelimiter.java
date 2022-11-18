import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.exit;

public class emailDelimiter {

    public static void main(String[] args) {
        String userDirectory = Paths.get("").toAbsolutePath().toString();
        Scanner input = new Scanner(System.in);

        System.out.print("Please enter the name of file: " + "\n");
        String fileName = input.nextLine();
        fileName = EnsureTxt(fileName);

        // make sure that file exists in current directory
        File currentFile = new File(userDirectory + "\\" + fileName);
        while(!currentFile.exists() || !currentFile.isFile() || !fileName.contains(".txt")){
            System.out.println(currentFile.getAbsolutePath());
            System.out.println("The file '" + fileName + "' either does not exist or is not a file. Please re-enter the file name ");
            fileName = input.nextLine();
            currentFile = new File(userDirectory + ("\\") + fileName);
        }

        // new file creation
        File outputFile = new File("updatedEmails.txt");
        System.out.println("Attempting to create new file named 'updatedEmails.txt'...");
        try{
            if(outputFile.createNewFile()){
                System.out.println("File has been successfully created.");
            }else{
                System.out.println("File already exists in current directory. Aborting...");
                exit(0);
            }
        }catch(IOException e){
            System.out.println("An unexpected error occurred.");
            exit(0);
        }

        String inputText = "";
        try{
            Scanner fileReader = new Scanner(currentFile);
            while(fileReader.hasNextLine()){
                inputText += fileReader.nextLine();
            }
            fileReader.close();
        }catch(FileNotFoundException e){
            System.out.println("An unexpected error occurred when reading from the input file.");
            exit(0);
        }

        // try to write current file from array list to the output file
        try{
            boolean insideDomain = false;
            int domainLength = 0;
            FileWriter fileWriter = new FileWriter(outputFile.getName());
            for(int i = 0; i < inputText.length(); i++){
                if(i + 3 == inputText.length()){
                    break;
                }

                if(inputText.charAt(i) == '@'){
                    insideDomain = true;
                }
                while(insideDomain){
                    i++;
                    if(insideDomain && inputText.charAt(i) == '.' && ((inputText.charAt(i + 1) == 'c' && inputText.charAt(i + 2) == 'a') || (inputText.charAt(i + 1) == 'c' && inputText.charAt(i + 2) == 'o'&& inputText.charAt(i + 3) == 'm'))){
                        insideDomain = false;
                        StringBuffer str = new StringBuffer(inputText);
                        if(inputText.charAt(i + 3) == 'm'){
                            str.insert(i + 4, ", ");
                        }else {
                            str.insert(i + 3, ", ");
                        }
                        inputText = str.toString();
                        i += 2;

                    }
                }
            }

            // checking for duplicate emails
            String currentEmail;
            int duplicateCount = 0;
            int emailCount = 0;
            int currentEmailEnd;
            ArrayList<String> duplicates = new ArrayList<>();
            ArrayList<String> emails = new ArrayList<String>();
            for(int i = 0; i < inputText.length(); i++){
                currentEmailEnd = i;
                while(currentEmailEnd < inputText.length() && inputText.charAt(currentEmailEnd) != ','){
                    if(currentEmailEnd >= inputText.length()){
                        break;
                    }
                    currentEmailEnd++;
                }
                if(i == 0){
                    currentEmail = inputText.substring(i, currentEmailEnd);
                }else{
                    currentEmail = inputText.substring(i + 1, currentEmailEnd);
                }

                for(String email : emails){
                    if(email.equals(currentEmail)){
                        duplicateCount++;
                        duplicates.add(currentEmail);
                    }
                }
                emails.add(currentEmail);
                emailCount++;
                i = currentEmailEnd;
            }
            System.out.println("Email Count: " + emailCount);
            System.out.println(duplicateCount + " duplicate(s) found.");
            if(duplicateCount > 0){
                System.out.println("***DUPLICATES LISTED BELOW***");
                for(String duplicate : duplicates){
                    System.out.println(duplicate);
                }
            }
            fileWriter.write(inputText);
            fileWriter.close();
        }catch(IOException e){
            System.out.println("An unexpected error occurred when writing to the output file.");
            exit(0);
        }

        System.out.println("All read and written successfully.");
    }

    public static String EnsureTxt(String fileName){
        Scanner fileInput = new Scanner(System.in);
        // ensure that file has a .txt extension
        if(fileName.contains(".txt")){
            return fileName;
        }else{
            // prompt for file entry until correct file name is entered
            do{
                System.out.println("You have entered an invalid file name.");
                System.out.println("Please enter a file name that includes '.txt'.");
                fileName = fileInput.nextLine();
            }while(!fileName.contains(".txt"));
            return fileName;
        }
    }
}


