package utils;

import java.io.*;

import exceptions.fileReaderException;

public class fileReader {
    public String read(String filepath) throws fileReaderException {
        File file = null;
        FileReader fr = null;
        BufferedReader br = null;
        StringBuilder sb = null;
        try {
            file = new File(filepath);
            if(!file.exists() || !file.isFile()) {
                // do something here
                throw new fileReaderException();
            } else {
                fr = new FileReader(file);
                br = new BufferedReader(fr);
                sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                String everything = sb.toString();
                if(fr != null) {
                    fr.close();
                }
                if(br != null) {
                    br.close();
                }
                if(sb != null) {
                    sb.setLength(0);
                }
                return everything.trim();
            }
        } catch (Exception e) {
            try {
                if(fr != null) {
                    fr.close();
                }
                if(br != null) {
                    br.close();
                }
                if(sb != null) {
                    sb.setLength(0);
                }
            } catch(Exception e2) {
            }
            throw new fileReaderException();
        }
    }
}
