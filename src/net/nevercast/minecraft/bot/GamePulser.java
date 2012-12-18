package net.nevercast.minecraft.bot;

import com.esotericsoftware.minlog.Log;

/**
 * This is supposedly 1.2.3 compliant.
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class GamePulser extends Thread {
	
	private static final String LOG_PREFIX = GamePulser.class.getSimpleName();
	
    private GamePulserReceptor receptor;
    private double delay;
    private double offset = 0;
    
    /**
     * Set this to false to kill this thread.
     */
    public boolean running = true;

    private int exceptionCounter = 0;
    public GamePulser(GamePulserReceptor receptor, long delay){
        this.receptor = receptor;
        this.delay = delay;
    }

    public void run(){
        long time = System.nanoTime()/1000000;
        long split = (long)delay / 5;
        while(!isInterrupted() && running){
            try{
            	// Time elapsed since thread started. (in ms I think)
                long elapsed = System.nanoTime()/1000000 - time;
                if(elapsed >= ( delay + offset )){
                    time = System.nanoTime()/1000000;
                    receptor.tick(elapsed);
                    long timeTaken = System.nanoTime()/1000000 - time;
                    if(timeTaken > 4){
                        offset -= 2;
                    }else if(offset < 0){
                        offset++;
                    }
                    if(offset < -10){
                        offset = -10;
                    }
                }
                if(split >= 10){
                    Thread.sleep((long)(delay + offset / 5));
                }else{
                    Thread.sleep(5);
                }
            }catch (Exception e){
                exceptionCounter++;
                Log.warn(LOG_PREFIX, "Pulser exception:", e);
                if(exceptionCounter < 3){
                    continue;
                }else{
                    Log.error(LOG_PREFIX,
                    		"Too many pulser exceptions, killing receptor: " + receptor.getClass().getSimpleName());
                    receptor.kill();
                    return;
                }
            }
        }
    }

    public interface GamePulserReceptor {
        void tick(long elapsedTime) throws Exception;
        
        /**
         * Called when this pulser dies.
         */
        void kill();
    }
}
