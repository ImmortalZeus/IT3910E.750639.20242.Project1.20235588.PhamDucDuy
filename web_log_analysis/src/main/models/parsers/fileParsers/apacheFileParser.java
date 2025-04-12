package models.parsers.fileParsers;

import models.exceptions.*;
import models.logData.logData;
import models.logData.logDataArray;
import models.parsers.lineParsers.*;
import models.utils.*;

import java.util.ArrayList;

public class apacheFileParser {
    private static fileReader fR = new fileReader();
    private static apacheLineParser aLP = new apacheLineParser();

    public logDataArray parse(String filepath) throws fileParserException {
        try {
            ArrayList<logData> res = new ArrayList<logData>();

            String filedata = fR.read(filepath);
            String[] lines = filedata.split("\n");
            
            for (int i = 0; i < lines.length; i++) {
                logData parsed = aLP.parse(lines[i]);
                res.add(parsed);
            }
            return (new logDataArray(res));
        } catch (Exception e) {
            throw new fileParserException();
        }
    }
}
