package com.adriangheo.mental3dtictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import com.sdsmdg.tastytoast.TastyToast;
import java.util.ArrayList;
import java.util.Random;

public class TTT3D extends AppCompatActivity implements View.OnClickListener {
    int randomRowNr, randomColumnNr, randomBoardNr;

    View viewWithMatricesButtons;

    private ArrayList<ExampleItem> mExampleList;
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    LinearLayout linearLayoutWithMatrices;
    ImageView imageView;

    private TextSwitcher textSwitcherRow, textSwitcherColumn, textSwitcherBoard;

    private int stringRowIndex = 0, stringColumnIndex = 0, stringBoardIndex = 0;

    char player1symbol, player2symbol;
    protected String[] arrayChosenRow = { "3", "2", "1"};
    protected String[] arrayChosenColumn = { "A", "B", "C"};
    protected String[] arrayChosenBoard = { "Top", "Core", "Bottom"};

    Button bnRowBack, bnRowForward, bnColumnBack, bnColumnForward,
            bnBoardback, bnBoardForward, newTextBasedMove;

    TextView tvChosenRow, tvChosenColumn, tvChoserBoard;

    public static final String SHARED_PREFS_CONSTANT = "sharedPrefs";
    public static final String FIRST_PLAYER_SELECTION_CONSTANT = "textConstantForPlayerSelected";
    public static final String DIFFICULTY_SELECTION_CONSTANT = "textConstantForDifficultySelected";
    public static final String TYPE_OF_OPPONENT_SELECTION = "textConstantForTypeOfOpponentSelected";
    public static final String BUTTON_MATRICES_VISIBILITY = "textConstantForMatricesVisibility";
    public static final String WALL_SELECTION_CONSTANT = "textConstantForEnabledWallObstacles";

    Button buttonReset;
    String sharedPrefsForChosenFirstPlayer, sharedPrefsForDifficultySelected,
            sharedPrefsForTypeOfOpponent, sharedPrefsForMatricesButtonsVisibility, sharedPrefsForEnabledWallObstacles;

    protected boolean humanFirst, playingVsCpu, playingVsHuman;

    protected int difficulty;
    protected int totalLooksAhead;
    private int lookAheadCounter;

    int[] finalWin = new int[3];

    public boolean win = false;

    char symbolCharX = 'X';
    char symbolCharO = 'O';
    char wallPiece = 'W';

    private Button[][][] buttons = new Button[3][3][3];
    private char[][][] config = new char[3][3][3];

    private boolean player1Turn = true;

    private int roundCount, player1Points = 0, player2Points = 0;
    private TextView textViewPlayer1, textViewPlayer2;

