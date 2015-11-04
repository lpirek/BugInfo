/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.wroc.pwr.buginfo.repo;

import pl.wroc.pwr.buginfo.*;

/**
 *
 * @author marian
 */
public class AbstractRepoWrapperImpl implements RepoWrapper{

    String mPrefix;
    String mPostfix;
    char mFileSeparator='/';
    String mBugfixPattern;
    
    public AbstractRepoWrapperImpl(){
        mPrefix = new String("");
        mPostfix = new String("");
    }

    @Override
    public ProjectInfo getBugs( Properties config) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSrcPathPrefix(String regexPre) {
        if( regexPre==null ){
            return;
        }
        mPrefix = regexPre;
    }

    @Override
    public void setSrcPathPostfix(String regexPost) {
        if( regexPost==null ){
            return;
        }
        mPostfix = regexPost;
    }
    
    /** '/' is the default file separator */
    @Override
    public void setFileSeparator(Character sep) {
        if( sep == null ){
            return;
        }
        mFileSeparator = sep.charValue();
    }

    @Override
    public void setBugfixPattern(String regex) {
        if( regex==null ){
            return;
        }
        mBugfixPattern = regex;
    }

    @Override
    public String getSrcPathPrefix() {
        return mPrefix;
    }

    @Override
    public String getSrcPathPostfix() {
        return mPostfix;
    }

    @Override
    public char getFileSeparator() {
        return mFileSeparator;
    }

    @Override
    public String getBugfixPattern() {
        return mBugfixPattern;
    }

}
