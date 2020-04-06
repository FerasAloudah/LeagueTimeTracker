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
    private String role;
    private int roleIndex;
    private String summonerSpell;
    private int time;

    public void decreaseSecond() {
        time--;
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
        return String.join(" ", summonerSpell, "-", timeInMinutes());
    }
}
