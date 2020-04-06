package tracker;

import lombok.Data;

import java.util.List;

@Data
public class ArgumentConstants {
    private List<Role> roles;
    private List<SummonerSpell> summonerSpells;
    private List<CDReduction> reductions;
}