    int flagWallObstacleHasBeenPlaced = 0;
    int flagWallObstaclePlacementEnabled = 0;
    int boardIsEnabled = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_t_t3_d);

        viewWithMatricesButtons = findViewById(R.id.constraintLayoutWithMatrices);

        linearLayoutWithMatrices = findViewById(R.id.linear_layout_with_matrices);
        imageView = new ImageView(this);

        bnRowBack = findViewById(R.id.button_back_1);
        bnRowForward = findViewById(R.id.button_forward_1);

        bnColumnBack = findViewById(R.id.button_back_2);
        bnColumnForward = findViewById(R.id.button_forward_2);

        bnBoardback = findViewById(R.id.button_back_3);
        bnBoardForward = findViewById(R.id.button_forward_3);

        newTextBasedMove = findViewById(R.id.button_place_move);

        textSwitcherRow = findViewById(R.id.textSwicher_nr_1);
        textSwitcherColumn = findViewById(R.id.textSwicher_nr_2);
        textSwitcherBoard = findViewById(R.id.textSwicher_nr_3);


        bnRowForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stringRowIndex == arrayChosenRow.length-1){
                    stringRowIndex = 0;
                    textSwitcherRow.setText(arrayChosenRow[stringRowIndex]);
                }else {
                    textSwitcherRow.setText(arrayChosenRow[++stringRowIndex]);
                }
            }
        });

        bnColumnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stringColumnIndex == arrayChosenColumn.length-1){
                    stringColumnIndex = 0;
                    textSwitcherColumn.setText(arrayChosenColumn[stringColumnIndex]);
                }else {
                    textSwitcherColumn.setText(arrayChosenColumn[++stringColumnIndex]);
                }
            }
        });

        bnBoardForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringBoardIndex == arrayChosenBoard.length-1){
                    stringBoardIndex = 0;
                    textSwitcherBoard.setText(arrayChosenBoard[stringBoardIndex]);
                }else{
                    textSwitcherBoard.setText(arrayChosenBoard[++stringBoardIndex]);
                }
            }
        });

        bnRowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stringRowIndex == 0){
                    textSwitcherRow.setText(arrayChosenRow[arrayChosenRow.length-1]);
                    stringRowIndex = arrayChosenRow.length-1;
                }else {
                    textSwitcherRow.setText(arrayChosenRow[--stringRowIndex]);
                }
            }
        });

        bnColumnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stringColumnIndex == 0){
                    textSwitcherColumn.setText(arrayChosenColumn[arrayChosenColumn.length-1]);
                    stringColumnIndex = arrayChosenColumn.length -1;
                }else {
                    textSwitcherColumn.setText(arrayChosenColumn[--stringColumnIndex]);
                }
            }
        });

        bnBoardback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringBoardIndex == 0){
                    textSwitcherBoard.setText(arrayChosenBoard[arrayChosenBoard.length-1]);
                    stringBoardIndex = arrayChosenBoard.length - 1;
                }else{
                    textSwitcherBoard.setText(arrayChosenBoard[--stringBoardIndex]);
                }
            }
        });

        textSwitcherRow.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                tvChosenRow = new TextView(TTT3D.this);
                tvChosenRow.setTextColor(Color.BLACK);
                tvChosenRow.setTextSize(16);
                tvChosenRow.setGravity((Gravity.CENTER_HORIZONTAL));
                return tvChosenRow;
            }
        });

        textSwitcherColumn.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                tvChosenColumn = new TextView(TTT3D.this);
                tvChosenColumn.setTextColor(Color.BLACK);
                tvChosenColumn.setTextSize(16);
                tvChosenColumn.setGravity((Gravity.CENTER_HORIZONTAL));
                return tvChosenColumn;
            }
        });

        textSwitcherBoard.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                tvChoserBoard = new TextView(TTT3D.this);
                tvChoserBoard.setTextColor(Color.BLACK);
                tvChoserBoard.setTextSize(16);
                tvChoserBoard.setGravity((Gravity.CENTER_HORIZONTAL));
                return tvChoserBoard;
            }
        });

        textSwitcherRow.setText(arrayChosenRow[stringRowIndex]);
        textSwitcherColumn.setText(arrayChosenColumn[stringColumnIndex]);
        textSwitcherBoard.setText(arrayChosenBoard[stringBoardIndex]);

        newTextBasedMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((playingVsCpu) &&(boardIsEnabled == 1)){
                    placeTextBasedMoveAgainstCPU();
                }else if(playingVsHuman){
                    placeTextBasedMoveVsHuman();
                }
            }
        });

        textViewPlayer1 = findViewById(R.id.text_view_v1);
        textViewPlayer2 = findViewById(R.id.text_view2_v1);

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                for(int k = 0; k < 3; k++){
                    String buttonID = "button_" + i + j + k;
                    config[i][j][k] = '-';
                    int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                    buttons[i][j][k] = findViewById(resID);
                    buttons[i][j][k].setOnClickListener(this);
                }
            }
        }

        buttonReset = findViewById(R.id.new_game_button_v1);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        createRecyclerViewList();
        buildRecyclerView();
        loadData();
        updatePointsText();

        if(flagWallObstaclePlacementEnabled == 1){
            placeInitialWallObstacles();
        }
    }

    public void addNewTextBasedSpecMoveFromPlayer(int position, int numBoard, int numRow, int numColumn, char symbol){
        mExampleList.add(position, new ExampleItem("" + arrayChosenRow[numRow]
                + " " + arrayChosenColumn[numColumn] + " "
                + arrayChosenBoard[numBoard], "" + symbol));
        mLayoutManager.scrollToPosition(0);
        mAdapter.notifyItemInserted(position);
    }

    public void addWallObstaclesToRecyclerView(int position, int numBoard, int numRow, int numColumn, char symbol){
        mExampleList.add(position, new ExampleItem("" + arrayChosenRow[numRow]
                + " " + arrayChosenColumn[numColumn] + " "
                + arrayChosenBoard[numBoard], "" + symbol));
        mLayoutManager.scrollToPosition(0);
        mAdapter.notifyItemInserted(position);
    }

    public void createRecyclerViewList(){
        mExampleList = new ArrayList<>();
    }

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void clearRecyclerViewData(){
        int size = mExampleList.size();
        mExampleList.clear();
        mAdapter.notifyItemRangeChanged(0, size);
    }

    public void placeTextBasedMoveVsHuman(){
        int numBoard = 0, numRow = 0, numColumn = 0;

        switch (arrayChosenRow[stringRowIndex]) {
            case "3":
                numRow = 0;
                break;
            case "2":
                numRow = 1;
                break;
            case "1":
                numRow = 2;
                break;
        }

        switch (arrayChosenColumn[stringColumnIndex]) {
            case "A":
                numColumn = 0;
                break;
            case "B":
                numColumn = 1;
                break;
            case "C":
                numColumn = 2;
                break;
        }

        switch (arrayChosenBoard[stringBoardIndex]) {
            case "Top":
                numBoard = 0;
                break;
            case "Core":
                numBoard = 1;
                break;
            case "Bottom":
                numBoard = 2;
                break;
        }

        if(boardIsEnabled == 1){
            if (config[numBoard][numRow][numColumn] != '-'){
                return;
            }

            if(player1Turn){
                config[numBoard][numRow][numColumn] = player1symbol;
                if(player1symbol == 'X'){
                    buttons[numBoard][numRow][numColumn].setText(R.string.emoji_x);
                }else{
                    buttons[numBoard][numRow][numColumn].setText(R.string.emoji_o);
                }
                addNewTextBasedSpecMoveFromPlayer(0, numBoard, numRow, numColumn, player1symbol);

            }else{
                config[numBoard][numRow][numColumn] = player2symbol;
                if(player2symbol == 'X'){
                    buttons[numBoard][numRow][numColumn].setText(R.string.emoji_x);
                }else{
                    buttons[numBoard][numRow][numColumn].setText(R.string.emoji_o);
                }
                addNewTextBasedSpecMoveFromPlayer(0, numBoard, numRow, numColumn, player2symbol);
            }


            roundCount++;

            if(checkForWin()){
                if(player1Turn){
                    player1Wins();
                }else{
                    player2Wins();
                }
            } else if (roundCount == 27){
                draw();
            } else{
                player1Turn = !player1Turn;
            }
        }
    }

    public void placeTextBasedMoveAgainstCPU(){
        int numBoard = 0, numRow = 0, numColumn = 0;

        switch (arrayChosenRow[stringRowIndex]) {
            case "3":
                numRow = 0;
                break;
            case "2":
                numRow = 1;
                break;
            case "1":
                numRow = 2;
                break;
        }

        switch (arrayChosenColumn[stringColumnIndex]) {
            case "A":
                numColumn = 0;
                break;
            case "B":
                numColumn = 1;
                break;
            case "C":
                numColumn = 2;
                break;
        }

        switch (arrayChosenBoard[stringBoardIndex]) {
            case "Top":
                numBoard = 0;
                break;
            case "Core":
                numBoard = 1;
                break;
            case "Bottom":
                numBoard = 2;
                break;
        }

        if (config[numBoard][numRow][numColumn] != '-'){
            return;
        }


        config[numBoard][numRow][numColumn] = symbolCharX;
        buttons[numBoard][numRow][numColumn].setText(R.string.emoji_x);
        addNewTextBasedSpecMoveFromPlayer(0, numBoard, numRow, numColumn, symbolCharX);


        OneMove newMove = new OneMove();
        newMove.board = numBoard;
        newMove.row = numRow;
        newMove.column = numColumn;

        if(checkWin(symbolCharX, newMove))
        {
            TastyToast.makeText(getApplicationContext(), "Congrats! You won! Press New Game to play again.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

            player1Points++;
            win = true;

            updatePointsText();
            disableBoard();
        }
        else
        {
            computerPlays();
        }

    }


    @Override
    public void onClick(View v) {
        if((playingVsCpu) &&(boardIsEnabled == 1)){
            playVsCpuOnClick(v);
        }else if(playingVsHuman){
            playVsHumanOnClick(v);
        }
    }



    public void playVsHumanOnClick(View v){
        int numBoard, numRow, numColumn;

        if(boardIsEnabled == 1){
            if (!((Button) v).getText().toString().equals("")) {
                return;
            }

            if(player1Turn){
                if(player1symbol == 'X'){
                    ((Button) v).setText(R.string.emoji_x);
                    numBoard = Integer.parseInt(v.getTag().toString().substring(2,3));
                    numRow = Integer.parseInt(v.getTag().toString().substring(3,4));
                    numColumn = Integer.parseInt(v.getTag().toString().substring(4,5));
                    addNewTextBasedSpecMoveFromPlayer(0, numBoard, numRow, numColumn, 'X');
                }else{
                    ((Button) v).setText(R.string.emoji_o);
                    numBoard = Integer.parseInt(v.getTag().toString().substring(2,3));
                    numRow = Integer.parseInt(v.getTag().toString().substring(3,4));
                    numColumn = Integer.parseInt(v.getTag().toString().substring(4,5));
                    addNewTextBasedSpecMoveFromPlayer(0, numBoard, numRow, numColumn, 'O');
                }
            }else{
                if(player2symbol == 'X'){
                    ((Button) v).setText(R.string.emoji_x);
                    numBoard = Integer.parseInt(v.getTag().toString().substring(2,3));
                    numRow = Integer.parseInt(v.getTag().toString().substring(3,4));
                    numColumn = Integer.parseInt(v.getTag().toString().substring(4,5));
                    addNewTextBasedSpecMoveFromPlayer(0, numBoard, numRow, numColumn, 'X');
                }else{
                    ((Button) v).setText(R.string.emoji_o);
                    numBoard = Integer.parseInt(v.getTag().toString().substring(2,3));
                    numRow = Integer.parseInt(v.getTag().toString().substring(3,4));
                    numColumn = Integer.parseInt(v.getTag().toString().substring(4,5));
                    addNewTextBasedSpecMoveFromPlayer(0, numBoard, numRow, numColumn, 'O');
                }
            }

            roundCount++;

            if(checkForWin()){
                if(player1Turn){
                    player1Wins();
                }else{
                    player2Wins();
                }
            } else if (roundCount == 27){
                draw();
            } else{
                player1Turn = !player1Turn;
            }
        }
    }


    private boolean checkForWin() {
        String[][][] field = new String[3][3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++){
                    field[i][j][k] = buttons[i][j][k].getText().toString();
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0][2].equals(field[i][1][2])
                    && field[i][0][2].equals(field[i][2][2])
                    && !field[i][0][2].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i][2].equals(field[1][i][2])
                    && field[0][i][2].equals(field[2][i][2])
                    && !field[0][i][2].equals("")) {
                return true;
            }
        }

        if (field[0][0][2].equals(field[1][1][2])
                && field[0][0][2].equals(field[2][2][2])
                && !field[0][0][2].equals("")) {
            return true;
        }

        if (field[0][2][2].equals(field[1][1][2])
                && field[0][2][2].equals(field[2][0][2])
                && !field[0][2][2].equals("")) {
            return true;
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0][1].equals(field[i][1][1])
                    && field[i][0][1].equals(field[i][2][1])
                    && !field[i][0][1].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i][1].equals(field[1][i][1])
                    && field[0][i][1].equals(field[2][i][1])
                    && !field[0][i][1].equals("")) {
                return true;
            }
        }

        if (field[0][0][1].equals(field[1][1][1])
                && field[0][0][1].equals(field[2][2][1])
                && !field[0][0][1].equals("")) {
            return true;
        }

        if (field[0][2][1].equals(field[1][1][1])
                && field[0][2][1].equals(field[2][0][1])
                && !field[0][2][1].equals("")) {
            return true;
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0][0].equals(field[i][1][0])
                    && field[i][0][0].equals(field[i][2][0])
                    && !field[i][0][0].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i][0].equals(field[1][i][0])
                    && field[0][i][0].equals(field[2][i][0])
                    && !field[0][i][0].equals("")) {
                return true;
            }
        }

        if (field[0][0][0].equals(field[1][1][0])
                && field[0][0][0].equals(field[2][2][0])
                && !field[0][0][0].equals("")) {
            return true;
        }

        if (field[0][2][0].equals(field[1][1][0])
                && field[0][2][0].equals(field[2][0][0])
                && !field[0][2][0].equals("")) {
            return true;
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0][0].equals(field[i][0][1])
                    && field[i][0][1].equals(field[i][0][2])
                    && !field[i][0][2].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][1][0].equals(field[i][1][1])
                    && field[i][1][1].equals(field[i][1][2])
                    && !field[i][1][2].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][2][0].equals(field[i][2][1])
                    && field[i][2][1].equals(field[i][2][2])
                    && !field[i][2][2].equals("")) {
                return true;
            }
        }

        if (field[0][0][2].equals(field[1][0][1])
                && field[0][0][2].equals(field[2][0][0])
                && !field[0][0][2].equals("")) {
            return true;
        }

        if (field[0][0][0].equals(field[1][0][1])
                && field[0][0][0].equals(field[2][0][2])
                && !field[0][0][0].equals("")) {
            return true;
        }

        if (field[0][1][2].equals(field[1][1][1])
                && field[0][1][2].equals(field[2][1][0])
                && !field[0][1][2].equals("")) {
            return true;
        }

        if (field[2][1][2].equals(field[1][1][1])
                && field[2][1][2].equals(field[0][1][0])
                && !field[2][1][2].equals("")) {
            return true;
        }

        if (field[0][2][2].equals(field[1][2][1])
                && field[0][2][2].equals(field[2][2][0])
                && !field[0][2][2].equals("")) {
            return true;
        }

        if (field[2][2][2].equals(field[1][2][1])
                && field[2][2][2].equals(field[0][2][0])
                && !field[2][2][2].equals("")) {
            return true;
        }

        if (field[1][0][2].equals(field[1][1][1])
                && field[1][0][2].equals(field[1][2][0])
                && !field[1][0][2].equals("")) {
            return true;
        }

        if (field[0][0][0].equals(field[1][1][1])
                && field[1][1][1].equals(field[2][2][2])
                && !field[2][2][2].equals("")) {
            return true;
        }

        if (field[0][0][2].equals(field[1][1][1])
                && field[1][1][1].equals(field[2][2][0])
                && !field[2][2][0].equals("")) {
            return true;
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0][2].equals(field[i][1][1])
                    && field[i][1][1].equals(field[i][2][0])
                    && !field[i][2][0].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0][0].equals(field[i][1][1])
                    && field[i][1][1].equals(field[i][2][2])
                    && !field[i][2][2].equals(""))  {
                return true;
            }
        }

        return false;
    }

    private void player1Wins() {
        player1Points++;
        TastyToast.makeText(getApplicationContext(), "Player 1 has won! Press New Game to play again.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
        updatePointsText();
        disableBoard();
    }

    private void player2Wins() {
        player2Points++;
        TastyToast.makeText(getApplicationContext(), "Player 2 has won! Press New Game to play again.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
        updatePointsText();
        disableBoard();
    }

    private void draw() {
        TastyToast.makeText(getApplicationContext(), "There was a draw! Press New Game to play again.", TastyToast.LENGTH_LONG, TastyToast.INFO);
        disableBoard();
    }


    public void placeInitialWallObstacles(){
        if((flagWallObstacleHasBeenPlaced == 0) && (flagWallObstaclePlacementEnabled == 1)){
            int topBoardWallObstacleRowNr1, topBoardWallObstacleColumnNr1;
            int topBoardWallObstacleRowNr2, topBoardWallObstacleColumnNr2;

            int bottomBoardWallObstacleRowNr1, bottomBoardWallObstacleColumnNr1;
            int bottomBoardWallObstacleRowNr2, bottomBoardWallObstacleColumnNr2;

            Random random = new Random();

            do{
                topBoardWallObstacleRowNr1 = random.nextInt(2 + 1);
                topBoardWallObstacleColumnNr1 = random.nextInt(2 + 1);
            }while((config[0][topBoardWallObstacleRowNr1][topBoardWallObstacleColumnNr1] == symbolCharX) ||
                    (config[0][topBoardWallObstacleRowNr1][topBoardWallObstacleColumnNr1] == symbolCharO) ||
                    (config[0][topBoardWallObstacleRowNr1][topBoardWallObstacleColumnNr1] == wallPiece)   );

            config[0][topBoardWallObstacleRowNr1][topBoardWallObstacleColumnNr1] = wallPiece;
            buttons[0][topBoardWallObstacleRowNr1][topBoardWallObstacleColumnNr1].setText(R.string.emoji_lock);
            addWallObstaclesToRecyclerView(0, 0, topBoardWallObstacleRowNr1, topBoardWallObstacleColumnNr1, 'W');


            do{
                topBoardWallObstacleRowNr2 = random.nextInt(2 + 1);
                topBoardWallObstacleColumnNr2 = random.nextInt(2 + 1);
            }while((config[0][topBoardWallObstacleRowNr2][topBoardWallObstacleColumnNr2] == symbolCharX) ||
                    (config[0][topBoardWallObstacleRowNr2][topBoardWallObstacleColumnNr2] == symbolCharO) ||
                    (config[0][topBoardWallObstacleRowNr2][topBoardWallObstacleColumnNr2] == wallPiece)   );


            config[0][topBoardWallObstacleRowNr2][topBoardWallObstacleColumnNr2] = wallPiece;
            buttons[0][topBoardWallObstacleRowNr2][topBoardWallObstacleColumnNr2].setText(R.string.emoji_lock);
            addWallObstaclesToRecyclerView(0, 0, topBoardWallObstacleRowNr2, topBoardWallObstacleColumnNr2, 'W');


            config[1][1][1] = wallPiece;
            buttons[1][1][1].setText(R.string.emoji_lock2);
            addWallObstaclesToRecyclerView(0, 1, 1, 1, 'W');


            do{
                bottomBoardWallObstacleRowNr1 = random.nextInt(2 + 1);
                bottomBoardWallObstacleColumnNr1 = random.nextInt(2 + 1);
            }while((config[2][bottomBoardWallObstacleRowNr1][bottomBoardWallObstacleColumnNr1] == symbolCharX) ||
                    (config[2][bottomBoardWallObstacleRowNr1][bottomBoardWallObstacleColumnNr1] == symbolCharO) ||
                    (config[2][bottomBoardWallObstacleRowNr1][bottomBoardWallObstacleColumnNr1] == wallPiece)   );

            config[2][bottomBoardWallObstacleRowNr1][bottomBoardWallObstacleColumnNr1] = wallPiece;
            buttons[2][bottomBoardWallObstacleRowNr1][bottomBoardWallObstacleColumnNr1].setText(R.string.emoji_lock);
            addWallObstaclesToRecyclerView(0, 2, bottomBoardWallObstacleRowNr1, bottomBoardWallObstacleColumnNr1, 'W');


            do{
                bottomBoardWallObstacleRowNr2 = random.nextInt(2 + 1);
                bottomBoardWallObstacleColumnNr2 = random.nextInt(2 + 1);
            }while((config[2][bottomBoardWallObstacleRowNr2][bottomBoardWallObstacleColumnNr2] == symbolCharX) ||
                    (config[2][bottomBoardWallObstacleRowNr2][bottomBoardWallObstacleColumnNr2] == symbolCharO) ||
                    (config[2][bottomBoardWallObstacleRowNr2][bottomBoardWallObstacleColumnNr2] == wallPiece)   );

            config[2][bottomBoardWallObstacleRowNr2][bottomBoardWallObstacleColumnNr2] = wallPiece;
            buttons[2][bottomBoardWallObstacleRowNr2][bottomBoardWallObstacleColumnNr2].setText(R.string.emoji_lock);
            addWallObstaclesToRecyclerView(0, 2, bottomBoardWallObstacleRowNr2, bottomBoardWallObstacleColumnNr2, 'W');


            flagWallObstacleHasBeenPlaced = 1;
        }


    }


    public void insertWallObstacle(View v){

        if((flagWallObstacleHasBeenPlaced == 0) && (flagWallObstaclePlacementEnabled == 1)) {
            int numBoard, numRow, numColumn;

            if (!((Button) v).getText().toString().equals("")) {
                return;
            }else{
                numBoard = Integer.parseInt(v.getTag().toString().substring(2,3));
                numRow = Integer.parseInt(v.getTag().toString().substring(3,4));
                numColumn = Integer.parseInt(v.getTag().toString().substring(4,5));
            }

            config[numBoard][numRow][numColumn] = wallPiece;
            buttons[numBoard][numRow][numColumn].setText("" + wallPiece);

            flagWallObstacleHasBeenPlaced = 1;
        }

    }

    public void playVsCpuOnClick(View v){

        insertWallObstacle(v);

        int numBoard, numRow, numColumn;

        if (!((Button) v).getText().toString().equals("")) {
            return;
        }else{
            numBoard = Integer.parseInt(v.getTag().toString().substring(2,3));
            numRow = Integer.parseInt(v.getTag().toString().substring(3,4));
            numColumn = Integer.parseInt(v.getTag().toString().substring(4,5));
            addNewTextBasedSpecMoveFromPlayer(0, numBoard, numRow, numColumn, symbolCharX);
        }


        config[numBoard][numRow][numColumn] = symbolCharX;
        buttons[numBoard][numRow][numColumn].setText(R.string.emoji_x);


        OneMove newMove = new OneMove();
        newMove.board = numBoard;
        newMove.row = numRow;
        newMove.column = numColumn;

        if(checkWin(symbolCharX, newMove))
        {
            TastyToast.makeText(getApplicationContext(), "Congrats! You won! Press New Game to play again.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);


            player1Points++;
            win = true;

            updatePointsText();
            disableBoard();
        }
        else
        {
            computerPlays();
        }


    }

    private void updatePointsText() {
        if(playingVsCpu){
            textViewPlayer1.setText("Player: " + player1Points);
            textViewPlayer2.setText("CPU: " + player2Points);
        }else{
            textViewPlayer1.setText("Player1: " + player1Points);
            textViewPlayer2.setText("Player2: " + player2Points);
        }
    }

    private void resetGame(){
        updatePointsText();

        clearRecyclerViewData();


        boardIsEnabled = 1;
        flagWallObstaclePlacementEnabled = 0;
        flagWallObstacleHasBeenPlaced = 0;
        clearBoard();
        loadData();


        if(flagWallObstaclePlacementEnabled == 1){
            placeInitialWallObstacles();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }


    public class OneMove
    {
        int board;
        int row;
        int column;
    }


    public void clearBoard()
    {

        win = false;
        lookAheadCounter = 0;

        for (int i = 0; i <= 2; i++)
        {
            for (int j = 0; j <= 2; j++)
            {
                for(int k = 0; k <= 2; k++)
                {
                    config[i][j][k] = '-';
                    buttons[i][j][k].setText("");

                }
            }
        }

        finalWin = new int[3];
        roundCount = 0;
        player1Turn = true;
    }

    public void disableBoard()
    {
        boardIsEnabled = 0;
    }

    protected void computerPlayRandom()
    {
        Random random = new Random();
        randomRowNr = random.nextInt(3);
        randomColumnNr = random.nextInt(3);
        randomBoardNr = random.nextInt(2);
        if(randomBoardNr == 1){
            randomBoardNr = 2;
        }

        config[randomBoardNr][randomRowNr][randomColumnNr] = symbolCharO;
        buttons[randomBoardNr][randomRowNr][randomColumnNr].setText(R.string.emoji_o);
        addNewTextBasedSpecMoveFromPlayer(0, randomBoardNr, randomRowNr, randomColumnNr, symbolCharO);

    }

    protected void computerPlays()
    {
        int bestScore;
        int hValue;
        OneMove nextMove;
        int bestScoreBoard = -1;
        int bestScoreRow = -1;
        int bestScoreColumn = -1;

        bestScore = -1000;
        outerLoopLabel:
        for (int i = 0; i <= 2; i++)
        {
            for (int j = 0; j <= 2; j++)
            {
                for(int k = 0; k <= 2; k++)
                {
                    if(config[i][j][k] == '-')
                    {
                        nextMove = new OneMove();
                        nextMove.board = i;
                        nextMove.row = j;
                        nextMove.column = k;

                        if(checkWin(symbolCharO, nextMove))
                        {
                            config[i][j][k] = symbolCharO;
                            buttons[i][j][k].setText(R.string.emoji_o);
                            addNewTextBasedSpecMoveFromPlayer(0, i, j, k, symbolCharO);
                            win = true;
                            player2Points++;

                            updatePointsText();
                            disableBoard();
                            TastyToast.makeText(getApplicationContext(), "The CPU has won! Press New Game to play again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                            break outerLoopLabel;
                        }
                        else
                        {
                            if(difficulty != 1)
                            {
                                hValue = lookAhead(symbolCharX, -1000, 1000);
                            }
                            else
                            {
                                hValue = heuristic();
                            }

                            lookAheadCounter = 0;

                            if(hValue >= bestScore)
                            {
                                bestScore = hValue;
                                bestScoreBoard = i;
                                bestScoreRow = j;
                                bestScoreColumn = k;
                                config[i][j][k] = '-';
                            }
                            else
                            {
                                config[i][j][k] = '-';
                            }
                        }
                    }
                }
            }
        }

        if(!win)
        {
            config[bestScoreBoard][bestScoreRow][bestScoreColumn] = symbolCharO;
            buttons[bestScoreBoard][bestScoreRow][bestScoreColumn].setText(R.string.emoji_o);
            addNewTextBasedSpecMoveFromPlayer(0, bestScoreBoard, bestScoreRow, bestScoreColumn, symbolCharO);
        }
    }


    private int lookAhead(char c, int a, int b)
    {
        int alpha = a;
        int beta = b;

        if(lookAheadCounter <= totalLooksAhead)
        {

            lookAheadCounter++;
            if(c == symbolCharO)
            {
                int hValue;
                OneMove nextMove;

                for (int i = 0; i <= 2; i++)
                {
                    for (int j = 0; j <= 2; j++)
                    {
                        for(int k = 0; k <= 2; k++)
                        {
                            if(config[i][j][k] == '-')
                            {
                                nextMove = new OneMove();
                                nextMove.board = i;
                                nextMove.row = j;
                                nextMove.column = k;

                                if(checkWin(symbolCharO, nextMove))
                                {
                                    config[i][j][k] = '-';
                                    return 1000;
                                }
                                else
                                {
                                    hValue = lookAhead(symbolCharX, alpha, beta);
                                    if(hValue > alpha)
                                    {
                                        alpha = hValue;
                                        config[i][j][k] = '-';
                                    }
                                    else
                                    {
                                        config[i][j][k] = '-';
                                    }
                                }

                                if (alpha >= beta)
                                    break;
                            }
                        }
                    }
                }

                return alpha;
            }

            else
            {
                int hValue;
                OneMove nextMove;

                for (int i = 0; i <= 2; i++)
                {
                    for (int j = 0; j <= 2; j++)
                    {
                        for(int k = 0; k <= 2; k++)
                        {

                            if(config[i][j][k] == '-')
                            {

                                nextMove = new OneMove();
                                nextMove.board = i;
                                nextMove.row = j;
                                nextMove.column = k;

                                if(checkWin(symbolCharX, nextMove))
                                {
                                    config[i][j][k] = '-';
                                    return -1000;
                                }
                                else
                                {
                                    hValue = lookAhead(symbolCharO, alpha, beta);
                                    if(hValue < beta)
                                    {
                                        beta = hValue;
                                        config[i][j][k] = '-';
                                    }
                                    else
                                    {
                                        config[i][j][k] = '-';
                                    }
                                }

                                if (alpha >= beta)
                                    break;
                            }
                        }
                    }
                }

                return beta;
            }
        }
        else
        {
            return heuristic();
        }
    }


    private int heuristic()
    {
        return (checkAvailable(symbolCharO) - checkAvailable(symbolCharX));
    }

    private boolean checkWin(char c, OneMove pos)
    {
        config[pos.board][pos.row][pos.column] = c;

        int[][] wins = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {9, 10, 11}, {12, 13, 14}, {15, 16, 17}, {18, 19, 20},
                {21, 22, 23}, {24, 25, 26},

                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {9, 12, 15}, {10, 13, 16}, {11, 14, 17}, {18, 21, 24},
                {19, 22, 25}, {20, 23, 26},

                {0, 4, 8}, {2, 4, 6}, {9, 13, 17}, {11, 13, 15},
                {18, 22, 26}, {20, 22, 24},

                {0, 9, 18}, {1, 10, 19}, {2, 11, 20}, {3, 12, 21}, {4, 13, 22}, {5, 14, 23}, {6, 15, 24},
                {7, 16, 25}, {8, 17, 26},

                {0, 12, 24}, {1, 13, 25}, {2, 14, 26}, {6, 12, 18}, {7, 13, 19}, {8, 14, 20}, {0, 10, 20},
                {3, 13, 23}, {6, 16, 26},{2, 10, 18}, {5, 13, 21}, {8, 16, 24}, {0, 13, 26}, {2, 13, 24},
                {6, 13, 20}, {8, 13, 18},
        };

        int[] gameBoard = new int[27];

        int counter = 0;

        for (int i = 0; i <= 2; i++)
        {
            for (int j = 0; j <= 2; j++)
            {
                for(int k = 0; k <= 2; k++)
                {
                    if(config[i][j][k] == c)
                    {
                        gameBoard[counter] = 1;
                    }
                    else
                    {
                        gameBoard[counter] = 0;
                    }
                    counter++;
                }
            }
        }

        for (int i = 0; i <= 48; i++)
        {
            counter = 0;
            for (int j = 0; j <= 2; j++)
            {
                if(gameBoard[wins[i][j]] == 1)
                {
                    counter++;

                    finalWin[j] = wins[i][j];
                    if(counter == 3)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    private int checkAvailable(char c)
    {
        int winCounter = 0;

        int[][] wins = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {9, 10, 11}, {12, 13, 14}, {15, 16, 17}, {18, 19, 20},
                {21, 22, 23}, {24, 25, 26},

                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {9, 12, 15}, {10, 13, 16}, {11, 14, 17}, {18, 21, 24},
                {19, 22, 25}, {20, 23, 26},

                {0, 4, 8}, {2, 4, 6}, {9, 13, 17}, {11, 13, 15},
                {18, 22, 26}, {20, 22, 24},

                {0, 9, 18}, {1, 10, 19}, {2, 11, 20}, {3, 12, 21}, {4, 13, 22}, {5, 14, 23}, {6, 15, 24},
                {7, 16, 25}, {8, 17, 26},

                {0, 12, 24}, {1, 13, 25}, {2, 14, 26}, {6, 12, 18}, {7, 13, 19}, {8, 14, 20}, {0, 10, 20},
                {3, 13, 23}, {6, 16, 26},{2, 10, 18}, {5, 13, 21}, {8, 16, 24}, {0, 13, 26}, {2, 13, 24},
                {6, 13, 20}, {8, 13, 18},
        };

        int[] gameBoard = new int[27];

        int counter = 0;

        for (int i = 0; i <= 2; i++)
        {
            for (int j = 0; j <= 2; j++)
            {
                for(int k = 0; k <= 2; k++)
                {
                    if(config[i][j][k] == c || config[i][j][k] == '-')
                        gameBoard[counter] = 1;
                    else
                        gameBoard[counter] = 0;

                    counter++;
                }
            }
        }

        for (int i = 0; i <= 48; i++)
        {
            counter = 0;
            for (int j = 0; j <= 2; j++)
            {
                if(gameBoard[wins[i][j]] == 1)
                {
                    counter++;

                    finalWin[j] = wins[i][j];
                    if(counter == 3)
                        winCounter++;
                }
            }
        }

        return winCounter;
    }


    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_CONSTANT, MODE_PRIVATE);
        sharedPrefsForChosenFirstPlayer = sharedPreferences.getString(FIRST_PLAYER_SELECTION_CONSTANT, "CPU_First");
        sharedPrefsForDifficultySelected = sharedPreferences.getString(DIFFICULTY_SELECTION_CONSTANT, "E");
        sharedPrefsForTypeOfOpponent = sharedPreferences.getString(TYPE_OF_OPPONENT_SELECTION, "C");
        sharedPrefsForMatricesButtonsVisibility = sharedPreferences.getString(BUTTON_MATRICES_VISIBILITY, "V");
        sharedPrefsForEnabledWallObstacles = sharedPreferences.getString(WALL_SELECTION_CONSTANT, "D");

        updatePointsText();

        if(sharedPrefsForEnabledWallObstacles.equals("D")) {
            flagWallObstaclePlacementEnabled = 0;
        } else {
            flagWallObstaclePlacementEnabled = 1;

        }

        if(sharedPrefsForMatricesButtonsVisibility.equals("I")) {
            viewWithMatricesButtons.setVisibility(View.INVISIBLE);
            imageView.setImageResource(R.drawable.tape3);
            linearLayoutWithMatrices.removeAllViews();
            linearLayoutWithMatrices.addView(imageView);
        }

        symbolCharX = 'X';
        symbolCharO = 'O';


        if(sharedPrefsForTypeOfOpponent.equals("C")) {
            playingVsCpu = true;
            playingVsHuman = false;
        } else {
            playingVsCpu = false;
            playingVsHuman = true;
        }

        if(sharedPrefsForChosenFirstPlayer.equals("CPU_First")) {
            humanFirst = false;

            if(sharedPrefsForTypeOfOpponent.equals("C")){
                computerPlayRandom();
            }

        } else {
            humanFirst = true;
        }

        if(sharedPrefsForDifficultySelected.equals("E")) {
            difficulty = 1;
            totalLooksAhead = 1;
        } else if(sharedPrefsForDifficultySelected.equals("M")) {
            difficulty = 2;
            totalLooksAhead = 2;
        } else {
            difficulty = 3;
            totalLooksAhead = 6;
        }
    }
}
