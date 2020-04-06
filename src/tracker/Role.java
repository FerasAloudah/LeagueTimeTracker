package tracker;

import lombok.Data;

import java.util.List;

@Data
public class Role {
    private String name;
    private List<String> inputs;
    private int index;
}
