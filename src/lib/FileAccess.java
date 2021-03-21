package lib;


import java.io.*;
import java.util.*;


public class FileAccess{

	public static File freeFile(String pathfile){
		File file = new File(pathfile);
		if (!file.delete()){
			System.out.println("file '" + pathfile + "' Not successfully deleted");
		}
		try{
			if (!file.createNewFile()){
				System.out.println("file '" + pathfile + "' Not successfully created");
			}
		}catch (IOException e){
			e.printStackTrace();
		}
		return file;
	}

	public static void writeLine(String pathfile, String record) throws IOException{// the record contains '\n'
		File file = new File(pathfile);
		if (file.exists()){
			FileWriter writer = new FileWriter(file, true);
			writer.write(record);
			writer.close();
		}else{
		  	FileNotFoundException excep = new FileNotFoundException();
			excep.initCause(new FileNotFoundException());
			throw(excep);
		}
	}


	public static String readLine(String pathfile, int lineNumber){
    	String line = "";
		try{
			Scanner reader = new Scanner(new File(pathfile));
			for (int i=0; i<lineNumber; i++){
    			if (reader.hasNextLine()) 
					line = reader.nextLine();
			}
			reader.close();
		}catch(FileNotFoundException exc){
			exc.printStackTrace();
		}
		return line;
	}


	public static String readLast(String pathfile){
    	String line = "";
		try{
			Scanner reader = new Scanner(new File(pathfile));
			while (reader.hasNextLine()) {
				line = reader.nextLine();
			}
			reader.close();
		}catch(FileNotFoundException exc){
			exc.printStackTrace();
		}
		return line;
	}


	public static void eraseLast(String filePath) {
		StringBuilder s = new StringBuilder();
		try{
			Scanner reader = new Scanner(new File(filePath));
			String bkToline = "";
			while (reader.hasNextLine()) {
    		    String line = reader.nextLine();
    		    if (reader.hasNextLine()) {
					s.append(bkToline);
					bkToline = "\n";
    		        s.append(line);
    		    }
    		}
			reader.close();
		}catch(FileNotFoundException exc){
			exc.printStackTrace();
		}
    	try {
			FileWriter fWriter = new FileWriter(filePath);
    	    BufferedWriter writer = new BufferedWriter(fWriter);
    	    writer.write(s.toString());
    	    writer.close();
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
	}
}
