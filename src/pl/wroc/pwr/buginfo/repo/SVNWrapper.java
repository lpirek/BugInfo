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
public interface SVNWrapper {
    
    ProjectInfo getBugs(String repositoryUrl, long startRevision);
    
    void setSrcPathPrefix( String regexPre );
    
    void setSrcPathPostfix( String regexPost );
    
    void setFileSeparator( char sep );

}
