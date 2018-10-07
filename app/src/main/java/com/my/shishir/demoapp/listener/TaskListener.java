package com.my.shishir.demoapp.listener;

import android.support.annotation.NonNull;

/**
 * A parameterized task result that can be used for callbacks from any task.
 */
public interface TaskListener<T> {

    /**
     * Called when the task has succeeded.
     */
    void onSuccess(@NonNull T result);

    /**
     * Called when the task has failed.
     * Note that the errorMessageResource is usually a string resource type, but we cannot
     * annotate it that way because it can also be 0.
     *
     * @param errorMessageResource user-oriented error message, or 0 if no error should be
     *                             displayed - typically because it's already been shown.
     * @param result result in case of failure
     */
    void onFailure(@NonNull T result, int errorMessageResource);
}
