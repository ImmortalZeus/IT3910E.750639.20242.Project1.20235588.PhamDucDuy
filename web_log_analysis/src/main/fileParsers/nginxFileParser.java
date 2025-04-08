package fileParsers;

import lineParsers.*;
import logData.logData;
import logData.logDataArray;
import utils.*;
import exceptions.*;

import java.util.ArrayList;

public class nginxFileParser {
    private static fileReader fR = new fileReader();
    private static nginxLineParser nLP = new nginxLineParser();

    public logDataArray parse(String filepath) throws fileParserException {
        try {
            ArrayList<logData> res = new ArrayList<logData>();

            String filedata = fR.read(filepath);
            String[] lines = filedata.split("\n");

            for (int i = 0; i < lines.length; i++) {
                logData parsed = nLP.parse(lines[i]);
                res.add(parsed);
            }
            return (new logDataArray(res));
        } catch (Exception e) {
            throw new fileParserException();
        }
    }
}