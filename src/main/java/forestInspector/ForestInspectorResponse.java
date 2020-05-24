package forestInspector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties
public class ForestInspectorResponse {
    @JsonProperty("ID")
    private float ID;
    private float L;
    private float l;
    private float V;
    private float TR;
    private String TIME;
    private String NRM;


}