package sdz.Labyrinthe;

import java.util.ArrayList;
import java.util.List;

import sdz.Labyrinthe.Bloc.Type;
import android.app.Service;
import android.graphics.Color;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import static android.content.ContentValues.TAG;

class LabyrintheEngine {
	private Boule mBoule = null;

	public void setBoule(Boule pBoule) {
		this.mBoule = pBoule;
	}

	private ColorBackGround mColor;

	public void setmColor(ColorBackGround pColor) { this.mColor = pColor;}



	private List<Bloc> mBlocks = null;

	private LabyrintheActivity mActivity = null;

	private SensorManager mManager = null;
	private Sensor mAccelerometre = null;
	private Sensor mLight = null;
	private Sensor mMagnetic = null;

	private final SensorEventListener mSensorEventListenerAcc = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent pEvent) {
			float x = pEvent.values[0];
			float y = pEvent.values[1];

			if(mBoule != null) {
				// On met à jour les coordonnées de la boule
				RectF hitBox = mBoule.putXAndY(x, y);

				// Pour tous les blocs du labyrinthe
				for(Bloc block : mBlocks) {
					// On crée un nouveau rectangle pour ne pas modifier celui du bloc
					RectF inter = new RectF(block.getRectangle());
					if(inter.intersect(hitBox)) {
						// On agit différement en fonction du type de bloc
						switch(block.getType()) {
						case TROU:
							mActivity.showDialog(LabyrintheActivity.DEFEAT_DIALOG);
							break;

						case DEPART:
							break;

						case ARRIVEE:
							mActivity.showDialog(LabyrintheActivity.VICTORY_DIALOG);
							break;
						}
						break;
					}
				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor pSensor, int pAccuracy) {

		}
	};

	private final SensorEventListener mSensorEventListenerLight = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent pEvent) {
			if (pEvent.values[0] < 80 ){
				mColor.setmColor(Color.GREEN);
			}else if (pEvent.values[0] > 80 && pEvent.values[0] < 150){
				mColor.setmColor(Color.BLUE);
			}else{
				mColor.setmColor(Color.GRAY);
			}
		}

		@Override
		public void onAccuracyChanged(Sensor pSensor, int pAccuracy) {

		}
	};

	private final SensorEventListener mSensorEventListenerMagnetic = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent pEvent) {
			Log.d(TAG, "onSensorChanged: values = " + pEvent.values[0] + pEvent.values[1] + pEvent.values[2]);
			if (pEvent.values[0] > -45 ){
				mBoule.setCouleur(Color.YELLOW);
			}else if (pEvent.values[0] < -45 && pEvent.values[0] > -55){
				mBoule.setCouleur(Color.RED);
			}else{
				mBoule.setCouleur(Color.BLACK);
			}
		}

		@Override
		public void onAccuracyChanged(Sensor pSensor, int pAccuracy) {

		}
	};

	public LabyrintheEngine(LabyrintheActivity pView) {
		mActivity = pView;
		mManager = (SensorManager) mActivity.getBaseContext().getSystemService(Service.SENSOR_SERVICE);
		mAccelerometre = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mLight = mManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		mMagnetic = mManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	}

	// Remet à zéro l'emplacement de la boule
	public void reset() {
		mBoule.reset();
	}

	// Arrête le capteur
	public void stop() {
		mManager.unregisterListener(mSensorEventListenerAcc, mAccelerometre);
		mManager.unregisterListener(mSensorEventListenerLight, mLight);
		mManager.unregisterListener(mSensorEventListenerMagnetic, mMagnetic);
	}

	// Redémarre le capteur
	public void resume() {
		mManager.registerListener(mSensorEventListenerAcc, mAccelerometre, SensorManager.SENSOR_DELAY_GAME);
		mManager.registerListener(mSensorEventListenerLight, mLight, SensorManager.SENSOR_DELAY_GAME);
		mManager.registerListener(mSensorEventListenerMagnetic, mMagnetic, SensorManager.SENSOR_DELAY_GAME);
	}

	// Construit le labyrinthe
	public List<Bloc> buildLabyrinthe() {
		mBlocks = new ArrayList<Bloc>();
		mBlocks.add(new Bloc(Type.TROU, 0, 0));
		mBlocks.add(new Bloc(Type.TROU, 0, 1));
		mBlocks.add(new Bloc(Type.TROU, 0, 2));
		mBlocks.add(new Bloc(Type.TROU, 0, 3));
		mBlocks.add(new Bloc(Type.TROU, 0, 4));
		mBlocks.add(new Bloc(Type.TROU, 0, 5));
		mBlocks.add(new Bloc(Type.TROU, 0, 6));
		mBlocks.add(new Bloc(Type.TROU, 0, 7));
		mBlocks.add(new Bloc(Type.TROU, 0, 8));
		mBlocks.add(new Bloc(Type.TROU, 0, 9));
		mBlocks.add(new Bloc(Type.TROU, 0, 10));
		mBlocks.add(new Bloc(Type.TROU, 0, 11));
		mBlocks.add(new Bloc(Type.TROU, 0, 12));
		mBlocks.add(new Bloc(Type.TROU, 0, 13));
		mBlocks.add(new Bloc(Type.TROU, 0, 14));

		mBlocks.add(new Bloc(Type.TROU, 1, 0));
		mBlocks.add(new Bloc(Type.TROU, 1, 14));

		mBlocks.add(new Bloc(Type.TROU, 2, 0));
		mBlocks.add(new Bloc(Type.TROU, 2, 14));

		mBlocks.add(new Bloc(Type.TROU, 3, 0));
		mBlocks.add(new Bloc(Type.TROU, 3, 14));

		mBlocks.add(new Bloc(Type.TROU, 4, 0));
		mBlocks.add(new Bloc(Type.TROU, 4, 1));
		mBlocks.add(new Bloc(Type.TROU, 4, 2));
		mBlocks.add(new Bloc(Type.TROU, 4, 3));
		mBlocks.add(new Bloc(Type.TROU, 4, 4));
		mBlocks.add(new Bloc(Type.TROU, 4, 5));
		mBlocks.add(new Bloc(Type.TROU, 4, 6));
		mBlocks.add(new Bloc(Type.TROU, 4, 7));
		mBlocks.add(new Bloc(Type.TROU, 4, 8));
		mBlocks.add(new Bloc(Type.TROU, 4, 9));
		mBlocks.add(new Bloc(Type.TROU, 4, 10));
		mBlocks.add(new Bloc(Type.TROU, 4, 13));
		mBlocks.add(new Bloc(Type.TROU, 4, 14));

		mBlocks.add(new Bloc(Type.TROU, 5, 0));
		mBlocks.add(new Bloc(Type.TROU, 5, 14));

		mBlocks.add(new Bloc(Type.TROU, 6, 0));
		mBlocks.add(new Bloc(Type.TROU, 6, 14));

		mBlocks.add(new Bloc(Type.TROU, 7, 0));
		mBlocks.add(new Bloc(Type.TROU, 7, 1));
		mBlocks.add(new Bloc(Type.TROU, 7, 2));
		mBlocks.add(new Bloc(Type.TROU, 7, 5));
		mBlocks.add(new Bloc(Type.TROU, 7, 6));
		mBlocks.add(new Bloc(Type.TROU, 7, 9));
		mBlocks.add(new Bloc(Type.TROU, 7, 10));
		mBlocks.add(new Bloc(Type.TROU, 7, 11));
		mBlocks.add(new Bloc(Type.TROU, 7, 12));
		mBlocks.add(new Bloc(Type.TROU, 7, 13));
		mBlocks.add(new Bloc(Type.TROU, 7, 14));

		mBlocks.add(new Bloc(Type.TROU, 8, 0));
		mBlocks.add(new Bloc(Type.TROU, 8, 5));
		mBlocks.add(new Bloc(Type.TROU, 8, 9));
		mBlocks.add(new Bloc(Type.TROU, 8, 14));

		mBlocks.add(new Bloc(Type.TROU, 9, 0));
		mBlocks.add(new Bloc(Type.TROU, 9, 5));
		mBlocks.add(new Bloc(Type.TROU, 9, 9));
		mBlocks.add(new Bloc(Type.TROU, 9, 14));

		mBlocks.add(new Bloc(Type.TROU, 10, 0));
		mBlocks.add(new Bloc(Type.TROU, 10, 5));
		mBlocks.add(new Bloc(Type.TROU, 10, 9));
		mBlocks.add(new Bloc(Type.TROU, 10, 14));

		mBlocks.add(new Bloc(Type.TROU, 11, 0));
		mBlocks.add(new Bloc(Type.TROU, 11, 5));
		mBlocks.add(new Bloc(Type.TROU, 11, 9));
		mBlocks.add(new Bloc(Type.TROU, 11, 14));

		mBlocks.add(new Bloc(Type.TROU, 12, 0));
		mBlocks.add(new Bloc(Type.TROU, 12, 1));
		mBlocks.add(new Bloc(Type.TROU, 12, 2));
		mBlocks.add(new Bloc(Type.TROU, 12, 3));
		mBlocks.add(new Bloc(Type.TROU, 12, 4));
		mBlocks.add(new Bloc(Type.TROU, 12, 5));
		mBlocks.add(new Bloc(Type.TROU, 12, 9));
		mBlocks.add(new Bloc(Type.TROU, 12, 8));
		mBlocks.add(new Bloc(Type.TROU, 12, 14));

		mBlocks.add(new Bloc(Type.TROU, 13, 0));
		mBlocks.add(new Bloc(Type.TROU, 13, 8));
		mBlocks.add(new Bloc(Type.TROU, 13, 14));

		mBlocks.add(new Bloc(Type.TROU, 14, 0));
		mBlocks.add(new Bloc(Type.TROU, 14, 8));
		mBlocks.add(new Bloc(Type.TROU, 14, 14));

		mBlocks.add(new Bloc(Type.TROU, 15, 0));
		mBlocks.add(new Bloc(Type.TROU, 15, 8));
		mBlocks.add(new Bloc(Type.TROU, 15, 14));

		mBlocks.add(new Bloc(Type.TROU, 16, 0));
		mBlocks.add(new Bloc(Type.TROU, 16, 4));
		mBlocks.add(new Bloc(Type.TROU, 16, 5));
		mBlocks.add(new Bloc(Type.TROU, 16, 6));
		mBlocks.add(new Bloc(Type.TROU, 16, 7));
		mBlocks.add(new Bloc(Type.TROU, 16, 8));
		mBlocks.add(new Bloc(Type.TROU, 16, 9));
		mBlocks.add(new Bloc(Type.TROU, 16, 14));

		mBlocks.add(new Bloc(Type.TROU, 17, 0));
		mBlocks.add(new Bloc(Type.TROU, 17, 14));

		mBlocks.add(new Bloc(Type.TROU, 18, 0));
		mBlocks.add(new Bloc(Type.TROU, 18, 14));

		mBlocks.add(new Bloc(Type.TROU, 19, 0));
		mBlocks.add(new Bloc(Type.TROU, 19, 1));
		mBlocks.add(new Bloc(Type.TROU, 19, 2));
		mBlocks.add(new Bloc(Type.TROU, 19, 3));
		mBlocks.add(new Bloc(Type.TROU, 19, 4));
		mBlocks.add(new Bloc(Type.TROU, 19, 5));
		mBlocks.add(new Bloc(Type.TROU, 19, 6));
		mBlocks.add(new Bloc(Type.TROU, 19, 7));
		mBlocks.add(new Bloc(Type.TROU, 19, 8));
		mBlocks.add(new Bloc(Type.TROU, 19, 9));
		mBlocks.add(new Bloc(Type.TROU, 19, 10));
		mBlocks.add(new Bloc(Type.TROU, 19, 11));
		mBlocks.add(new Bloc(Type.TROU, 19, 12));
		mBlocks.add(new Bloc(Type.TROU, 19, 13));
		mBlocks.add(new Bloc(Type.TROU, 19, 14));

		Bloc b = new Bloc(Type.DEPART, 2, 2);
		mBoule.setInitialRectangle(new RectF(b.getRectangle()));
		mBlocks.add(b);

		mBlocks.add(new Bloc(Type.ARRIVEE, 8, 11));

		return mBlocks;
	}

}
