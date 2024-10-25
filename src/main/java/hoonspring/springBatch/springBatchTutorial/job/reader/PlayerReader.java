package hoonspring.springBatch.springBatchTutorial.job.reader;

import hoonspring.springBatch.springBatchTutorial.domain.Player;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class PlayerReader implements ItemStreamReader<Player> {

    @Override
    public Player read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return null;
    }
}
