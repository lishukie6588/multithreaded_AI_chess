package chess_project;
import chess_project.pieces.*;

import java.lang.reflect.Array;
import java.util.*;

public class AI {

    public static List<List<Integer>> getAIMove(Board input_board) {

        List<List<Integer>> fetched_move = null;
        int max_recursion = 0;
        do {
            fetched_move = getAIMoveMaxRecursion(null, 0, max_recursion, input_board);
            max_recursion++;
        }
        while (fetched_move == null);

        return fetched_move;

    }


    private static List<List<Integer>> getAIMoveMaxRecursion(List<List<Integer>> root_move, int current_recursion, int max_recursion, Board input_board) {

        List<List<List<Integer>>> legal_moves = input_board.getAllLegalMoves(1);
        for (List<List<Integer>> move : legal_moves) {
            // make mock move with static ai method in ai class
            // returns a ReverseMove object
            ReverseMove reverse_move = aIMockMove(move);
            if (input_board.inCheck(0) == 1) {
                aiReverseMockMove(reverse_move);
                if (root_move == null) {
                    return move;
                } else {
                    return root_move;
                }
            }
            // else if player 0 not in check after current ai mock move
            else if (current_recursion < max_recursion) {

                List<List<Integer>> fetched_move;
                if (root_move == null) {
                    fetched_move = getAIMoveMaxRecursion(move, current_recursion + 1, max_recursion, input_board);
                } else {
                    fetched_move = getAIMoveMaxRecursion(root_move, current_recursion + 1, max_recursion, input_board);
                }

                aiReverseMockMove(reverse_move);

                if (fetched_move != null) {
                    return fetched_move; // == root_move
                }
            } else if (current_recursion == max_recursion) {
                aiReverseMockMove(reverse_move);
            }
        }

        // if no in-check resulting root move is found within provided max recursion level
        return null;

    }


    private static ReverseMove aIMockMove(List<List<Integer>> mock_move, Board input_board) {

        ReverseMove reverse_move = null;
        // normal mock move
        if (mock_move.size() == 2) {
            reverse_move = aiMockNormalMove(mock_move, input_board);
        }
        // castling mock move
        else if (mock_move.get(2).get(0) == 0 || mock_move.get(2).get(0) == 1) {
            reverse_move = aiMockCastlingMove(mock_move, input_board);
        }
        // en passant mock move
        else if (mock_move.get(2).get(0) == 3) {
            reverse_move = aiMockEnPassantMove(mock_move, input_board);
        }
        // two square mock move
        else if (mock_move.get(2).get(0) == 4) {
            reverse_move = aiMockTwoSquareMove(mock_move, input_board);
        }
        // promotion mock move
        else if (mock_move.get(2).get(0) == 2) {
            reverse_move = aiMockPromotionMove(mock_move, input_board);
        }

        return reverse_move;
    }


    private static void aiReverseMockMove(ReverseMove reverse_move, Board input_board) {

        // normal mock move
        if (reverse_move.mock_move.size() == 2) {
            aiReverseMockNormalMove(reverse_move, input_board);
        }
        // castling mock move
        else if (reverse_move.mock_move.get(2).get(0) == 0 || reverse_move.mock_move.get(2).get(0) == 1) {
            aiReverseMockCastlingMove(reverse_move, input_board);
        }
        // en passant mock move
        else if (reverse_move.mock_move.get(2).get(0) == 3) {
            aiReverseMockEnPassantMove(reverse_move, input_board);
        }
        // two square mock move
        else if (reverse_move.mock_move.get(2).get(0) == 4) {
            aiReverseMockTwoSquareMove(reverse_move, input_board);
        }
        // promotion mock move
        else if (reverse_move.mock_move.get(2).get(0) == 2) {
            aiReverseMockPromotionMove(reverse_move, input_board);
        }
    }


    private static ReverseMove aiMockNormalMove(List<List<Integer>> normal_move, Board input_board) {

        int backup_mover_moved = -99;
        Piece mover_piece = input_board.getBoard().get(normal_move.get(0).get(0)).get(normal_move.get(0).get(1));
        String character = mover_piece.getCharacter();
        switch(character)
        {
            case "ro":
                backup_mover_moved = ((Rook) mover_piece).getMoved();
                break;
            case "ki":
                backup_mover_moved = ((King) mover_piece).getMoved();
                break;
            case "pa":
                backup_mover_moved = ((Pawn) mover_piece).getMoved();
                break;
        }

        Piece dest_piece = input_board.getBoard().get(normal_move.get(1).get(0)).get(normal_move.get(1).get(1));
        ReverseMove reverse_move = new ReverseMove(normal_move, backup_mover_moved, dest_piece);
        input_board.normalMove(normal_move);
        input_board.increaseTurnNumber();
        return reverse_move;
    }


    private static void aiReverseMockNormalMove(ReverseMove reverse_move, Board input_board) {

        Piece mover_piece = input_board.move(reverse_move.mock_move.get(1), reverse_move.mock_move.get(0));
        input_board.place(reverse_move.mock_move.get(1), reverse_move.captured_piece);
        String character = mover_piece.getCharacter();
        switch(character)
        {
            case "ro":
                ((Rook) mover_piece).setMoved(reverse_move.mover_moved);
                break;
            case "ki":
                ((King) mover_piece).setMoved(reverse_move.mover_moved);
                break;
            case "pa":
                ((Pawn) mover_piece).setMoved(reverse_move.mover_moved);
                break;
        }
        input_board.decreaseTurnNumber();
    }


    private static ReverseMove aiMockCastlingMove(List<List<Integer>> castling_move, Board input_board) {

        ReverseMove reverse_move = new ReverseMove(castling_move);
        input_board.castlingMove(castling_move);
        input_board.increaseTurnNumber();
        return reverse_move;
    }


    private static void aiReverseMockCastlingMove(ReverseMove reverse_move, Board input_board) {

        Piece king = input_board.move(reverse_move.mock_move.get(1), reverse_move.mock_move.get(0));
        Piece rook = input_board.move(reverse_move.mock_move.get(4), reverse_move.mock_move.get(3));
        ((King) king).setMoved(1);
        ((Rook) rook).setMoved(1);
        input_board.decreaseTurnNumber();
    }


    private static ReverseMove aiMockEnPassantMove(List<List<Integer>> en_passant_move, Board input_board) {

        Piece captured_pawn = input_board.getBoard().get(en_passant_move.get(3).get(0)).get(en_passant_move.get(3).get(1));
        ReverseMove reverse_move = new ReverseMove(en_passant_move, captured_pawn);
        input_board.enPassantMove(en_passant_move);
        input_board.increaseTurnNumber();
        return reverse_move;
    }


    private static void aiReverseMockEnPassantMove(ReverseMove reverse_move, Board input_board) {

        Piece player_pawn = input_board.move(reverse_move.mock_move.get(1), reverse_move.mock_move.get(0));
        input_board.place(reverse_move.mock_move.get(3), reverse_move.captured_piece);
        input_board.decreaseTurnNumber();

    }




}