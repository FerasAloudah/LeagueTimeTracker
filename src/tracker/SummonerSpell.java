package tracker;

import lombok.Data;

import java.util.List;

@Data
public class SummonerSpell {
    private String name;
    private List<String> inputs;
    private int timer;
}
