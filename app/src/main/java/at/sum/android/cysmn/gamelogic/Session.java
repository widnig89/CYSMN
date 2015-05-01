package at.sum.android.cysmn.gamelogic;

import com.google.android.gms.games.multiplayer.Participant;

import java.util.ArrayList;

import at.sum.android.cysmn.utils.AppLogger;
import at.sum.android.cysmn.utils.FactionEnum;

/**
 * Created by widnig89 on 01.05.15.
 */
public class Session {

    private static Session instance = new Session();

    private ArrayList<Player> players;


    public static Session getInstance() {
        if(instance == null)
            instance = new Session();
        return instance;
    }

    private Session() {
        initSession();
    }

    private void initSession()
    {
        players = new ArrayList<>();

        //TODO: remove these entries, these are only for testing
        addPlayer(new Player(null, 47.05866253, 15.44849396, 45.0f, 0));
        addPlayer(new Player(null, 47.0611183, 15.45656204, 245.0f, 1));
        addPlayer(new Player(null, 47.06930338, 15.43870926, 180.0f, 0));
        addPlayer(new Player(null, 47.08467626, 15.42858124, 320.0f, 1));


    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getPlayerByParticipant(Participant participant)
    {
        if(participant == null)
        {
            AppLogger.logError(this.getClass().getSimpleName(), "Participant is null!");
            return null;
        }

        for(Player player : players)
            if(player.getParticipant().equals(participant))
            {
                AppLogger.logDebug(this.getClass().getSimpleName(), "Player found by participant");

                return player;
            }
        return null;
    }

    public ArrayList<Player> getRunners()
    {
        ArrayList<Player> runners = new ArrayList<>();

        for(Player player : this.players)
        {
            if(player.getFaction() == FactionEnum.RUNNER)
            {
                runners.add(player);
            }
        }
        return runners;
    }

    public ArrayList<Player> getOnlinePlayers()
    {
        ArrayList<Player> onlinePlayers = new ArrayList<>();

        for(Player player : this.players)
        {
            if(player.getFaction() == FactionEnum.ONLINE_PLAYER)
            {
                onlinePlayers.add(player);
            }
        }
        return onlinePlayers;
    }

    public void addPlayer(Player player)
    {
        players.add(player);
    }

    public void removePlayer(Player player)
    {
        players.remove(player);
    }

    public void quitSession()
    {
        players.clear();
    }
}
