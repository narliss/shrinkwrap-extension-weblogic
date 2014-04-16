package org.jboss.shrinkwrap.weblogic.impl;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.impl.base.spec.EnterpriseArchiveImpl;
import org.jboss.shrinkwrap.weblogic.api.WebLogicArtifactAttributes;
import org.jboss.shrinkwrap.weblogic.api.WebLogicEnterpriseArchive;

/**
 * Default implementation of {@link WebLogicEnterpriseArchive}.
 *
 * @author Patrick Peralta, Noah Arliss
 */
public class WebLogicEnterpriseArchiveImpl extends EnterpriseArchiveImpl implements WebLogicEnterpriseArchive {
    private final WebLogicArtifactAttributes webLogicArtifactAttributes = new WebLogicArtifactAttributesImpl();

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
