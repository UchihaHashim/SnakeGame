package com.hashim;

import javax.swing.*;

public class GameFrame extends JFrame {
    GameFrame(){
        this.setTitle("Snakzy");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new GamePanel());
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
