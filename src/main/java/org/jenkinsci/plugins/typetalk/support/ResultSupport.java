package org.jenkinsci.plugins.typetalk.support;

import hudson.model.Job;
import hudson.model.Result;
import hudson.model.Run;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;

public class ResultSupport {

    public TypetalkMessage convertBuildToMessage(Run build) {
        if (isSuccessCurrentBuild(build)) {
            if (isSuccessFromFailure(build)) {
                return new TypetalkMessage(Emoji.SMILEY, "Build recovery");
            } else {
                return new TypetalkMessage(Emoji.SMILEY, "Build success");
            }
        } else if (build.getResult().equals(Result.ABORTED)) {
            return new TypetalkMessage(Emoji.ASTONISHED, "Build aborted");
        } else if (build.getResult().equals(Result.NOT_BUILT)) {
            return new TypetalkMessage(Emoji.ASTONISHED, "Not built");
        } else if (build.getResult().equals(Result.FAILURE)) {
            return new TypetalkMessage(Emoji.RAGE, "Build failure");
        } else if (build.getResult().equals(Result.UNSTABLE)) {
            return new TypetalkMessage(Emoji.CRY, "Build unstable");
        }

        throw new IllegalArgumentException("Unknown build result.");
    }

    public boolean isSuccessFromSuccess(Run build) {
        return isSuccessCurrentBuild(build) && isSuccessPreviousBuild(build) ;
    }

    private boolean isSuccessFromFailure(Run build) {
        return isSuccessCurrentBuild(build) && !isSuccessPreviousBuild(build);
    }

    private boolean isSuccessCurrentBuild(Run build) {
        return build instanceof WorkflowRun ? build.getResult() == null : build.getResult().equals(Result.SUCCESS);
    }

    private boolean isSuccessPreviousBuild(Run build) {
        if (build.getPreviousCompletedBuild() == null) {
            // as success when this build is 1st build
            return true;
        }

        return build.getPreviousCompletedBuild().getResult().equals(Result.SUCCESS);
    }

    public Emoji convertProjectToEmoji(Job project) {
        switch (project.getIconColor()) {
            case RED:
            case RED_ANIME:
                return Emoji.RAGE;
            case YELLOW:
            case YELLOW_ANIME:
                return Emoji.CRY;
            case BLUE:
            case BLUE_ANIME:
                return Emoji.SMILEY;
            case DISABLED:
            case DISABLED_ANIME:
            case NOTBUILT:
            case NOTBUILT_ANIME:
                return Emoji.MASK;
            default:
                return Emoji.ASTONISHED;
        }
    }

}