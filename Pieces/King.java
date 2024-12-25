package Pieces;
import java.util.ArrayList;
import Game.Game;

public class King extends Piece{
    private boolean hasMoved = false;
    private boolean isChecked = false;
    public String[] getPossibleMoves(Piece[][] tiles) {
        ArrayList<String> moves = new ArrayList<String>();
        int[] cords = Game.getCords(position);
        int[][] adjacents = new int[][]{{-1,-1},{-1,0},{-1,1},{0,1},{1,1},{1,0},{1,-1},{0,-1}};
        for (int[] adjacent:adjacents) {
            int row = cords[0]+adjacent[0];
            int col = cords[1]+adjacent[1];
            try {
                if (!(tiles[row][col] instanceof Piece && tiles[row][col].getTeam().equals(team)) ){
                    moves.add(Game.getPos(new int[]{row, col}));
                }
            } catch (Exception e) {}
        }
        for (String move:getCastling(tiles)) {
            moves.add(move);
        }
        String[] possible_moves = new String[moves.size()];
        for (int i = 0; i < possible_moves.length;i++) {
            possible_moves[i] = moves.get(i);
        }
        return possible_moves;
    }
    private String[] getCastling(Piece[][] tiles) {
        ArrayList<String> moves = new ArrayList<>();
        if (!hasMoved && !isChecked) {
            int[] cords = Game.getCords(position);
            try {
                if (!(((Rook) tiles[cords[0]][0]).getMoved()) && tiles[cords[0]][1] == null && tiles[cords[0]][2] == null && tiles[cords[0]][3] == null) {
                    moves.add(Game.getPos(new int[]{cords[0],2}));
                }
                if (!(((Rook) tiles[cords[0]][7]).getMoved()) && tiles[cords[0]][6] == null && tiles[cords[0]][5] == null) {
                    moves.add(Game.getPos(new int[]{cords[0],6}));
                }
            } catch (Exception e) {}
        }
        if (moves.size() == 0) {
            return new String[0];
        }
        String[] castling = new String[moves.size()];
        for (int i = 0; i < moves.size(); i++) {
            castling[i] = moves.get(i);
        }
        return castling;
    }
    public void move(String move) {
        super.move(move);
        hasMoved = true;
    }
    public boolean getMoved() {
        return hasMoved;
    }
    public void setChecked(boolean checked) {
        isChecked = checked;
    }
    public void setHasMoved(boolean b) {
        hasMoved = b;
    }
    public boolean getChecked() {
        return isChecked;
    }
}