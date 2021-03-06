package tests.immutablecollections;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import de.peeeq.datastructures.Partitions;
import de.peeeq.wurstscript.translation.imtranslation.IntRange;

public class IntRangeTests {

	@Test
	public void test1() {
		List<IntRange> parts = IntRange.createFromIntList(Lists.newArrayList(5,6,7,10,11,12));
		assertEquals("[5..7, 10..12]", parts.toString());
		parts = IntRange.createFromIntList(Lists.<Integer>newArrayList());
		assertEquals("[]", parts.toString());
		parts = IntRange.createFromIntList(Lists.newArrayList(5,6,7,10,11,12,15));
		assertEquals("[5..7, 10..12, 15..15]", parts.toString());
		parts = IntRange.createFromIntList(Lists.newArrayList(12,6,7,15,10,5,11));
		assertEquals("[5..7, 10..12, 15..15]", parts.toString());
		
	}
	

}
