package com.util;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import java.io.File;
import java.io.FileFilter;

import java.util.List;

import com.RMT2Base;

/**
 * An implementation of FileFilter that provides functionality to query a
 * directory for only the files that fit a certain criteria. The format of the
 * criteria will most likely represent a wildcard construct as String. For
 * example, "*.txt" or "acct_*.*".
 * 
 * @author appdev
 * 
 */
public class RMT2FileDirectoryFilter extends RMT2Base implements FileFilter {
    private static Logger logger = Logger
            .getLogger(RMT2FileDirectoryFilter.class);

    private List<String> wildcards;

    private static final String WILDCARD_SEP = ";";

    /**
     * Creates a RMT2FileDirectoryFilter that does not apply any filtering to
     * the directory query.
     */
    public RMT2FileDirectoryFilter() {
        RMT2FileDirectoryFilter.logger
                .log(Level.INFO, RMT2Base.LOGGER_INIT_MSG);
        return;
    }

    /**
     * Creates a RMT2FileDirectoryFilter with the ability to filter files by one
     * or more wild card criteria.
     * 
     * @param criteria
     *            filename wildcard criteria. Multiple wildcard combinations are
     *            allow as long as each wildcard criteria is separated by the
     *            character, ";". Can be null if no filtering is needed.
     */
    public RMT2FileDirectoryFilter(String criteria) {
        this();
        this.wildcards = this.setupWildcardCriteria(criteria);
    }

    /**
     * Creates a list of filename wildcard Strings by parsing <i>criteria</i>.
     * 
     * @param criteria
     *            One or more wildcard notations separated by the character,
     *            ";".
     * @return List of String where each String value is a wildcard.
     */
    private List<String> setupWildcardCriteria(String criteria) {
        if (criteria == null || criteria.length() <= 0) {
            return null;
        }
        List<String> tokens = RMT2String.getTokens(criteria,
                RMT2FileDirectoryFilter.WILDCARD_SEP);
        return tokens;
    }

    /**
     * Test whether or not a specified file is to be included as a result of a
     * directory listing based on some criteria.
     * 
     * @param pathname
     *            the file to be tested.
     * @return boolean true if and only if <i>pathname</i> matches one of the
     *         wildcard criteria or if the wildcard criteria does not exist.
     *         Otherwise, false is returned.
     */
    public boolean accept(File file) {
        if (this.wildcards == null) {
            return true;
        }
        String fileName = file.getName();
        for (String criteria : this.wildcards) {
            String sWild = this.replaceWildcards(criteria);
            boolean match = (fileName.matches(sWild));
            if (match) {
                return match;
            }
        }
        return false;
    }

    /**
     * Checks for * and ? in the wildcard variable and replaces them correct
     * pattern characters.
     * 
     * @param wild
     *            wildcard name containing * and ?
     * @return String the modified wildcard name
     */
    private String replaceWildcards(String wild) {
        StringBuffer buffer = new StringBuffer();
        char[] chars = wild.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            if (chars[i] == '*') {
                buffer.append(".*");
            }
            else if (chars[i] == '?') {
                buffer.append(".");
            }
            else if ("+()^$.{}[]|\\".indexOf(chars[i]) != -1) {
                buffer.append('\\').append(chars[i]);
            }
            else {
                buffer.append(chars[i]);
            }
        }
        return buffer.toString();
    }

}
