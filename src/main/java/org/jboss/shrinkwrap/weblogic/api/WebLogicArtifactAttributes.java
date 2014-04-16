package org.jboss.shrinkwrap.weblogic.api;

/**
 * Interface definition for a WebLogic archive that supports
 * WebLogic specific attributes:
 * <ul>
 * <li>Stage mode: indicates the method by which deployment
 * artifacts will be staged during deployment</li>
 * <li>Shared library: indicates whether the artifact is
 * a shared library.</li>
 * </ul>
 *
 * @author Patrick Peralta, Noah Arliss
 */
public interface WebLogicArtifactAttributes {
    /**
     * WebLogic staging modes. Invoke the {@code toString} method
     * in order to obtain the string to pass to the WebLogic
     * deployment tool.
     */
    enum StageMode {
        /**
         * Specifies that the deployment will not be copied to target servers.
         */
        NO_STAGE {
            public String toString() {
                return "nostage";
            }
        },

        /**
         * Specifies that WebLogic Server will copy the application to the
         * target server's staging directory.
         */
        STAGE {
            public String toString() {
                return "stage";
            }
        },

        /**
         * Indicates that the files will be expected in the target server's
         * staging directory, but the server will not copy them there.
         * The user is responsible for distributing files.
         */
        EXTERNAL_STAGE {
            public String toString() {
                return "external_stage";
            }
        }
    }

    /**
     * Return {@code true} if the artifact is a shared library.
     *
     * @return true if the artifact is a shared library.
     */
    boolean isSharedLibrary();

    /**
     * Configure the artifact as a shared library.
     *
     * @param sharedLibrary if {@code true}, the artifact is
     *                      configured as a shared library
     * @return this object
     */
    WebLogicArtifactAttributes setSharedLibrary(boolean sharedLibrary);

    /**
     * Return the stage mode for the artifact.
     *
     * @return the stage mode for the artifact
     */
    StageMode getStageMode();

    /**
     * Configure the stage mode for the artifact.
     *
     * @param stageMode stage mode for the artifact
     * @return this object
     */
    WebLogicArtifactAttributes setStageMode(StageMode stageMode);
}
