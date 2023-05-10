import java.math.BigInteger;
import java.nio.ByteBuffer;

public class KeyExpansion implements IKeyExpansion
{
    static final int NUMBER_OF_ROUND_KEYS = 16;
    @Override
    public byte[][] generateRoundKeys(byte[] key)
    {
        byte[][] roundKeys = new byte[NUMBER_OF_ROUND_KEYS][];
        byte[] key56 = Main.permutationBits(key, Main.KE_1);

        byte[] C = Main.extractArrayBits(key56, 0, Main.KE_1.length / 2);
        byte[] D = Main.extractArrayBits(key56, Main.KE_1.length / 2, Main.KE_1.length / 2);

        for (int i = 0; i < NUMBER_OF_ROUND_KEYS; i++)
        {
            C = shiftBits(C, Main.KShift[i], i);
            D = shiftBits(D, Main.KShift[i], i);

            key56 = concateBitArray(C, D);
            roundKeys[i] = Main.permutationBits(key56, Main.KE_2);
        }
        return roundKeys;
    }

    @Override
    public byte[] shiftBits(byte[] inputArray, int shift, int r)
    {
        BigInteger bigInt = new BigInteger(inputArray);
        int shiftInt = bigInt.intValue();
        if (r == 0)
        {
            shiftInt = shiftInt >>> 4;
        }
        shiftInt = ((shiftInt << shift) & 268435455) | (shiftInt >>> (28 - shift));
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.putInt(shiftInt);
        return buf.array();
    }

    @Override
    public byte[] concateBitArray(byte[] arrayFirst, byte[] arraySecond)
    {
        byte[] resultArray = new byte[(arrayFirst.length + arraySecond.length) / Main.NUMBER_BIT_IN_BYTE];
        int j = 0;
        int bit;
        for (int i = 0; i < arrayFirst.length; i++)
        {
            bit = Main.extractBitFromArray(arrayFirst, i);
            Main.setBitIntoArray(resultArray, i, bit);
            j += 1;
        }
        for (int i = 0; i < arraySecond.length; i++)
        {
            bit = Main.extractBitFromArray(arraySecond, i);
            Main.setBitIntoArray(resultArray, j, bit);
            j += 1;
        }
        return resultArray;
    }
}
