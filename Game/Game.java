package Chess_v1.Game;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import Chess_v1.Pieces.*;

public class Game implements ActionListener {
    private String teams_turn;
    private Piece picked;
    private Window window;
    private Piece[][] tiles;
    private String error;
    private String promotion;
    private Piece[][] old_tiles;
    private String suggested_pos;
    private String old_pos;
    private boolean gameover;
    private Boolean[] fifty_moves;
    Game() {
        window = new Window();
        tiles = new Piece[8][8];
        addListeners();
        resetPos();
        writeBoard();
    }
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("reset")) {
            resetPos();
            writeBoard();
        } else if (!gameover){
            if (suggested_pos != null) {
                readSuggestionAnswer(command);
            } else if (promotion != null) {
                promotion(command);
            } else {
                actionTile(command);
            }
            writeBoard();
        }
    }
    private void writeBoard() {
        ImageIcon[][] images = new ImageIcon[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = tiles[row][col];
                if (piece == null) {
                    images[row][col] = null;
                } else {
                    images[row][col] = piece.getPic();
                }
            }
        }
        window.writeBoard(images);
        window.resetBackground();
        window.hidePromotion();
        window.hideSuggestion();
        if (picked != null) {
            window.setPossibleMoves(picked.getLegalMoves(tiles,teams_turn));
            window.setPickedPos(picked.getPos());
        } else if (suggested_pos != null) {
            window.showSuggestion();
            window.setPossibleMoves(new String[]{suggested_pos});
            window.setPickedPos(old_pos);
        } else if (promotion != null) {
            window.showPromotion(changeTeam(teams_turn));
        }
        window.setTurn(teams_turn);
        window.setError(error);
        SwingUtilities.updateComponentTreeUI(window);
    }
    private void addListeners() {
        for (JButton button: window.getButtons()) {
            button.addActionListener(this);
        }
    }
    private void updatePawnMoves() {
        for (Piece[] row: tiles) {
            for (Piece p:row) {
                try {
                    ((Pawn)p).update();
                }catch(Exception e) {}
            }
        }
    }
    private void suggestion() {
        old_tiles = new Piece[8][8];
        for (int i = 0; i < 8;i++) {
            for (int j = 0; j < 8;j++) {
                Piece piece = tiles[i][j];
                if (piece == null) {
                    old_tiles[i][j] = null;
                } else {
                    Piece new_piece = Piece.create(piece.getName(),piece.getPos(),piece.getTeam());
                    if (new_piece instanceof Pawn) {
                        ((Pawn)new_piece).setJustMoved(((Pawn)piece).getJustMovedTwo());
                        ((Pawn)new_piece).setHasMoved(((Pawn)piece).getMoved());
                    }
                    else if (new_piece instanceof King) {
                        ((King)new_piece).setChecked(((King)piece).getChecked());
                        ((King)new_piece).setHasMoved(((King)piece).getMoved());
                    } else if (new_piece instanceof Rook) {
                        ((Rook)new_piece).setHasMoved(((Rook)piece).getMoved());
                    }
                    old_tiles[i][j] = new_piece;
                }
            }
        }
        old_pos = picked.getPos();
        suggested_pos = picked.getLegalMoves(tiles,teams_turn)[0];
        movePiece(picked.getLegalMoves(tiles, teams_turn)[0]);
    }
    private void resetPos() {
        clearBoard();
        String[][] white_pieces = {{"Rook","A1"},{"Knight","B1"},{"Bishop","C1"},{"Queen","D1"},{"King","E1"},{"Bishop","F1"},{"Knight","G1"},{"Rook","H1"},{"Pawn","A2"},{"Pawn","B2"},{"Pawn","C2"},{"Pawn","D2"},{"Pawn","E2"},{"Pawn","F2"},{"Pawn","G2"},{"Pawn","H2"}};
        String[][] black_pieces = {{"Rook","A8"},{"Knight","B8"},{"Bishop","C8"},{"Queen","D8"},{"King","E8"},{"Bishop","F8"},{"Knight","G8"},{"Rook","H8"},{"Pawn","A7"},{"Pawn","B7"},{"Pawn","C7"},{"Pawn","D7"},{"Pawn","E7"},{"Pawn","F7"},{"Pawn","G7"},{"Pawn","H7"}};
        for (int i=0;i<16;i++) {
            String[] white = white_pieces[i];
            String[] black = black_pieces[i];
            addPiece(white[0],white[1],"White");
            addPiece(black[0],black[1],"Black");
        }
    }
    private void clearBoard() {
        error = "";
        teams_turn = "White";
        gameover = false;
        picked = null;
        promotion = null;
        old_tiles = null;
        suggested_pos = null;
        old_pos = null;
        fifty_moves = new Boolean[50];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                tiles[row][col] = null;
            }
        }
    }
    private void checkGame(Piece picked, Piece old_piece,int[] picked_cords,int[] old_cords) {
        specialMoves(picked, old_piece, picked_cords, old_cords);
        updateFifty(picked, old_piece, picked_cords, old_cords);
        if (isCheckMate(tiles,teams_turn)) {
            error = "Checkmate! "+changeTeam(teams_turn)+" wins.";
            gameover = true;
            checkKing(true);
        }
        else if (isCheck(tiles,teams_turn)) {
            error = "Check";
            checkKing(true);
        } else if (isPositionDraw(tiles,teams_turn,fifty_moves)) {
            gameover = true;
            if (isStalemate(tiles, teams_turn)) {
                error = "Draw! Position is stalemate";
            } else if (isFiftyMoveDraw(fifty_moves)) {
                error = "Draw! Fifty move rule";
            } else {
                error = "Draw! Insufficient material";
            }
        } else {
            if (!error.equals("Choose a promotion")) { 
                error = "";
            }
        }
    }
    private void addPiece(String type, String pos,String team) {
        Piece new_piece = Piece.create(type,pos,team);
        int[] cords = getCords(pos);
        tiles[cords[0]][cords[1]] = new_piece;
    }
    private void actionTile(String pos) {
        if (picked == null) {
            pickPiece(pos);
        } else {
            movePiece(pos);
        }
    }
    private void pickPiece(String pos) {
        int[] cords = getCords(pos);
        Piece piece = tiles[cords[0]][cords[1]];
        if (piece == null) {
            return;
        }
        if (teams_turn.equals(piece.getTeam())) {
            picked = piece;
            if (picked.getLegalMoves(tiles, teams_turn).length == 1) {
                suggestion();
            }
        } else {
            error = "It is "+teams_turn+"'s turn";
            return;
        }
    }
    private void movePiece(String pos) {
        int[] cords = getCords(pos);
        Piece piece = tiles[cords[0]][cords[1]];
        if (piece != null) {
            if (piece.getTeam().equals(teams_turn)) {
                pickPiece(pos);
                return;
            } else {
                move(pos);
            }
        }
        if (piece == null) {
            move(pos);
        }
        picked = null;
    }
    private void move(String pos) {
        int[] cords = getCords(pos);
        if (picked.isMovePossible(pos, tiles, teams_turn)) {
            updatePawnMoves();
            checkKing(false);
            Piece old_piece = tiles[cords[0]][cords[1]];
            tiles[cords[0]][cords[1]] = picked;
            int[] picked_cords = getCords(picked.getPos());
            tiles[picked_cords[0]][picked_cords[1]] = null;
            picked.move(pos);
            teams_turn = changeTeam(teams_turn);
            checkGame(picked, old_piece, picked_cords, cords);
        } else {
            error = "Not a legal move";
            picked = null;
            return;
        }
    }
    private void checkKing(boolean bol) {
        for (Piece[] row:tiles) {
            for (Piece p:row) {
                try {
                    ((King)p).setChecked(bol);
                } catch(Exception e) {}
            }
        }
    } 
    private void updateFifty(Piece piece,Piece old_piece,int[] last_pos,int[] new_pos) {
        ArrayList<Boolean> fifty_list = new ArrayList<Boolean>();
        for (int i = 0; i < 50; i++) {
            if (fifty_moves[i] == null) {
                break;
            } else {
                fifty_list.add(fifty_moves[i]);
            }
        }
        if (piece instanceof Pawn && old_piece != null) {
            fifty_list.add(true);
        } else {
            fifty_list.add(false);
        }
        if (fifty_list.size() > 50) {
            fifty_list.remove(0);
        }
        for (int i = 0; i <fifty_list.size();i++) {
            fifty_moves[i] = fifty_list.get(i);
        }
    }
    private void specialMoves(Piece piece,Piece old_piece,int[] last_pos,int[] new_pos) {
        if (isEnpassant(piece,old_piece,last_pos,new_pos)) {
            if (piece.getTeam() == "White") {
                tiles[new_pos[0]+1][new_pos[1]] = null;
            } else {
                tiles[new_pos[0]-1][new_pos[1]] = null;
            } 
        } else if (isCastle(piece,last_pos,new_pos)) {
            int[] move_from;
            int[] move_to;
            if (new_pos[1] == 2) {
                move_from = new int[]{new_pos[0],0};
                move_to = new int[]{new_pos[0],3};
            } else {
                move_from = new int[]{new_pos[0],7};
                move_to = new int[]{new_pos[0],5};
            }
            tiles[move_to[0]][move_to[1]] = tiles[move_from[0]][move_from[1]];
            tiles[move_from[0]][move_from[1]] = null;
            tiles[move_to[0]][move_to[1]].move(getPos(move_to));
        } else if (isPromotion(piece,new_pos)) {
            promotion = getPos(new_pos);
            error = "Choose a promotion";
        } else {
            return;
        }
    }
    private void promotion(String type) {
        try {
            addPiece(type,promotion,changeTeam(teams_turn));
            promotion = null;
            error = "";
        } catch (Exception ex) {}
    }
    private void readSuggestionAnswer(String answer) {
        if (answer.equals("Decline")) {
            tiles = old_tiles.clone();
            old_tiles = null;
            suggested_pos = null;
            old_pos = null;
            promotion = null;
            teams_turn = changeTeam(teams_turn);
        } else if (answer.equals("Accept")) {
            old_tiles = null;
            suggested_pos = null;
            old_pos = null;
        } else {
            error = "Please respond to the suggestion";
        }
    }
    private boolean isEnpassant(Piece piece,Piece old_piece, int[] last_pos,int[] new_pos) {
        try {
            if (piece instanceof Pawn && new_pos[1] != last_pos[1] && old_piece == null) {
                return true;
            }
        } catch (Exception e) {}
        return false;
    }
    private boolean isCastle(Piece piece, int[] last_pos,int[] new_pos) {
        try {
            if (piece instanceof King && (Math.abs(new_pos[1]-last_pos[1]) > 1)) {
                return true;
            }
        } catch (Exception e) {}
        return false;
    }
    private boolean isPromotion(Piece piece,int[] pos) {
        if (piece instanceof Pawn && ((piece.getTeam().equals("White") && pos[0] == 0) || (piece.getTeam().equals("Black") && pos[0] == 7))) {
            return true;
        }
        return false;
    }
    static public boolean isPositionDraw(Piece[][] position, String team, Boolean[] fifty_moves) {
        return (isStalemate(position, team) || isFiftyMoveDraw(fifty_moves) || isInsufficientMaterial(position));
    }
    static public boolean isStalemate(Piece[][] position, String team) {
        for (Piece[] row : position) {
            for (Piece piece : row) {
                try {
                    if (piece.getTeam().equals(team) && piece.getLegalMoves(position, team).length != 0) {
                        return false;
                    }
                } catch (Exception e) {}
            }
        }
        return true;
    }
    static public boolean isFiftyMoveDraw(Boolean[] fifty_moves) {
        boolean isDraw = true;
        for (Boolean bol : fifty_moves) {
            if (bol == null) {
                return false;
            } else {
                if (bol) {
                    isDraw = false;
                }
            }
        }
        return isDraw;
    }
    static public boolean isInsufficientMaterial(Piece[][] position) {
        ArrayList<String> white = new ArrayList<String>();
        ArrayList<String> black = new ArrayList<String>();
        for (Piece[] row : position) {
            for (Piece piece : row) {
                try {
                    if (piece.getTeam().equals("White")) {
                        white.add(piece.getName());
                    } else {
                        black.add(piece.getName());
                    }
                } catch (Exception e) {}
            }
        }
        if (white.size() == 1) {
            if (black.size() == 1) {
                return true;
            } else if (black.size() == 2 && (black.contains("King") && (black.contains("Bishop") || black.contains("Knight")))) {
                return true;
            }
        }
        if (black.size() == 1) {
            if (white.size() == 2 && (white.contains("King") && (white.contains("Bishop") || white.contains("Knight")))) {
                return true;
            }
        }
        if ((white.size() == 2 && (white.contains("King") && (white.contains("Bishop") || white.contains("Knight")))) && (black.size() == 2 && (black.contains("King") && (black.contains("Bishop") || black.contains("Knight"))))) {
            return true;
        }
        return false;
    }
    static public boolean isCheck(Piece[][] position, String team) {
        for (Piece[] row:position) {
            for (Piece piece:row) {
                try {
                    String[] moves = piece.getPossibleMoves(position);
                    for (String move:moves) {
                        int[] move_cords = getCords(move);
                        if (position[move_cords[0]][move_cords[1]] instanceof King && position[move_cords[0]][move_cords[1]].getTeam().equals(team)) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        return false;
    }
    static public boolean isCheckMate(Piece[][] position, String team) {
        return (isCheck(position, team) && isStalemate(position, team));
    }
    static public int[] getCords(String pos) {
        String[] coordinates = pos.split("");
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H"};
        int col = 0;
        for (int i = 0; i < letters.length; i++) {
            if (letters[i].equals(coordinates[0])) {
                col = i;
            }
        }
        return new int[]{8-Integer.parseInt(coordinates[1]),col};
    }
    static public String getPos(int[] cords) {
        String row = Integer.toString(8-cords[0]);
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H"};
        String col = letters[cords[1]];
        return col+row;
    }
    static public String changeTeam(String t) {
        String team;
        if (t.equals("White")) {
            team = "Black";
        } else {
            team = "White";
        }
        return team;
    }
}