package com.shotin.addressgenerator.random;

import org.junit.Assert;
import org.junit.Test;

public class LocalRandomProviderTest {

    private LocalRandomProvider localRandomProvider = new LocalRandomProvider();

    @Test
    public void testRandomizeStringFromNull() {
        String random = localRandomProvider.randomizeString(null, 10);
        Assert.assertNull(random);
    }

    @Test
    public void testRandomizeString() {
        String random = localRandomProvider.randomizeString("AbcdefgHijklmnOpqrstuw", 10);
        Assert.assertEquals(10, random.length());
    }

    @Test
    public void testRandomizeStringMinus1() {
        String random = localRandomProvider.randomizeString("AbcdefgHijklmnOpqrstuw", -1);
        Assert.assertTrue(random.isEmpty());
    }
}
