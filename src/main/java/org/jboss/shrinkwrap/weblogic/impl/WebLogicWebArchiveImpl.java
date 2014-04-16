package org.jboss.shrinkwrap.weblogic.impl;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.impl.base.spec.WebArchiveImpl;
import org.jboss.shrinkwrap.weblogic.api.WebLogicArtifactAttributes;
import org.jboss.shrinkwrap.weblogic.api.WebLogicWebArchive;

/**
 * Default implementation of {@link WebLogicWebArchive}.
 *
 * @author Patrick Peralta, Noah Arliss
 */
public class WebLogicWebArchiveImpl extends WebArchiveImpl implements WebLogicWebArchive {
    private final WebLogicArtifactAttributes webLogicArtifactAttributes = new WebLogicArtifactAttributesImpl();

    /**
     * Create a new WebLogicWebArchive with any type storage engine as backing.
     *
     * @param delegate the archive to delegate to
     */
    public WebLogicWebArchiveImpl(Archive<?> delegate) {
        super(delegate);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSharedLibrary() {
        return webLogicArtifactAttributes.isSharedLibrary();
    }

    /**
     * {@inheritDoc}
     */
    public WebLogicArtifactAttributes setSharedLibrary(boolean sharedLibrary) {
        return webLogicArtifactAttributes.setSharedLibrary(sharedLibrary);
    }

    /**
     * {@inheritDoc}
     */
    public StageMode getStageMode() {
        return webLogicArtifactAttributes.getStageMode();
    }

    /**
     * {@inheritDoc}
     */
    public WebLogicArtifactAttributes setStageMode(StageMode stageMode) {
        return webLogicArtifactAttributes.setStageMode(stageMode);
    }
}
