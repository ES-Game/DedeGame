package com.quangph.base.thread;

import android.os.AsyncTask;

/**
 * Created by QuangPH on 2020-01-11.
 */
public abstract class BaseAsyncTask<Params, Progress, Result>
        extends AsyncTask<Params, Progress, BaseAsyncTask.AsyncTaskResult<Result>> {

    abstract protected Result doJob(Params... params) throws Exception;
    abstract public void onPostExecute(Result result, Exception exception);

    @Override
    protected AsyncTaskResult<Result> doInBackground(Params... params) {
        try {
            Result result = doJob(params);
            return new AsyncTaskResult(result);
        } catch (Exception e) {
            return new AsyncTaskResult<>(e);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<Result> resultAsyncTaskResult) {
        super.onPostExecute(resultAsyncTaskResult);
        onPostExecute(resultAsyncTaskResult.result, resultAsyncTaskResult.error);
    }


    public static class AsyncTaskResult<T> {
        T result;
        Exception error;

        public T getResult() {
            return result;
        }

        public Exception getError() {
            return error;
        }

        public AsyncTaskResult(T result) {
            super();
            this.result = result;
        }

        public AsyncTaskResult(Exception error) {
            super();
            this.error = error;
        }
    }
}
