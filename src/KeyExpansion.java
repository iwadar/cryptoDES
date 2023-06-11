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
            C = shiftBits(C, 28, Main.KShift[i]);
            D = shiftBits(D, 28, Main.KShift[i]);

            key56 = Main.concateBitArray(C, 28, D, 28);
            roundKeys[i] = Main.permutationBits(key56, Main.KE_2);
        }
        return roundKeys;
    }

    @Override
    public byte[] shiftBits(byte[] inputArray, int len, int shift)
    {
        int nrBytes = (len - 1) / 8 + 1;
        byte[] out = new byte[nrBytes];
        for (int i = 0; i < len; i++)
        {
            int val = Main.extractBitFromArray(inputArray, (i + shift) % len);
            Main.setBitIntoArray(out, i, val);
        }
        return out;
    }
}
