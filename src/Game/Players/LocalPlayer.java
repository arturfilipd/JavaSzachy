package Game.Players;


import UI.Scenes.LocalGameScreen;

public class LocalPlayer extends Player {

    LocalGameScreen game;

    public LocalPlayer (LocalGameScreen g){
        game = g;
    }

    @Override
    public int[] getMove(){
        return null;
    }

}
