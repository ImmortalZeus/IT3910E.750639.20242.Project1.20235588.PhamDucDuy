package fileParsers;

import lineParsers.*;
import utils.*;
import exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;

public class nginxFileParser {
    private static fileReader fR = new fileReader();
    private static nginxLineParser nLP = new nginxLineParser();

    public ArrayList<HashMap<String, String>> parse(String filepath) throws fileParserException {
        try {
            ArrayList<HashMap<String, String>> res = new ArrayList<HashMap<String, String>>();

            String filedata = fR.read(filepath);
            String[] lines = filedata.split("\n");

            for (int i = 0; i < lines.length; i++) {
                HashMap<String, String> parsed = nLP.parse(lines[i]);
                res.add(parsed);
            }
            return res;
        } catch (Exception e) {
            throw new fileParserException();
        }
    }
}