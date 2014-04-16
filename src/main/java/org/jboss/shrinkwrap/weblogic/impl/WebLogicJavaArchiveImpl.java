package org.jboss.shrinkwrap.weblogic.impl;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.impl.base.spec.JavaArchiveImpl;
import org.jboss.shrinkwrap.weblogic.api.WebLogicArtifactAttributes;
import org.jboss.shrinkwrap.weblogic.api.WebLogicJavaArchive;

/**
 * Default implementation of {@link WebLogicJavaArchive}.
 *
 * @author Patrick Peralta, Noah Arliss
 */
public class WebLogicJavaArchiveImpl extends JavaArchiveImpl implements WebLogicJavaArchive {
    private final WebLogicArtifactAttributes webLogicArtifactAttributes = new WebLogicArtifactAttributesImpl();

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
