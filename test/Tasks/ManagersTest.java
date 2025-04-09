package Tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ManagersTest {
    @Test
    public void getDefaultShouldNotReturnNull(){
        Assertions.assertNotNull(Managers.getDefault(),"return null");
    }
    @Test
    public void getDefaultHistoryShouldNotReturnNull(){
        Assertions.assertNotNull(Managers.getDefaultHistory(),"return null");
    }
}
