
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

public class RecordData {
	
	private CSVWriter w;
	private FileWriter wr;
    
    public RecordData(String filePath, String[] info) {
        if (filePath == null) {
            throw new IllegalArgumentException();
        }
        try {
        	File file = new File(filePath);
        	
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);
      
            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);
            this.wr = outputfile;
            
            this.w = writer;		
            
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public void writeData(String[] data) {
    	w.writeNext(data);
    }
    
    public void closeWriter() throws IOException {
    	wr.flush();
    	w.close();
    	
    }

}
