package forestInspector;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class FRL {
    List<ForestInspectorResponse> responses = new ArrayList<ForestInspectorResponse>();

}
