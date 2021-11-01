import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class EntitySentimentServiceTest {


    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test entity is not empty
     */
    @Test
    public void testAnalyseSpeech(){
        SpeechService speechService = new SpeechService();
        assertNotNull(new EntitySentimentService().analyseSpeech(speechService.generatePath("Restaurant")));
    }

    /**
     * Test time out of the api
     */
    @Test (timeout = 1000)
    public void testAnalyseSpeechTime(){
        SpeechService speechService = new SpeechService();
        assertNotNull(new EntitySentimentService().analyseSpeech(speechService.generatePath("Restaurant")));
    }

    /**
     * Test the list is ranked by sentiment score
     */
    @Test
    public void generateList() {
        EntitySentimentService entitySentimentService = new EntitySentimentService();
        SpeechService speechService = new SpeechService();
        String filePath = speechService.generatePath("Restaurant");
        List<SpeechEntity> list = entitySentimentService.analyseSpeech(speechService.speechRecognize(filePath));
        List<SpeechEntity> rankedList = entitySentimentService.generateList(list);
        assertTrue(rankedList.get(0).getSentiment() > rankedList.get(1).getSentiment());
    }
}