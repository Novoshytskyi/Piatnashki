package com.example.piatnashki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    int uzeHodov = 0; // Количество сделанных ходов

    // Прописывание menu ==========================================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        TextView headerView = (TextView) findViewById(R.id.header);
        switch (id){
            case R.id.exit:
                // headerView.setText("Выход");
                finish();
                System.exit(0);
                return true;
            case R.id.restart:
                // headerView.setText("Перезапуск");
                uzeHodov = 0;
                shuffle();
                ((TextView)findViewById(R.id.hody)).setText("Новая игра"); //
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //=============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Подключаем свайпы
        // 1) android:id="@+id/layout"
        // 2) On..Listener
        findViewById(R.id.activity_main).setOnTouchListener(
                new OnSwipeTouchListener(MainActivity.this){
                    @Override
                    public void onSwipeRight(){
                        if(canMoveRight()) {
                            moveRight();
                            if(isFinish()){
                                endOfGame();
                            }
                            sdelanoHodov();
                        } else {
                            Toast.makeText(MainActivity.this, "Не \"трамбуйте\" пятнашки вправо!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onSwipeBottom(){
                        if(canMoveBottom()) {
                            moveBottom();
                            if(isFinish()){
                                endOfGame();
                            }
                            sdelanoHodov();
                        } else {
                            Toast.makeText(MainActivity.this, "Не \"трамбуйте\" пятнашки вниз!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onSwipeLeft(){
                        if(canMoveLeft()){
                            moveLeft();
                            if(isFinish()){
                                endOfGame();
                            }
                            sdelanoHodov();
                        } else {
                            Toast.makeText(MainActivity.this, "Не \"трамбуйте\" пятнашки влево!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onSwipeTop(){
                        if(canMoveTop()) {
                            moveTop();
                            if(isFinish()){
                                endOfGame();
                            }
                            sdelanoHodov();
                        } else {
                            //Toast.makeText(MainActivity.this, "Дальше хода НЕТ - Вы уперлись!", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, "Не \"трамбуйте\" пятнашки вверх!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        // Для сохранения состояния при повороте экрана.
        if(savedInstanceState == null) {
            shuffle(); // Запуск метода перемешивания пятнашек.
        }
    }

    @Override // Событие при сохранении состояния.
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("s1", 10);
        Toast.makeText(MainActivity.this, "Состояние осхраняется!", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < 16; i++){
            outState.putString("cell" + i, getCellByNumber(i).getText().toString());
        }
        outState.putInt("emptyCellNumber", getEmptyCellNumber());
    }

    @Override // Событие при восстанавлении состояния.
    protected void onRestoreInstanceState(@NonNull Bundle outState) {
        super.onRestoreInstanceState(outState);
        int s1 = outState.getInt("s1");
        Toast.makeText(MainActivity.this, "Состояние восстанавливается!" + s1, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < 16; i++){
            getCellByNumber(i).setText(outState.getString("cell"+i));
        }
        int savedEmptyCellNumber = outState.getInt("emptyCellNumber");
        if(savedEmptyCellNumber != 15){
            Drawable bg =getCellByNumber(15).getBackground();
            getCellByNumber(15).setBackground(getCellByNumber(savedEmptyCellNumber).getBackground());
            getCellByNumber(savedEmptyCellNumber).setBackground(bg);
        }
        //getCellByNumber(i).setText(outState.getString("cell"+i));
        //outState.putInt("emptyCellNumber", getEmptyCellNumber());
    }


    // Обмен элементов местами.
    private void swapCells(TextView c1, TextView c2){
        //Числа
        CharSequence
                c1Text = c1.getText(),
                c2Text = c2.getText();
        c1.setText(c2Text);
        c2.setText(c1Text);
        // Фон
        Drawable
                c1bg = c1.getBackground(),
                c2bg = c2.getBackground();
        c1.setBackground(c2bg);
        c2.setBackground(c1bg);
    }

    // Поиск элемента по "id".
    private TextView getCellByNumber(int n){
        return findViewById(
                getResources()
                        .getIdentifier(
                                "cell" + n,
                                "id",
                                getPackageName()
                        )
        );
    }

    // Метод определения индекса пустой ячейки
    private int getEmptyCellNumber(){
        for(int i = 0; i <16 ; i++){ // i = 0, а не i = 1
            if(getCellByNumber(i).getText().equals("") ){
                return i;
            }
        }
        return -1;
    }

    // Проверка возможности смещения вправо.
    private boolean canMoveRight(){
        return (getEmptyCellNumber() % 4 != 0);
    }

    // Проверка возможности смещения влево.
    private boolean canMoveLeft(){
        return getEmptyCellNumber() % 4 != 3;
    }

    // Проверка возможности смещения вверх.
    private boolean canMoveTop(){
        return getEmptyCellNumber() < 12;
    }

    // Проверка возможности смещения вниз.
    private boolean canMoveBottom(){
        return getEmptyCellNumber() > 3;
    }

    // Смещение вправо.
    private void moveRight(){
        swapCells(getCellByNumber(getEmptyCellNumber()), getCellByNumber(getEmptyCellNumber() - 1));
    }

    // Смещение влево.
    private void moveLeft(){
        swapCells(getCellByNumber(getEmptyCellNumber()), getCellByNumber(getEmptyCellNumber() + 1));
    }

    // Смещение вверх.
    private void moveTop(){
        swapCells(getCellByNumber(getEmptyCellNumber()), getCellByNumber(getEmptyCellNumber() + 4));
    }

    // Смещение вниз.
    private void moveBottom(){
        swapCells(getCellByNumber(getEmptyCellNumber()), getCellByNumber(getEmptyCellNumber() - 4));
    }

    // Выполнить 200-300 случайных ходов
    // Метод перемешивания пятнашек.
    private void shuffle(){
        Random rnd = new Random();
        int moves = 0;
        do{
            switch (rnd.nextInt(4)){
                case 0: if(canMoveRight()){
                    moveRight();
                    moves++;
                }
                break;
                case 1: if(canMoveLeft()){
                    moveLeft();
                    moves++;
                }
                break;
                case 2: if(canMoveTop()){
                    moveTop();
                    moves++;
                }
                break;
                case 3: if(canMoveBottom()){
                    moveBottom();
                    moves++;
                }
                break;
            }
        }
        //while (moves <50); // поэкспериментировать с числом ходов.
        while (moves <25); // поэкспериментировать с числом ходов.
    }

    // Проверка на собранное состояние
    private  boolean isFinish(){
        for(int i = 0; i < 15; ++i){
            if(! String.valueOf(i + 1)
                .contentEquals(getCellByNumber(i).getText())){
                return false;
            }
        }
        return true;
    }
    // Game over
    private void endOfGame(){
        new AlertDialog.Builder(this,R.style.Theme_AppCompat_DayNight_Dialog_Alert)
                .setTitle("Победа!")
                .setMessage("Сыграть еще раз?")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("Еще раз", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        uzeHodov = 0;
                        shuffle();
                        ((TextView)findViewById(R.id.hody)).setText("Новая игра"); // ??????????
                    }
                })

                .setNegativeButton("Выход", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                })
                .show();

    }

    // Метод подсчета количества сделанных ходов
    private void sdelanoHodov(){
        uzeHodov++;
        //Toast.makeText(MainActivity.this, "Ход №" +uzeHodov, Toast.LENGTH_SHORT).show();
        //hody
        ((TextView)findViewById(R.id.hody)).setText("Ход №" + uzeHodov);
    }



}