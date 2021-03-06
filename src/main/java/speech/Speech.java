package speech;

import entities.Sentence;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import speech.synthesiser.SynthesiserV2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;


public class Speech {

    //Create a Synthesizer instance
    private final SynthesiserV2 synthesizer = new SynthesiserV2("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");

    public void speak(String text, String language_code) {
        synthesizer.setLanguage(language_code);
        //Create a JLayer instance
        synthesizer.setSpeed(1);
        AdvancedPlayer player = null;
        try {
            player = new AdvancedPlayer(synthesizer.getMP3Data(text));
            player.play();
        } catch (JavaLayerException | IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(String folder_path, Sentence sentence) {
        Boolean rez = false;
        int try_count = 0;

        while (!rez && (try_count < 5)) {
            try_count++;
            rez = processWord(folder_path, sentence);
        }
    }

    private boolean processWord(String folder_path, Sentence sentence) {
        this.synthesizer.setLanguage("en-us");
        String output_file = sentence.getSentence() + ".mp3";
        try {
            process(folder_path, sentence.getSentence(), output_file);
            this.synthesizer.setLanguage("uk");
            String output_file_ukr = sentence.getSentence() + "_ukr" + ".mp3";
            process(folder_path, sentence.getFragment(), output_file_ukr);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void process(String folder_path, String word, String output_file) throws IOException {
        List<String> word_list = Arrays.asList(word.split(" "));
        File output = new File(folder_path + "//" + output_file);
        if (!output.exists()) output.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(output);
        synthesizer.setSpeed(1);
        InputStream inputStream = synthesizer.getMP3Data(word_list);
        int rez = 0;
        while (rez > -1) {
            rez = inputStream.read();
            outputStream.write(rez);
        }
        outputStream.flush();
        outputStream.close();
    }
}

