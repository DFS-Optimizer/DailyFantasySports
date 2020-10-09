
package com.example.testrequest;


        import org.json.JSONException;

        import java.io.IOException;
        import java.util.List;

        //import nw15s305.plantplaces.com.dto.PlantDTO;


public interface Populate{
    List<UnitTest> fetchPlants(String searchTerm) throws IOException, JSONException;
}