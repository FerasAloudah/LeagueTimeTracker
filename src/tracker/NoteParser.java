package tracker;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class NoteParser {
    private ArgumentConstants argumentConstants;

    public NoteParser() {
        try (Reader reader = new FileReader("constants.json")) {
            argumentConstants = new Gson().fromJson(reader, ArgumentConstants.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getTPCooldown(int level) {
        return (int) (430.588 - 10.588 * level);
    }

    public Role getRole(String word) {
        word = word.toUpperCase();
        for (Role role : argumentConstants.getRoles()) {
            if (role.getInputs().contains(word)) {
                return role;
            }
        }
        return null;
    }

    public SummonerSpell getSummonerSpell(String word) {
        word = word.toUpperCase();
        for (SummonerSpell summonerSpell : argumentConstants.getSummonerSpells()) {
            if (summonerSpell.getInputs().contains(word)) {
                return summonerSpell;
            }
        }
        return null;
    }

    public double getCDReduction(String word) {
        word = word.toUpperCase();
        for (CDReduction reduction : argumentConstants.getReductions()) {
            if (reduction.getInputs().contains(word)) {
                return reduction.getReduction();
            }
        }
        return 0;
    }

    public Note parseNote(String line) {
        Note note = new Note();
        Role role;
        SummonerSpell summonerSpell;
        double reduction;
        double multiplier = 1.0;
        int level = 1;
        for (String word : line.split(" ")) {
            if ((role = getRole(word)) != null) {
                note.setRole(role.getName());
                note.setRoleIndex(role.getIndex());
            } else if ((summonerSpell = getSummonerSpell(word)) != null) {
                note.setSummonerSpell(summonerSpell.getName());
                note.setTime(summonerSpell.getTimer());
            } else if ((reduction = getCDReduction(word)) != 0) {
                multiplier -= reduction;
            } else if (word.matches("\\d+")) {
                // Check if word contains digits only.
                level = Integer.parseInt(word);
            }
        }

        if (!note.isValid()) {
            return null;
        }

        if (note.getSummonerSpell().equals("Teleport")) {
            note.setTime(getTPCooldown(level));
        }
        note.setTime((int) (note.getTime() * multiplier));

        return note;
    }
}
