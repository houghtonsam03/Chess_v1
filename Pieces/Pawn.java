package Pieces;
import java.util.ArrayList;

import Game.Game;

public class Pawn extends Piece{
    private boolean hasMoved = false;
    private boolean justMovedTwo = false;
    public String[] getPossibleMoves(Piece[][] tiles) {
        ArrayList<String> moves = new ArrayList<>();
        int[] cords = Game.getCords(position);
        if (team == "White") {
            if (!(tiles[cords[0]-1][cords[1]] instanceof Piece)) {
                if (!hasMoved) {
                    moves.add(position.split("")[0] + Integer.toString(Integer.parseInt(position.split("")[1])+1));
                    if (!(tiles[cords[0]-2][cords[1]] instanceof Piece)) {
                        moves.add(position.split("")[0] + Integer.toString(Integer.parseInt(position.split("")[1])+2));
                    }
                } else {
                    moves.add(position.split("")[0] + Integer.toString(Integer.parseInt(position.split("")[1])+1));
                }
            }
        } else {
            if (!(tiles[cords[0]+1][cords[1]] instanceof Piece)) {
                if (!hasMoved) {
                    moves.add(position.split("")[0] + Integer.toString(Integer.parseInt(position.split("")[1])-1));
                    if (!(tiles[cords[0]+2][cords[1]] instanceof Piece)) {
                        moves.add(position.split("")[0] + Integer.toString(Integer.parseInt(position.split("")[1])-2));
                    }
                } else {
                    moves.add(position.split("")[0] + Integer.toString(Integer.parseInt(position.split("")[1])-1));
                }
            }
        }
        for (String move:checkCaptures(tiles)) {
            moves.add(move);
        }
        for (String move:checkEnpassant(tiles)) {
            moves.add(move);
        }
        String[] possibleMoves = new String[moves.size()];
        for (int i = 0; i < moves.size(); i++) {
            possibleMoves[i] = moves.get(i);
        }
        return possibleMoves;
    }
    public void move(String move) {
        justMovedTwo = isSecondMove(move);
        super.move(move);
        hasMoved = true;
    }
    private String[] checkCaptures(Piece[][] tiles) {
        ArrayList<String> moves = new ArrayList<>();
        if (team == "White") {
            int[] cords = Game.getCords(position);
            int[] left_cords = new int[]{cords[0]-1,cords[1]-1};
            int[] right_cords = new int[]{cords[0]-1,cords[1]+1};
            try {
                if (tiles[left_cords[0]][left_cords[1]].getTeam() != team) {
                    moves.add(Game.getPos(left_cords));
                }
            } catch(Exception e) {}
            try {
                if (tiles[right_cords[0]][right_cords[1]].getTeam() != team) {
                    moves.add(Game.getPos(right_cords));
                }
            } catch(Exception e) {}
        } else {
            int[] cords = Game.getCords(position);
            int[] left_cords = new int[]{cords[0]+1,cords[1]-1};
            int[] right_cords = new int[]{cords[0]+1,cords[1]+1};
            try {
                if (tiles[left_cords[0]][left_cords[1]].getTeam() != team) {
                    moves.add(Game.getPos(left_cords));
                }
            } catch(Exception e) {}
            try {
                if (tiles[right_cords[0]][right_cords[1]].getTeam() != team) {
                    moves.add(Game.getPos(right_cords));
                }
            } catch(Exception e) {}
        }
        String[] captures = new String[moves.size()];
        for (int i = 0; i < moves.size(); i++) {
            captures[i] = moves.get(i);
        }
        return captures;
    }
    private boolean isSecondMove(String pos) {
        int[] old_cords = Game.getCords(position);
        int[] new_cords = Game.getCords(pos);
        if (Math.abs(new_cords[0]-old_cords[0]) == 2) {
            return true;
        }
        return false;
    }
    private String[] checkEnpassant(Piece[][] tiles) {
        ArrayList<String> moves = new ArrayList<>();
        int[] piece_cords = Game.getCords(position);
        int[][] neighbours = new int[][]{{piece_cords[0],piece_cords[1]-1},{piece_cords[0],piece_cords[1]+1}};
        for (int[] cords:neighbours) {
            try {
                if (((Pawn)tiles[cords[0]][cords[1]]).getJustMovedTwo()) {
                    if (team.equals("White")) {
                        moves.add(Game.getPos(new int[]{cords[0]-1,cords[1]}));
                    } else {
                        moves.add(Game.getPos(new int[]{cords[0]+1,cords[1]}));
                    }
                }
            } catch(Exception e) {}
        }
        String[] enpassant = new String[moves.size()];
        for (int i = 0; i < enpassant.length; i++) {
            enpassant[i] = moves.get(i);
        }
        return enpassant;
    }
    public boolean getJustMovedTwo() {
        return justMovedTwo;
    }
    public boolean getMoved() {
        return hasMoved;
    }
    public void update() {
        justMovedTwo = false;
    }
    public void setHasMoved(boolean b) {
        hasMoved = b;
    }
    public void setJustMoved(boolean b) {
        justMovedTwo = b;
    }
}