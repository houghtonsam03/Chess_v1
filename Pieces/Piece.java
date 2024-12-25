package Pieces;

import java.util.ArrayList;
import Game.Game;
import javax.swing.*;

public abstract class Piece {
    protected String position;
    protected String name;
    protected String team;
    protected ImageIcon image;
    public String getPos() {
        return position;
    }
    public String getName() {
        return name;
    }
    public String getTeam() {
        return team;
    }
    public ImageIcon getPic() {
        return image;
    }
    public void move(String move) {
        position = move;
    }  
    static public Piece create(String type,String position,String team) {
        Piece piece;
        if (type == "Pawn") {
            piece =  new Pawn();
        } else if (type == "Knight"){
            piece =  new Knight();
        } else if (type == "Bishop"){
            piece =  new Bishop();
        } else if (type == "Rook"){
            piece =  new Rook();
        } else if (type == "Queen"){
            piece =  new Queen();
        } else {
            piece =  new King();
        }
        piece.position = position;
        piece.team = team;
        piece.name = type;
        piece.image = new ImageIcon(piece.getClass().getResource("../Icons/"+type+"_"+team+".png"));
        return piece;
    }
    abstract public String[] getPossibleMoves(Piece[][] tiles);   
    public String[] getLegalMoves(Piece[][] tiles,String teams_turn) {
        ArrayList<String> legalmoves_arraylist = new ArrayList<String>();
        String[] possible_moves = getPossibleMoves(tiles);
        String piece_pos = this.getPos();
        int[] piece_cords = Game.getCords(piece_pos);
        for (String move:possible_moves) {
            int[] move_cords = Game.getCords(move);
            tiles[piece_cords[0]][piece_cords[1]] = null;
            Piece remove_piece = tiles[move_cords[0]][move_cords[1]];
            tiles[move_cords[0]][move_cords[1]] = this;
            if (!Game.isCheck(tiles,team)) {
                legalmoves_arraylist.add(move);
            }
            tiles[piece_cords[0]][piece_cords[1]] = this;
            tiles[move_cords[0]][move_cords[1]] = remove_piece;
        }
        String[] legal_moves = new String[legalmoves_arraylist.size()];
        for (int i = 0; i < legal_moves.length;i++) {
            legal_moves[i] = legalmoves_arraylist.get(i);
        }
        return legal_moves;
    }
    public String toString() {
        return team+" "+name+" - "+position;
    }
    public boolean isMovePossible(String pos,Piece[][] tiles,String teams_turn) {
        boolean possible = false;
        String[] possible_moves = getLegalMoves(tiles,teams_turn);
        for (String move: possible_moves) {
            if (pos.equals(move)) {
                possible = true;
            }
        }
        return possible;
    }
}