package org.jboss.shrinkwrap.weblogic.impl;

import org.jboss.shrinkwrap.weblogic.api.WebLogicArtifactAttributes;

/**
 * Default implementation of {@link WebLogicArtifactAttributes}.
 *
 * @author Patrick Peralta, Noah Arliss
 */
public class WebLogicArtifactAttributesImpl implements WebLogicArtifactAttributes {
    /**
     * Specifies if the artifact is a shared library.
     */
    private boolean sharedLibrary = false;

    /**
     * Specifies stage behavior upon deployment.
     */
    private StageMode stageMode = StageMode.NO_STAGE;

    /**
     * {@inheritDoc}
     */
    public boolean isSharedLibrary() {
        return sharedLibrary;
    }

    /**
     * {@inheritDoc}
     */
    public WebLogicArtifactAttributes setSharedLibrary(boolean sharedLibrary) {
        this.sharedLibrary = sharedLibrary;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public StageMode getStageMode() {
        return stageMode;
    }

    /**
     * {@inheritDoc}
     */
    public WebLogicArtifactAttributes setStageMode(StageMode stageMode) {
        this.stageMode = stageMode;
        return this;
    }
}
