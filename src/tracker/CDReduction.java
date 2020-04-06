package tracker;

import lombok.Data;

import java.util.List;

@Data
public class CDReduction {
    private List<String> inputs;
    private double reduction;
}
