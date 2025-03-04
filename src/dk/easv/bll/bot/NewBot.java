package dk.easv.bll.bot;

import dk.easv.bll.bot.IBot;
import dk.easv.bll.field.IField;
import dk.easv.bll.game.IGameState;
import dk.easv.bll.move.IMove;
import dk.easv.bll.move.Move;

import java.util.ArrayList;
import java.util.List;


public class NewBot implements IBot {

    private static final String BOTNAME = "NewBot";
    // Moves {row, col} in order of preferences. {0, 0} at top-left corner
    protected int[][] preferredMoves = {
            {1, 1}, //Center
            {0, 0}, {2, 2}, {0, 2}, {2, 0},  //Corners ordered across
            {0, 1}, {2, 1}, {1, 0}, {1, 2}}; //Outer Middles ordered across

    /**
     * Makes a turn. Edit this method to make your bot smarter.
     * A bot that uses a local prioritised list algorithm, in order to win any local board,
     * and if all boards are available for play, it'll run a on the macroboard,
     * to select which board to play in.
     *
     * @return The selected move we want to make.
     */
    @Override
    public IMove doMove(IGameState state) {
        List<IMove> winMoves = getWinningMoves(state);
        if(!winMoves.isEmpty())
            return winMoves.get(0);

        //Find macroboard to play in
        for (int[] move : preferredMoves)
        {
            if(state.getField().getMacroboard()[move[0]][move[1]].equals(IField.AVAILABLE_FIELD))
            {
                //find move to play
                for (int[] selectedMove : preferredMoves)
                {
                    int x = move[0]*3 + selectedMove[0];
                    int y = move[1]*3 + selectedMove[1];
                    if(state.getField().getBoard()[x][y].equals(IField.EMPTY_FIELD))
                    {
                        return new Move(x,y);
                    }
                }
            }
        }

        //NOTE: Something failed, just take the first available move I guess!
        return state.getField().getAvailableMoves().get(0);
    }
    //Compile a list of all available winning moves
    private List<IMove> getWinningMoves(IGameState state) {
        String player = "1";
        if(state.getMoveNumber()%2==0)
            player="0";

        List<IMove> avail = state.getField().getAvailableMoves();
        String[][] board = state.getField().getBoard();

        List<IMove> winningMoves = new ArrayList<>();
        for(IMove move : avail){
            boolean isRowWin = true;
            // Row checking
            int startX = move.getX()-(move.getX()%3);
            int endX = startX + 2;
            for(int x = startX; x <= endX; x++){
                if(x!=move.getX())
                    if(!board[x][move.getY()].equals(player))
                        isRowWin = false;
            }
            if(isRowWin) {
                winningMoves.add(move);
                break;
            }

            boolean isColumnWin = true;
            // Column checking
            int startY = move.getY()-(move.getY()%3);
            int endY = startX + 2;
            for(int y = startY; y <= endY; y++){
                if(y!=move.getY())
                    if(!board[move.getX()][y].equals(player))
                        isColumnWin = false;
            }
            if(isColumnWin) {
                winningMoves.add(move);
                break;
            }

            boolean isDiagonalWin = true;

            // Diagonal checking left-top to right-bottom
            if(!(move.getX()==startX && move.getY()==startY))
                if(!board[startX][startY].equals(player))
                    isDiagonalWin = false;
            if(!(move.getX()==startX+1 && move.getY()==startY+1))
                if(!board[startX+1][startY+1].equals(player))
                    isDiagonalWin = false;
            if(!(move.getX()==startX+2 && move.getY()==startY+2))
                if(!board[startX+2][startY+2].equals(player))
                    isDiagonalWin = false;

            if(isDiagonalWin) {
                winningMoves.add(move);
                break;
            }

            boolean isOppositeDiagonalWin = true;
            // Diagonal checking left-bottom to right-top
            if(!(move.getX()==startX && move.getY()==startY+2))
                if(!board[startX][startY+2].equals(player))
                    isOppositeDiagonalWin = false;
            if(!(move.getX()==startX+1 && move.getY()==startY+1))
                if(!board[startX+1][startY+1].equals(player))
                    isOppositeDiagonalWin = false;
            if(!(move.getX()==startX+2 && move.getY()==startY))
                if(!board[startX+2][startY].equals(player))
                    isOppositeDiagonalWin = false;
            if(isOppositeDiagonalWin) {
                winningMoves.add(move);
                break;
            }
        }
        //stage.getField().getBoard()
        return winningMoves;
    }

    @Override
    public String getBotName() {
        return BOTNAME;
    }
}
