package com.ef.analyzer;

import com.ef.parser.LogRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * A container to  put log record and analyze.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public class LogAnalyzeContainer {

    private HashMap<String, Integer> table = new HashMap<>();

    public void put(LogRecord record) {
        if (table.containsKey(record.getIp())) {
            int i = table.get(record.getIp()) + 1;
            table.put(record.getIp(), i);
        } else {
            table.put(record.getIp(), 1);
        }
    }


    public List<KeyValuePair<String, Integer>> getByThreshold(int threshold) {
        List<KeyValuePair<String, Integer>> ret = new ArrayList<>();
        table.forEach((ip, th) -> {
            if (th >= threshold) {
                ret.add(new KeyValuePair<>(ip, th));
            }
        });
        return ret;
    }

}
