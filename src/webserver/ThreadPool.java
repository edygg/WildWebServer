
package webserver;

/**
 *
 * @author Edilson Gonzalez
 */

public class ThreadPool {
    
    private BlockingQueue queue = new BlockingQueue();
    private boolean closed = true;
    private int poolSize = 32;
    
    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }
    
    public int getPoolSize() {
        return poolSize;
    }
    
    synchronized public void start() {
        if (!closed) {
            throw new IllegalStateException("Pool already started.");
        }
        closed = false;
        for (int i = 0; i < poolSize; ++i) {
            new PooledThread().start();
        }
    }

    synchronized public void execute(Runnable job) {
        if (closed) {
            throw new PoolClosedException();
        }
        queue.enqueue(job);
    }

    public void close() {
        closed = true;
        queue.close();
    }
    
    private static class PoolClosedException extends RuntimeException {
        PoolClosedException() { 
            super ("Pool closed.");
        }
    }
    
    private class PooledThread extends Thread {
        @Override
        public void run() {
            while (true) {
                Runnable job = (Runnable) queue.dequeue();
                if (job == null) {
                    break;
                }
                try {
                    job.run();
                } catch (Throwable t) {
                    // ignore
                }
            }
        }
    }
}
