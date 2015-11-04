/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.wroc.pwr.buginfo.repo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.netbeans.lib.cvsclient.command.log.LogInformation;
import org.netbeans.lib.cvsclient.commandLine.BasicListener;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;
import pl.wroc.pwr.buginfo.*;

/**
 *
 * @author marian
 */
public abstract class AbstractRepoLogListener extends BasicListener{//CVSAdapter{
    protected ProjectInfo mResult;
    protected Pattern mBugfixPattern;
    protected String mPrefix;
    protected String mPostfix;
    protected char mFileSeparator;
    protected boolean mReadModifications;
    protected boolean mReadCommiters;
    protected boolean mReadModifiedLines;
    protected boolean  mReadBugs;

    protected AbstractRepoLogListener( ProjectInfo result, String bugfixPattern, String prefixPattern, String postfixPattern, char fileSeparator ){
        mResult = result;
        if( bugfixPattern!=null )
            mBugfixPattern = Pattern.compile(bugfixPattern);
        
        if( prefixPattern == null ){
            mPrefix = new String("");
        }
        else{
            mPrefix = prefixPattern;
        }
        if( postfixPattern == null ){
            mPostfix = new String ("");
        }
        else{
            mPostfix = postfixPattern;
        }
        mFileSeparator = fileSeparator;
    }

    boolean isBugFix(String message) {
        if (mBugfixPattern == null) {
            return true;
        }
        Matcher res = mBugfixPattern.matcher(message);
        return res.matches();
    }

    boolean isPathToClass(String path) {
        return path.matches(mPrefix + ".*" + mPostfix);
    }

    String convertPathToClassName(String path) {
        if (isPathToClass(path)) {
            String[] tab = path.split(mPrefix, 2);
            path = tab[1];
            StringBuffer sb = new StringBuffer();
            tab = path.split(mPostfix);
            sb.append(tab[0]);
            for (int i = 1; i < tab.length; i++) {
                sb.append(mPostfix);
                sb.append(tab[i]);
            }
            return convertFileSeparator(sb.toString());
        } else {
            return path;
        }
    }

    String convertFileSeparator(String path) {
        return path.replace(mFileSeparator, '.');
    }

}
