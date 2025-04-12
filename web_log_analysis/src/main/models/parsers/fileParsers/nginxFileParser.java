package models.parsers.fileParsers;

import models.exceptions.*;
import models.logData.logData;
import models.logData.logDataArray;
import models.parsers.lineParsers.*;
import models.utils.*;

import java.util.ArrayList;

public class nginxFileParser {
    private static fileReader fR = new fileReader();
    private static nginxLineParser nLP = new nginxLineParser();

    public logDataArray parse(String filepath) throws fileParserException {
        int j = 0;
        try {
            ArrayList<logData> res = new ArrayList<logData>();

            String filedata = fR.read(filepath);
            String[] lines = filedata.split("\n");

            for (int i = 0; i < lines.length; i++) {
                j = i;
                logData parsed = nLP.parse(lines[i]);
                res.add(parsed);
            }
            return (new logDataArray(res));
        } catch (Exception e) {
            System.out.println(j);
            throw new fileParserException();
        }
    }
}