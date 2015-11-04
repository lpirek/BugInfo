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
public class RepoWrapperFactory {

    static public RepoWrapper createWrapper(Properties pr) {
        RepoWrapper repo = null;
        if( pr.getRepotype().compareToIgnoreCase("cvs") == 0 ){
            repo = createCVSWrapper(pr);
        }
        else if( pr.getRepotype().compareToIgnoreCase("svn") == 0 ){
            repo = createSVNWrapper(pr);
        }
        setRepoProps(repo,pr);
        return repo;
    }

    private static RepoWrapper createSVNWrapper(Properties pr){
        SVNWrapperImpl svn =  new SVNWrapperImpl();
        svn.setPass( pr.getRepoSvnPass());
        svn.setUser( pr.getRepoSvnUser());
        return svn;
    }

    private static RepoWrapper createCVSWrapper(Properties pr){
        CVSWrapperImpl cvs = new CVSWrapperImpl();
        cvs.setWorkingCopy( pr.getRepoworkingcopy() );
        return cvs;
    }

    private static void setRepoProps(RepoWrapper repoToConfigure, Properties propsSource) {
        repoToConfigure.setBugfixPattern( propsSource.getRepobugfixregex() );
        repoToConfigure.setFileSeparator( propsSource.getRepofileseparator() );
        repoToConfigure.setSrcPathPrefix( propsSource.getReposrcpathprefixregex() );
        repoToConfigure.setSrcPathPostfix( propsSource.getReposrcpathpostfixregex() );
    }
}
