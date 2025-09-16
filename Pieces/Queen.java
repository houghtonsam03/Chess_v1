package Pieces;

import java.util.ArrayList;
import Game.Game;

public class Queen extends Piece {
    public String[] getPossibleMoves(Piece[][] tiles) {
        ArrayList<String> moves = new ArrayList<String>();
        int[] cords = Game.getCords(position);
        int[][] adjacents = new int[][]{{-1,-1},{-1,1},{1,1},{1,-1},{-1,0},{0,1},{1,0},{0,-1}};
        for (int[] adjacent: adjacents) {
            int steps = 1;
            while (true) {
                int[] new_cords = new int[]{cords[0]+adjacent[0]*steps,cords[1]+adjacent[1]*steps}; 
                try {
                    if (tiles[new_cords[0]][new_cords[1]] == null) {
                        moves.add(Game.getPos(new_cords));
                    } else {
                        if (!tiles[new_cords[0]][new_cords[1]].getTeam().equals(team)) {
                            moves.add(Game.getPos(new_cords));
                        }
                        break;
                    }
                } catch (Exception e) {
                    break;
                }
                steps++;
            }
        }
        String[] possible_moves = new String[moves.size()];
        for (int i = 0; i < possible_moves.length;i++) {
            possible_moves[i] = moves.get(i);
        }
        return possible_moves;
    }
}