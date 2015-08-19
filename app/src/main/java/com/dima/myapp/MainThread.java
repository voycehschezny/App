package com.dima.myapp;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Dimon on 20.07.2015.
 */
public class MainThread extends Thread {

    private int FPS = 30;
    private SurfaceHolder surfaceHolder;
    private double averageFPS;
    private GameView gameView;
    private boolean running;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime; //ждущее время
        long totalTime = 0;//общее
        long frameCount = 0;//количество кадров
        long targetTime = 1000 / FPS;

        while (running) {
            startTime = System.nanoTime(); //возвращает наносекунды
            canvas = null; //холст пустой
            //обращения поиска холста для редактирования пикселей
            try {
                canvas = this.surfaceHolder.lockCanvas();//создание поверхности рисования
                synchronized (surfaceHolder)  //Означает, что только один поток может выполнять этот блок кода.
                {
                    this.gameView.update();//обновить обьект
                    this.gameView.draw(canvas);//рисовать обьект
                }

            } catch (Exception e) {
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;//время в миллисекундах
            waitTime = targetTime - timeMillis;//время ожидания

            try {
                this.sleep(waitTime);
            } catch (Exception e) {
            }

            totalTime = System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == FPS) {
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(averageFPS);
            }

        }
    }

    public void setRunning(boolean b) {
        running = b;
    }
}
