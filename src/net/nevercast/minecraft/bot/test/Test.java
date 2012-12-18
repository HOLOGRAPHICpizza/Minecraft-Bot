package net.nevercast.minecraft.bot.test;

import com.esotericsoftware.minlog.Log;
import net.nevercast.minecraft.bot.GamePulser;
import net.nevercast.minecraft.bot.GamePulser.GamePulserReceptor;

public class Test implements Runnable, GamePulserReceptor {
	public static void main(String[] args) {
		new Thread(new Test()).start();
	}

	@Override
	public void tick(long elapsedTime) throws Exception {
		Log.debug("Tick: " + elapsedTime + "ms");
	}

	@Override
	public void run() {
		new GamePulser(this, 50).start();
		while(true) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {}
		}
	}

	@Override
	public void kill() {
		System.exit(1);
	}
}
