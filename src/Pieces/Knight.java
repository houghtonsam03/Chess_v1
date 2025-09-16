package Pieces;

import java.util.ArrayList;
import Game.Game;
public class Knight extends Piece {
    public String[] getPossibleMoves(Piece[][] tiles) {
            int[] cords = Game.getCords(position);
            ArrayList<String> moves = new ArrayList<>();
            for (int[] change_cords: new int[][]{{-2,-1},{-2,1},{-1,2},{1,2},{2,1},{2,-1},{-1,-2},{1,-2}}) {
                int row = cords[0] + change_cords[0];
                int col = cords[1] + change_cords[1];
                try {
                    if (!(tiles[row][col] instanceof Piece && tiles[row][col].getTeam().equals(team)) ){
                        moves.add(Game.getPos(new int[]{row, col}));
                    }
                } catch (Exception e) {}
            }
            String[] possible_moves = new String[moves.size()];
            for (int i = 0; i < possible_moves.length; i++){
                possible_moves[i] = moves.get(i);
            }
            return possible_moves;
    }
}