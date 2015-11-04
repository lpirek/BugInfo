/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.wroc.pwr.buginfo.repo;

import java.util.regex.Pattern;
import pl.wroc.pwr.buginfo.*;

/**
 *
 * @author marian
 */
public interface RepoWrapper {
    
    public ProjectInfo getBugs( Properties config);
    
    void setSrcPathPrefix( String regexPre );
    String getSrcPathPrefix();
    
    void setSrcPathPostfix( String regexPost );
    String getSrcPathPostfix();
    
    void setFileSeparator( Character sep );
    char getFileSeparator();
    
    void setBugfixPattern(String regex);
    String getBugfixPattern();

}
