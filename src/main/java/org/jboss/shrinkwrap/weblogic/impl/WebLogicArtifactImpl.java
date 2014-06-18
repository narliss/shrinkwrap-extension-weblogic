package org.jboss.shrinkwrap.weblogic.impl;

import org.jboss.shrinkwrap.weblogic.api.WebLogicArtifact;

/**
 * Default implementation of {@link org.jboss.shrinkwrap.weblogic.api.WebLogicArtifact}.
 *
 * @author Patrick Peralta, Noah Arliss
 */
public class WebLogicArtifactImpl implements WebLogicArtifact {
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
    public WebLogicArtifact setSharedLibrary(boolean sharedLibrary) {
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
    public WebLogicArtifact setStageMode(StageMode stageMode) {
        this.stageMode = stageMode;
        return this;
    }
}
