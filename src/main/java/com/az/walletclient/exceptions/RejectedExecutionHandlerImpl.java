package com.az.walletclient.exceptions;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author - Azhar Mobeen
 *
 * Description:
 *  =>  I'm catching all the thread those are rejected from queued then I'm adding again into blocking queue.
 */
public class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try {
            executor.getQueue().put(r);
        }
        catch (InterruptedException e) {
            throw new RejectedExecutionException("InterruptedException occurs while waiting to add a Runnable in the executor's blocking queue", e);
        }
    }
}
