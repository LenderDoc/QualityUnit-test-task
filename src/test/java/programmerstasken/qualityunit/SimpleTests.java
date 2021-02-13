package programmerstasken.qualityunit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class SimpleTests {

    @Test
    void ServiceIdTest() {
        assertFalse(new ServiceId("*").match(new ServiceId("2.2")));
        assertFalse(new ServiceId("1.2").match(new ServiceId("1.3")));
        
        assertTrue(new ServiceId("1.2").match(new ServiceId("1")));
        assertTrue(new ServiceId("2.2").match(new ServiceId("*")));
        assertTrue(new ServiceId("1.2").match(new ServiceId("1.2")));
    }

    @Test
    void QuestionTypeIdTest() {
        assertFalse(new QuestionTypeId("*").match(new QuestionTypeId("2.2")));
        assertFalse(new QuestionTypeId("1.2").match(new QuestionTypeId("1.3")));
        assertFalse(new QuestionTypeId("2.2").match(new QuestionTypeId("2.2.2")));
        assertFalse(new QuestionTypeId("2.2.2").match(new QuestionTypeId("2.2.1")));
        
        assertTrue(new QuestionTypeId("1.2").match(new QuestionTypeId("1")));
        assertTrue(new QuestionTypeId("2.2").match(new QuestionTypeId("*")));
        assertTrue(new QuestionTypeId("1.2").match(new QuestionTypeId("1.2")));
        assertTrue(new QuestionTypeId("2.2.2").match(new QuestionTypeId("2.2")));
        assertTrue(new QuestionTypeId("2.2.2").match(new QuestionTypeId("2.2.2")));
    }
    
    @Test
    void WaitingTimeLineTest(){
        assertEquals(new WaitingTimeLine("C 1.1 8.15.1 P 15.10.2012 83").toString(), "C 1.1 8.15.1 P 15.10.2012 83");
        assertEquals(new WaitingTimeLine("C 1 10.1 P 01.12.2012 65").toString(), "C 1 10.1 P 01.12.2012 65");
        assertEquals(new WaitingTimeLine("C 1.1 5.5.1 P 01.11.2012 117").toString(), "C 1.1 5.5.1 P 01.11.2012 117");
        assertEquals(new WaitingTimeLine("C 3 10.2 N 02.10.2012 100").toString(), "C 3 10.2 N 02.10.2012 100");        
    }
    
    @Test
    void QueryLineTest(){
        assertEquals(new QueryLine("D 1.1 8 P 01.01.2012-01.12.2012").toString(), "D 1.1 8 P 01.01.2012-01.12.2012");
        assertEquals(new QueryLine("D 1 * P 08.10.2012-20.11.2012").toString(), "D 1 * P 08.10.2012-20.11.2012");
        assertEquals(new QueryLine("D 3 10 P 01.12.2012").toString(), "D 3 10 P 01.12.2012");
    }
    
    @Test
    void ProcessorTest()
    {
        final Processor processor = new Processor();
        
        processor.process("C 1.1 8.15.1 P 15.10.2012 83");
        processor.process("C 1 10.1 P 01.12.2012 65");
        processor.process("C 1.1 5.5.1 P 01.11.2012 117");
        processor.process("D 1.1 8 P 01.01.2012-01.12.2012");
        
        assertEquals(processor.getLastQueryResult(), "83");
        
        processor.process("C 3 10.2 N 02.10.2012 100");
        processor.process("D 1 * P 08.10.2012-20.11.2012");
        
        assertEquals(processor.getLastQueryResult(), "100");
        
        processor.process("D 3 10 P 01.12.2012");
        
        assertEquals(processor.getLastQueryResult(), "-");
    }
}
