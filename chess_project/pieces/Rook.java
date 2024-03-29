package chess_project.pieces;
import chess_project.*;

import java.util.*;

public class Rook extends Piece{

    private int moved = 0;
    private int original = 1;

    public Rook(int player, int x, int y) {
        this.player = player;
        this.character = "ro";
        this.position.add(x);
        this.position.add(y);
    }

    /*
    public void testPrint() {
        Main.testCircular(this);
        System.out.println(this.position + this.character + this.player);
    }
     */

    public int getMoved() {
        return this.moved;
    }

    public void setMoved(int status) {
        this.moved = status;
    }


    public int getOriginal() {
        return this.original;
    }

    public void setOriginal(int value) {
        this.original = value;
    }


    @Override
    public List<List<List<Integer>>> getTheoreticMoves(Board input_board) {
        List<List<List<Integer>>> return_list = new ArrayList<>();
        basicMoves(1,0, return_list, input_board);
        basicMoves(-1,0, return_list, input_board);
        basicMoves(0,1, return_list, input_board);
        basicMoves(0,-1, return_list, input_board);
        return return_list;

    }

    private void basicMoves(int x_increment, int y_increment, List<List<List<Integer>>> return_list, Board input_board) {
        int x_axis = this.position.get(0);
        int y_axis = this.position.get(1);
        int x_total_inc = x_increment;
        int y_total_inc = y_increment;
        //System.out.println("before while");
        //Piece current_piece = input_board.getBoard().get(x_axis + x_increment).get(y_axis + y_increment);
        while (y_axis + y_total_inc <= 7 && 0 <= y_axis + y_total_inc &&
                x_axis + x_total_inc <= 7 && 0 <= x_axis + x_total_inc) {

            Piece current_piece = input_board.getBoard().get(x_axis + x_total_inc).get(y_axis + y_total_inc);
            // if dest piece == null or belongs to opponent
            if (current_piece == null || current_piece.getPlayer() != this.player) {
                //System.out.println("inside while");
                List<List<Integer>> move = new ArrayList<>(2);
                List<Integer> start = new ArrayList<>(2);
                start.add(this.position.get(0));
                start.add(this.position.get(1));
                move.add(start);
                List<Integer> dest = new ArrayList<>(2);
                dest.add(x_axis + x_total_inc);
                dest.add(y_axis + y_total_inc);
                move.add(dest);
                return_list.add(move);
                x_total_inc += x_increment;
                y_total_inc += y_increment;
                // if dest piece belongs to opponent, stop iterating
                if (current_piece != null && current_piece.getPlayer() != this.player) {
                    break;
                }
            }
            // if dest piece belongs to player
            else {
                break;
            }
        }
    }

    @Override
    public List<List<List<Integer>>> getLegalMoves(Board input_board) {
        List<List<List<Integer>>> return_list = new ArrayList<>();
        for (List<List<Integer>> theoretic_move : this.getTheoreticMoves(input_board)) {
            Piece captured = input_board.remove(theoretic_move.get(1));
            input_board.move(theoretic_move.get(0), theoretic_move.get(1));
            int backup_moved = this.getMoved();
            this.setMoved(1);
            if (input_board.inCheck(this.player) != 1) {
                return_list.add(theoretic_move);
            }
            this.setMoved(backup_moved);
            input_board.move(theoretic_move.get(1), theoretic_move.get(0));
            input_board.place(theoretic_move.get(1), captured);
        }
        return return_list;
    }


}
