package sdz.Labyrinthe;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class LabyrintheActivity extends Activity{

	// Identifiant de la boite de dialogue de victoire
	public static final int VICTORY_DIALOG = 0;
	// Identifiant de la boite de dialogue de défaite
	public static final int DEFEAT_DIALOG = 1;

	// Le moteur physique du jeu
	private LabyrintheEngine mEngine = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LabyrintheView mView = new LabyrintheView(this);
		setContentView(mView);

		mEngine = new LabyrintheEngine(this);

		Boule b = new Boule();
		ColorBackGround cbg = new ColorBackGround();
		mView.setBoule(b);
		mEngine.setBoule(b);
		mView.setmColor(cbg);
		mEngine.setmColor(cbg);

		List<Bloc> mList = mEngine.buildLabyrinthe();
		mView.setBlocks(mList);
	}



	@Override
	protected void onResume() {
		super.onResume();
		mEngine.resume();
	} 

	@Override
	protected void onPause() {
		super.onStop();
		mEngine.stop();
	}

	@Override
	public Dialog onCreateDialog (int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id) {
		case VICTORY_DIALOG:
			builder.setCancelable(false)
			.setMessage("Wow, quel talent !")
			.setTitle("T tro bg")
			.setNeutralButton("Recommencer", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// L'utilisateur peut recommencer s'il le veut
					mEngine.reset();
					mEngine.resume();
				}
			});
			break;

		case DEFEAT_DIALOG:
			builder.setCancelable(false)
			.setMessage("Perdu, essaye enore !")
			.setTitle("Nul / 20 !")
			.setNeutralButton("Recommencer", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mEngine.reset();
					mEngine.resume();
				}
			});
		}
		return builder.create();
	}

	@Override
	public void onPrepareDialog (int id, Dialog box) {
		// A chaque fois qu'une boite de dialogue est lancée, on arrête le moteur physique
		mEngine.stop();
	}
}
