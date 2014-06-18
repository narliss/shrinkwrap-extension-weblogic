package org.jboss.shrinkwrap.weblogic.impl;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.impl.base.spec.JavaArchiveImpl;
import org.jboss.shrinkwrap.weblogic.api.WebLogicArtifact;
import org.jboss.shrinkwrap.weblogic.api.WebLogicJavaArchive;

/**
 * Default implementation of {@link WebLogicJavaArchive}.
 *
 * @author Patrick Peralta, Noah Arliss
 */
public class WebLogicJavaArchiveImpl extends JavaArchiveImpl implements WebLogicJavaArchive {
    private final WebLogicArtifact webLogicArtifact = new WebLogicArtifactImpl();

    /**
     * Create a new WebLogicJavaArchive with any type storage engine as backing.
     *
     * @param delegate the archive to delegate to
     */
    public WebLogicJavaArchiveImpl(Archive<?> delegate) {
        super(delegate);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSharedLibrary() {
        return webLogicArtifact.isSharedLibrary();
    }

    /**
     * {@inheritDoc}
     */
    public WebLogicArtifact setSharedLibrary(boolean sharedLibrary) {
        return webLogicArtifact.setSharedLibrary(sharedLibrary);
    }

    /**
     * {@inheritDoc}
     */
    public StageMode getStageMode() {
        return webLogicArtifact.getStageMode();
    }

    /**
     * {@inheritDoc}
     */
    public WebLogicArtifact setStageMode(StageMode stageMode) {
        return webLogicArtifact.setStageMode(stageMode);
    }
}
