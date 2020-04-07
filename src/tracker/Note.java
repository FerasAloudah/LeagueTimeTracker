package tracker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note implements Serializable {
    private static final long serialVersionUID = 8581703854704855954L;
    private String name;
    private String role;
    private String summonerSpell;
    private int roleIndex;
    private int time;

    public Note(String name) {
        this.name = name;
    }

    public void decreaseSeconds() {
        --time;
    }

    public boolean isFinished() {
        return time <= 0;
    }

    public boolean isValid() {
        return role != null && summonerSpell != null;
    }

    private String timeInMinutes() {
        return (time / 60) + "m" + (time % 60) + "s";
    }

    public String toString() {
        return summonerSpell + " - " + timeInMinutes();
    }
}
