import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Frame extends JFrame {
    static Clip bgm;
    Frame(){
        JMenuBar menuBar = new JMenuBar();
        //menu game control
        JMenu gameMenu = new JMenu("Game");
        //sub menu buttons
        JMenuItem startMenuItem = new JMenuItem("Start");
        JMenuItem pauseMenuItem = new JMenuItem("Pause & Resume");
        JMenuItem quitMenuItem = new JMenuItem("Quit");
        //binding sub button to main button
        gameMenu.add(startMenuItem);
        gameMenu.add(pauseMenuItem);
        gameMenu.add(quitMenuItem);
        //menu game settings
        JMenu settingMenu = new JMenu("Settings");
        //sub menu resolution
        JMenu resolutionMenu = new JMenu("Resolution");
        //resolution choices with only single choice
        JRadioButtonMenuItem resolutionMenuItem1 =
                new JRadioButtonMenuItem("800 x 600");
        JRadioButtonMenuItem resolutionMenuItem2 =
                new JRadioButtonMenuItem("1020 x 760");
        JRadioButtonMenuItem resolutionMenuItem3 =
                new JRadioButtonMenuItem("1360 x 760");
        JRadioButtonMenuItem resolutionMenuItem4 =
                new JRadioButtonMenuItem("1400 x 1040");
        JRadioButtonMenuItem resolutionMenuItem5 =
                new JRadioButtonMenuItem("1600 x 900");
        JRadioButtonMenuItem resolutionMenuItem6 =
                new JRadioButtonMenuItem("1920 x 1080");
        JRadioButtonMenuItem resolutionMenuItem7 =
                new JRadioButtonMenuItem("2560 x 1440");
        JRadioButtonMenuItem resolutionMenuItem8 =
                new JRadioButtonMenuItem("3840 x 2160");
        //binding into one group to activate single choice
        ButtonGroup resolutionGroup = new ButtonGroup();
        resolutionGroup.add(resolutionMenuItem1);
        resolutionGroup.add(resolutionMenuItem2);
        resolutionGroup.add(resolutionMenuItem3);
        resolutionGroup.add(resolutionMenuItem4);
        resolutionGroup.add(resolutionMenuItem5);
        resolutionGroup.add(resolutionMenuItem6);
        resolutionGroup.add(resolutionMenuItem7);
        resolutionGroup.add(resolutionMenuItem8);
        //default choice
        resolutionMenuItem1.setSelected(true);
        //binding choices to sub button
        resolutionMenu.add(resolutionMenuItem1);
        resolutionMenu.add(resolutionMenuItem2);
        resolutionMenu.add(resolutionMenuItem3);
        resolutionMenu.add(resolutionMenuItem4);
        resolutionMenu.add(resolutionMenuItem5);
        resolutionMenu.add(resolutionMenuItem6);
        resolutionMenu.add(resolutionMenuItem7);
        resolutionMenu.add(resolutionMenuItem8);
        //binding sub button to main button
        settingMenu.add(resolutionMenu);

        JMenu bgmMenu = new JMenu("Music");

        JRadioButtonMenuItem bgmMenuItem1 =
                new JRadioButtonMenuItem("On");
        JRadioButtonMenuItem bgmMenuItem2 =
                new JRadioButtonMenuItem("Off");

        ButtonGroup bgmGroup = new ButtonGroup();
        bgmGroup.add(bgmMenuItem1);
        bgmGroup.add(bgmMenuItem2);

        bgmMenuItem1.setSelected(true);
        bgmMenu.add(bgmMenuItem1);
        bgmMenu.add(bgmMenuItem2);

        settingMenu.add(bgmMenu);

        //binding main button to main bar
        menuBar.add(gameMenu);
        //binding main button to main bar
        menuBar.add(settingMenu);
        //hit start to trigger Key_R and restart the panel
        startMenuItem.addActionListener(e -> {
            try {
                Robot robot = new Robot();
                robot.keyPress(KeyEvent.VK_R);
                robot.keyRelease(KeyEvent.VK_R);
            }
            catch (AWTException eA) {
                eA.printStackTrace();
            }
        });
        //hit pause to trigger Key_P and pause the panel
        pauseMenuItem.addActionListener(e -> {
            try {
                Robot robot = new Robot();
                robot.keyPress(KeyEvent.VK_P);
                robot.keyRelease(KeyEvent.VK_P);
            }
            catch (AWTException eA) {
                eA.printStackTrace();
            }
        });
        //hit quit to exit
        quitMenuItem.addActionListener(e -> System.exit(0));
        //hit resolution to trigger restart method
        resolutionMenuItem1.addActionListener(e -> {
            Panel.SCREEN_WIDTH = 800;
            Panel.SCREEN_HEIGHT = 540;
            restart(Panel.SCREEN_WIDTH, Panel.SCREEN_HEIGHT);
        });
        resolutionMenuItem2.addActionListener(e -> {
            Panel.SCREEN_WIDTH = 1020;
            Panel.SCREEN_HEIGHT = 700;
            restart(Panel.SCREEN_WIDTH, Panel.SCREEN_HEIGHT);
        });
        resolutionMenuItem3.addActionListener(e -> {
            Panel.SCREEN_WIDTH = 1360;
            Panel.SCREEN_HEIGHT = 700;
            restart(Panel.SCREEN_WIDTH, Panel.SCREEN_HEIGHT);
        });
        resolutionMenuItem4.addActionListener(e -> {
            Panel.SCREEN_WIDTH = 1400;
            Panel.SCREEN_HEIGHT = 980;
            restart(Panel.SCREEN_WIDTH, Panel.SCREEN_HEIGHT);
        });
        resolutionMenuItem5.addActionListener(e -> {
            Panel.SCREEN_WIDTH = 1600;
            Panel.SCREEN_HEIGHT = 840;
            restart(Panel.SCREEN_WIDTH, Panel.SCREEN_HEIGHT);
        });
        resolutionMenuItem6.addActionListener(e -> {
            Panel.SCREEN_WIDTH = 1920;
            Panel.SCREEN_HEIGHT = 1020;
            restart(Panel.SCREEN_WIDTH, Panel.SCREEN_HEIGHT);
        });
        resolutionMenuItem7.addActionListener(e -> {
            Panel.SCREEN_WIDTH = 2560;
            Panel.SCREEN_HEIGHT = 1380;
            restart(Panel.SCREEN_WIDTH, Panel.SCREEN_HEIGHT);
        });
        resolutionMenuItem8.addActionListener(e -> {
            Panel.SCREEN_WIDTH = 3840;
            Panel.SCREEN_HEIGHT = 2100;
            restart(Panel.SCREEN_WIDTH, Panel.SCREEN_HEIGHT);
        });
        bgmMenuItem1.addActionListener(e -> {
            bgm.start();
            bgm.loop(Clip.LOOP_CONTINUOUSLY);
        });
        bgmMenuItem2.addActionListener(e -> bgm.stop());
        //binding main bar
        this.setJMenuBar(menuBar);
        //generate game main panel
        this.add(new Panel());
        this.setTitle("Snake Game");
        //define the close operation
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //adaptive size
        this.pack();
        this.setVisible(true);
        //set location to center
        this.setLocationRelativeTo(null);
        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass()
                    .getResource("./icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setIconImage(image);
        File bgmPath = new File("bgm.wav");
        try {
            AudioInputStream aIS = AudioSystem.getAudioInputStream(bgmPath);
            bgm = AudioSystem.getClip();
            bgm.open(aIS);
            bgm.start();
            bgm.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //set new resolution with restart the game and resize the frame
    public void restart(int SCREEN_WIDTH, int SCREEN_HEIGHT){
        this.setSize(SCREEN_WIDTH + 15, SCREEN_HEIGHT + 60);
        //restart by Key_R hit in panel
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_R);
            robot.keyRelease(KeyEvent.VK_R);
        }
        catch (AWTException eA) {
            eA.printStackTrace();
        }
    }
}
