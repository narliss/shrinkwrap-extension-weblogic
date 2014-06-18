package org.jboss.shrinkwrap.weblogic.impl;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.impl.base.spec.EnterpriseArchiveImpl;
import org.jboss.shrinkwrap.weblogic.api.WebLogicArtifact;
import org.jboss.shrinkwrap.weblogic.api.WebLogicEnterpriseArchive;

/**
 * Default implementation of {@link WebLogicEnterpriseArchive}.
 *
 * @author Patrick Peralta, Noah Arliss
 */
public class WebLogicEnterpriseArchiveImpl extends EnterpriseArchiveImpl implements WebLogicEnterpriseArchive {
    private final WebLogicArtifact webLogicArtifact = new WebLogicArtifactImpl();

    /**
     * Create a new WebLogicEnterpriseArchive with any type storage engine as backing.
     *
     * @param delegate the archive to delegate to
     */
    public WebLogicEnterpriseArchiveImpl(Archive<?> delegate) {
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
