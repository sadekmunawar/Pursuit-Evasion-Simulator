
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import com.opencsv.CSVWriter;

/*
 * Author: Sadek Munawar
 * Date: June 2021
 * 
 * Description: Write the data from a simulation to a file.
 */
public class RecordData {
  private CSVWriter w;
  private FileWriter wr;

  public RecordData(String filePath, String[] info) {
    if (filePath == null) {
      throw new IllegalArgumentException();
    }
    try {
      final File file = new File(filePath);

      // create FileWriter object with file as parameter
      final FileWriter outputfile = new FileWriter(file);

      // create CSVWriter object filewriter object as parameter
      final CSVWriter writer = new CSVWriter(outputfile);
      this.wr = outputfile;

      this.w = writer;

    } catch (final FileNotFoundException e) {
      throw new IllegalArgumentException();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }


  public void closeWriter() throws IOException {
    wr.flush();
    w.close();

  }

  public void writeData(String[] data) {
    w.writeNext(data);
  }

}
