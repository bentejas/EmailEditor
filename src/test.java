import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.exit;

public class test {
    public static void main (String args[]){
        int numFiles;
        String userDirectory = Paths.get("").toAbsolutePath().toString();
        Scanner input = new Scanner(System.in);
        // ask user for number of files to be inputted
        System.out.println("Please enter the number of files your would like to input");

        // validation check to make sure that number of files is above 0
        do{
            numFiles = input.nextInt();
            if(numFiles <= 0){
                System.out.println("You have entered an invalid number of files. Please input 1 or more files.");
            }
        }while(numFiles <= 0);
        input.nextLine();
        ArrayList<File> files = new ArrayList<File>();
        String fileName;

        // for loop to collect all file names
        for(int i = 0; i < numFiles; i++){
            System.out.print("Please enter the name of file number " + (i + 1) + "\n");
            fileName = input.nextLine();
            fileName = EnsureTxt(fileName);

            // make sure that file exists in current directory
            File currentFile = new File(userDirectory + "\\" + fileName);
            while(!currentFile.exists() || !currentFile.isFile() || !fileName.contains(".txt")){
                System.out.println(currentFile.getAbsolutePath());
                System.out.println("The file '" + fileName + "' either does not exist or is not a file. Please re-enter the file name ");
                fileName = input.nextLine();
                currentFile = new File(userDirectory + ("\\") + fileName);
            }
            // add the currently verified file to the file arraylist
            files.add(currentFile);
        }

        // new file creation
        File outputFile = new File("final.txt");
        System.out.println("Attempting to create new file named 'final.txt'...");
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

        // read the current file and store in a string
        String inputText = "";
        for(File file : files){
            try{
                Scanner fileReader = new Scanner(file);
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
                FileWriter fileWriter = new FileWriter(outputFile.getName());
                inputText += "\n";
                fileWriter.write(inputText);
                fileWriter.close();
                System.out.println("Successfully wrote file number " + (files.indexOf(file) + 1) + " to the output file.");
            }catch(IOException e){
                System.out.println("An unexpected error occurred when writing to the output file.");
                exit(0);
            }
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
