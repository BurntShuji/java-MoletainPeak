package sounds;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Moletain Peak Sound Effects
 * BS COMPUTER ENGINEERING
 */
public class Sound {
    private final URL music;
    private final URL hitMole;
    private final URL hitGold;
    private final URL hitTrap;
    private final URL correct;
    private final URL wrong;
    private final URL countdown;
    
    public Sound(){
        this.music = this.getClass().getClassLoader().getResource("sounds/bgm.wav");
        this.hitMole = this.getClass().getClassLoader().getResource("sounds/hit.wav");
        this.hitGold = this.getClass().getClassLoader().getResource("sounds/chance.wav");
        this.hitTrap= this.getClass().getClassLoader().getResource("sounds/kill.wav");
        this.correct = this.getClass().getClassLoader().getResource("sounds/correct.wav");
        this.wrong = this.getClass().getClassLoader().getResource("sounds/wrong.wav");
        this.countdown = this.getClass().getClassLoader().getResource("sounds/countdown.wav");
    }
    
    //Methods to play the sound
    public void bgm(){
        try{
            AudioInputStream forLoop = AudioSystem.getAudioInputStream(music);
            Clip clip = AudioSystem.getClip();
            clip.open(forLoop);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e){
            System.out.println(e);
        }
    }
    public void moleSfx(){
        play(hitMole);
    }    
    public void goldSfx(){
        play(hitGold);
    }
    public void trapSfx(){
        play(hitTrap);
    }    
    public void correctSfx(){
        play(correct);
    }    
    public void wrongSfx(){
        play(wrong);
    }    
    public void countSfx(){
        play(countdown);
    }
    
    private void play (URL url){
        try{
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event){
                    if(event.getType() == LineEvent.Type.STOP){
                        clip.close();
                    }
                }
            });
            audioIn.close();
            clip.start();
        } catch(Exception e){
            System.err.println(e);
        }
    }
}
