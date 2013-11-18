package android.client.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.client.recoveryGames.GameDTO;
import android.client.recoveryGames.RecoveryGamesThread;
import android.client.util.ConstantI;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ClientAndroidActivity extends ListActivity {
    
	private ArrayAdapter<String> arrayAdapter;
	private ProgressDialog progressDialog;
	private AlertDialog alertDialog;
	
	private List<GameDTO> gamesList;
	
	private RecoveryGamesThread recoveryGamesThread;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        recoveryGames();        
    }
    
    private void recoveryGames() {
    	
    	progressDialog = ProgressDialog.show(
				this, this.getString(R.string.please_wait_title), 
				this.getString(R.string.finding_games), true);
        
        recoveryGamesThread = new RecoveryGamesThread();
        recoveryGamesThread.setHandler(handlerMessage);
        recoveryGamesThread.setActivity(this);
        
        recoveryGamesThread.start();
    }
    
    private Handler handlerMessage = new Handler() {

		public void handleMessage(Message msg) {

			if (progressDialog.isShowing()) 
				progressDialog.dismiss();

			String title = msg.getData().getString(ConstantI.TITLE);
			String message = msg.getData().getString(ConstantI.MESSAGE);

			if (title.equals(ClientAndroidActivity.this.getString(R.string.error))) {
				
				alertDialog = new AlertDialog.Builder(ClientAndroidActivity.this).create();
				alertDialog.setButton(ClientAndroidActivity.this.getString(R.string.ok), 
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
						alertDialog.dismiss();
					}
				});
				alertDialog.setTitle(title);
				alertDialog.setMessage(message);
				alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
				alertDialog.show();
				
			} else if (title.equals(ClientAndroidActivity.this.getString(R.string.ok))) {
				
				gamesList = createGamesListByString(message);
				
				if (gamesList != null && ! gamesList.isEmpty()) {
					
					String[] names = new String[gamesList.size()];
					
					for (int i = 0; i < names.length; i++) {
						
						names[i] = gamesList.get(i).getName();
					}
					
					arrayAdapter = new ArrayAdapter<String>(ClientAndroidActivity.this, R.layout.games_row, R.id.gamesRowTextViewNome, names);
					
					setListAdapter(arrayAdapter);
				} 
			}
			
			recoveryGamesThread.interrupt();
		}
	};
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	
    	recoveryGamesThread.interrupt();
    	
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
    	
    	String name = arrayAdapter.getItem(position);
    	
    	Intent intent = new Intent(this, PlayGameActivity.class);
		intent.putExtra(ConstantI.NAME, name);
		
		this.startActivity(intent);
    }
    
    private List<GameDTO> createGamesListByString(String message) {
		
		List<GameDTO> gamesList = null;
		
		String[] firstStringArray = message.split("#");
		
		String type = firstStringArray[0];
		
		if (type.equalsIgnoreCase(ConstantI.GAMES)) {
			
			String gameMessage = firstStringArray[1];
			
			String[] gamesArray = gameMessage.split("\\|");
			
			gamesList = new ArrayList<GameDTO>();
			
			GameDTO game = null;
			
			for (String string : gamesArray) {
				
				game = new GameDTO();
				game.setName(string);
				
				gamesList.add(game);
			}
			
		} else {
			
			gamesList = null;
		}
		
		return gamesList;
	}
}