public interface IKeyExpansion
{
    public byte[][] generateRoundKeys(byte[] key);
    public byte[] shiftBits(byte[] inputArray, int shift, int r);
    public byte[] concateBitArray(byte[] arrayFirst, byte[] arraySecond);
}
